/*
 * Copyright 2012-2013 Manuel Schulze <manuel_schulze@i-entwicklung.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iew.services.impl;

import de.iew.framework.audit.Log4jAuditAppender;
import de.iew.framework.domain.audit.AuditEventMessage;
import de.iew.framework.domain.audit.Severity;
import de.iew.framework.domain.utils.CollectionHolder;
import de.iew.framework.persistence.AuditEventMessageDao;
import de.iew.services.AuditService;
import de.iew.services.MessagePassingService;
import de.iew.framework.audit.AuditEvent;
import de.iew.framework.audit.GenericAuditEvent;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.integration.MessageChannel;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.management.Notification;
import java.util.Collection;
import java.util.Date;

/**
 * Implements a simple audit service.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 24.01.13 - 21:01
 */
@Service(value = "auditService")
@ManagedResource(objectName = "modelmbean:name=AuditService", description = "AuditService für wichtige System Ereignisse")
public class AuditServiceImpl implements AuditService, ApplicationListener<ApplicationEvent>, InitializingBean, NotificationPublisherAware {

    private static final Log log = LogFactory.getLog(AuditServiceImpl.class);

    /**
     * The default audit message severity from which audit events are passed to the message passing service.
     */
    public static final Severity DEFAULT_MPS_PASS_SEVERITY = Severity.WARN;

    /**
     * The name of the log4j appender.
     */
    private String auditServiceAppenderName = "auditServiceAppenderName";

    /**
     * The name of the log4j appender logging level.
     */
    private Level auditServiceAppenderLog4jPriority = Log4jAuditAppender.DEFAULT_AUDIT_SERVICE_APPENDER_LOG4J_PRIORITY;

    /**
     * The audit message severity from which audit events are passed to the message passing service.
     */
    private Severity mpsPassSeverity = DEFAULT_MPS_PASS_SEVERITY;

    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(this.auditServiceAppenderName);
        Assert.notNull(this.auditServiceAppenderLog4jPriority);
        Assert.notNull(this.mpsPassSeverity);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public CollectionHolder<AuditEventMessage> getAuditEventMessages(long firstItem, long itemCount) {
        if (log.isDebugEnabled()) {
            log.debug("Lade AuditMessages ab " + firstItem + " mit Anzahl " + itemCount + ".");
        }

        long totalCount = this.auditEventMessageDao.count();
        if (log.isDebugEnabled()) {
            log.debug("Es sind " + totalCount + " AuditMessages vorhanden.");
        }

        Collection<AuditEventMessage> auditMessages = this.auditEventMessageDao.findAll(firstItem, itemCount);

        return new CollectionHolder<AuditEventMessage>(auditMessages, firstItem, totalCount);
    }

    protected void persistMessage(Object source, Date date, Authentication authentication, Severity severity, String message, Throwable throwable) {
        String principal = null;

        if (authentication != null) {
            principal = authentication.getName();
        }

        try {
            passMessage(severity, message);
            passThrowable(severity, throwable);
        } catch (Exception e) {
            if (log.isFatalEnabled()) {
                log.fatal("AuditEvent was not passed to " + MessagePassingService.class.getName() + " due to an unexpected error", e);
            }
        }

        AuditEventMessage auditEventMessage = null;
        try {
            auditEventMessage = new AuditEventMessage();
            auditEventMessage.setTimestamp(date);
            auditEventMessage.setPrincipal(principal);
            auditEventMessage.setSeverity(severity);
            auditEventMessage.setMessage(toAuditEventMessage(message, throwable));

            this.auditEventMessageDao.save(auditEventMessage);
        } catch (Exception e) {
            if (log.isFatalEnabled()) {
                log.fatal("AuditEvent was not persisted due to an unexpected error", e);
            }
        }

        try {
            if (auditEventMessage != null) {
                sendMBeanNotification(source, date, severity, message, throwable);
            }
        } catch (Exception e) {
            if (log.isFatalEnabled()) {
                log.fatal("AuditEvent was not populated to mbean service due to an unexpected error", e);
            }
        }
    }

    protected String toAuditEventMessage(String message, Throwable throwable) {
        StringBuilder sb = new StringBuilder(message);
        if (throwable != null) {
            sb.append(SystemUtils.LINE_SEPARATOR);
            sb.append(ExceptionUtils.getFullStackTrace(throwable));
        }
        return sb.toString();
    }

    protected void passMessage(Severity severity, String message) {
        if (severity.isHigherOrEqualAs(this.mpsPassSeverity)) {
            this.messagePassingService.passSystemMessage(message);
        }
    }

    protected void passThrowable(Severity severity, Throwable throwable) {
        if (severity.isHigherOrEqualAs(this.mpsPassSeverity)) {
            this.messagePassingService.passThrowableSystemMessage(throwable);
        }
    }

    protected void sendMBeanNotification(Object source, Date timestamp, Severity severity, String message, Throwable throwable) {
        Notification notification = new Notification(AuditEvent.class.getName(), source.toString(), notificationSequence++, timestamp.getTime(), message);
        if (throwable != null) {
            String throwableMessage = throwable.getLocalizedMessage();

            StringBuffer sb = new StringBuffer(throwableMessage == null ? "": throwableMessage);
            sb.append(SystemUtils.LINE_SEPARATOR).append(ExceptionUtils.getFullStackTrace(throwable));
            notification.setUserData(sb);
        }

        this.notificationPublisher.sendNotification(notification);
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AuditEvent) {
            onAuditEvent(event.getSource(), (AuditEvent) event);
        } else if (event instanceof ContextStartedEvent) {
            configureLog4jForAuditLogging();
        } else if (event instanceof ContextRefreshedEvent) {
            configureLog4jForAuditLogging();
        }
    }

    /**
     * Handles the audit event.
     *
     * @param source the event source
     * @param evt    the audit event
     */
    public void onAuditEvent(Object source, AuditEvent evt) {
        try {
            /*
            Beware of logging from here. You will enter an infinite loop. Ignoring events coming from ourselves.
             */
            if (checkIgnoreAuditEventBySource(source)) {
                return;
            }
            Date date;
            if (evt instanceof GenericAuditEvent) {
                date = new Date(((GenericAuditEvent) evt).getAuditEventTimestamp());
            } else {
                date = new Date();
            }

            Authentication authentication = evt.getAuthentication();
            persistMessage(source, date, authentication, evt.getSeverity(), evt.getMessage(), evt.getThrowable());
        } catch (Exception e) {
            if (log.isFatalEnabled()) {
                log.fatal("AuditEvent was not persisted due to an unexpected error", e);
            }
        }
    }

    protected boolean checkIgnoreAuditEventBySource(Object source) {
        boolean ignore = false;
        if (source != null) {
            String sourceAsString = source.toString();
            ignore = sourceAsString.contains(AuditService.class.getSimpleName());
        }
        return ignore;
    }

    /**
     * Adds an {@link de.iew.framework.audit.Log4jAuditAppender} instance to the Log4j root appender
     * for logging messages through this audit service.
     */
    protected synchronized void configureLog4jForAuditLogging() {
        if (!isLog4jConfiguredForAuditLogging()) {
            MessageChannel auditEventChannel = this.applicationEventChannel;

            if (auditEventChannel != null) {
                Logger logger = Logger.getRootLogger();
                Log4jAuditAppender log4jAuditAppender = new Log4jAuditAppender(auditEventChannel);
                log4jAuditAppender.setName(this.auditServiceAppenderName);

                logger.addAppender(log4jAuditAppender);

                changeAuditServiceAppenderLog4jPriority();
            } else {
                if (log.isInfoEnabled()) {
                    log.info("The audit event channel is not configured. The audit service appender will not be registered to log4j.");
                }
            }
        }
    }

    /**
     * Is Log4j configured for audit logging.
     *
     * @return the boolean
     */
    protected synchronized boolean isLog4jConfiguredForAuditLogging() {
        Logger logger = Logger.getRootLogger();
        return (logger.getAppender(this.auditServiceAppenderName) != null);
    }

    /**
     * Changes the audit service appender Log4j priority.
     */
    protected synchronized void changeAuditServiceAppenderLog4jPriority() {
        Logger logger = Logger.getRootLogger();
        Log4jAuditAppender appender = (Log4jAuditAppender) logger.getAppender(this.auditServiceAppenderName);
        if (appender != null) {
            appender.setAuditServiceAppenderLog4jPriority(this.auditServiceAppenderLog4jPriority);
        }
    }

    @Value(value = "#{config['audit.audit_service_appender_name']}")
    public void setAuditServiceAppenderName(String auditServiceAppenderName) {
        this.auditServiceAppenderName = auditServiceAppenderName;
    }

    @ManagedAttribute(description = "The name of the log4j appender logging level.",
            defaultValue = "WARN")
    @Value(value = "#{config['audit.audit_service_appender_log4j_priority']}")
    public void setAuditServiceAppenderLog4jPriority(String auditServiceAppenderLog4jPriority) {
        this.auditServiceAppenderLog4jPriority = Level.toLevel(auditServiceAppenderLog4jPriority, Log4jAuditAppender.DEFAULT_AUDIT_SERVICE_APPENDER_LOG4J_PRIORITY);

        changeAuditServiceAppenderLog4jPriority();
    }

    @ManagedAttribute
    public String getAuditServiceAppenderLog4jPriority() {
        return this.auditServiceAppenderLog4jPriority.toString();
    }

    @ManagedAttribute(description = "The audit message severity from which audit events are passed to the message passing service.",
            defaultValue = "WARN")
    @Value(value = "#{config['audit.mps_pass_severity']}")
    public void setMpsPassSeverity(String mpsPassSeverity) {
        this.mpsPassSeverity = Severity.valueOf(mpsPassSeverity);
    }

    @ManagedAttribute
    public String getMpsPassSeverity() {
        return this.mpsPassSeverity.name();
    }

    // Service und Dao Abhängigkeiten /////////////////////////////////////////

    private AuditEventMessageDao auditEventMessageDao;

    private MessagePassingService messagePassingService;

    private MessageChannel applicationEventChannel;

    private NotificationPublisher notificationPublisher;

    private long notificationSequence = 0;

    @Autowired
    public void setAuditEventMessageDao(AuditEventMessageDao auditEventMessageDao) {
        this.auditEventMessageDao = auditEventMessageDao;
    }

    @Autowired
    public void setMessagePassingService(MessagePassingService messagePassingService) {
        this.messagePassingService = messagePassingService;
    }

    @Autowired(required = false)
    public void setApplicationEventChannel(MessageChannel applicationEventChannel) {
        this.applicationEventChannel = applicationEventChannel;
    }

    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }
}
