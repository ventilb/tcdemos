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

import de.iew.domain.audit.AuditEventMessage;
import de.iew.domain.utils.CollectionHolder;
import de.iew.framework.log4j.AuditServiceAppender;
import de.iew.persistence.AuditEventMessageDao;
import de.iew.services.AuditService;
import de.iew.services.MessagePassingService;
import de.iew.services.events.AuditEvent;
import de.iew.services.events.GenericAuditEvent;
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
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Date;

/**
 * Implements a simple audit service.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 24.01.13 - 21:01
 */
@Service(value = "auditService")
public class AuditServiceImpl implements AuditService, ApplicationListener<ApplicationEvent>, InitializingBean {

    private static final Log log = LogFactory.getLog(AuditServiceImpl.class);

    /**
     * The default audit message severity from which audit events are passed to the message passing service.
     */
    public static final AuditEvent.Severity DEFAULT_MPS_PASS_SEVERITY = AuditEvent.Severity.WARN;

    /**
     * The name of the log4j appender.
     */
    private String auditServiceAppenderName = "auditServiceAppenderName";

    /**
     * The name of the log4j appender logging level.
     */
    private Level auditServiceAppenderLog4jPriority = AuditServiceAppender.DEFAULT_AUDIT_SERVICE_APPENDER_LOG4J_PRIORITY;

    /**
     * The audit message severity from which audit events are passed to the message passing service.
     */
    private AuditEvent.Severity mpsPassSeverity = DEFAULT_MPS_PASS_SEVERITY;

    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(this.auditServiceAppenderName);
        Assert.notNull(this.auditServiceAppenderLog4jPriority);
        Assert.notNull(this.mpsPassSeverity);
    }

    @PostFilter(value = "hasRole(filterObject, 'ROLE_ADMIN')")
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

    protected void persistMessage(Date date, Authentication authentication, AuditEvent.Severity severity, String message, Throwable throwable) {
        String principal = null;

        if (authentication != null) {
            principal = authentication.getName();
        }

        passMessage(severity, message);
        passThrowable(severity, throwable);

        AuditEventMessage auditEventMessage = new AuditEventMessage();
        auditEventMessage.setTimestamp(date);
        auditEventMessage.setPrincipal(principal);
        auditEventMessage.setSeverity(severity);
        auditEventMessage.setMessage(toAuditEventMessage(message, throwable));

        this.auditEventMessageDao.save(auditEventMessage);
    }

    protected String toAuditEventMessage(String message, Throwable throwable) {
        StringBuilder sb = new StringBuilder(message);
        sb.append(SystemUtils.LINE_SEPARATOR);
        sb.append(ExceptionUtils.getFullStackTrace(throwable));
        return sb.toString();
    }

    protected void passMessage(AuditEvent.Severity severity, String message) {
        if (severity.isHigherOrEqualAs(this.mpsPassSeverity)) {
            this.messagePassingService.passSystemMessage(message);
        }
    }

    protected void passThrowable(AuditEvent.Severity severity, Throwable throwable) {
        if (severity.isHigherOrEqualAs(this.mpsPassSeverity)) {
            this.messagePassingService.passThrowableSystemMessage(throwable);
        }
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
        /*
        Beware of logging from here. You will enter an infinite loop.
         */
        Date date;
        if (evt instanceof GenericAuditEvent) {
            date = new Date(((GenericAuditEvent) evt).getAuditEventTimestamp());
        } else {
            date = new Date();
        }

        Authentication authentication = evt.getAuthentication();
        persistMessage(date, authentication, evt.getSeverity(), evt.getMessage(), evt.getThrowable());
    }

    /**
     * Adds an {@link AuditServiceAppender} instance to the Log4j root appender
     * for logging messages through this audit service.
     */
    protected synchronized void configureLog4jForAuditLogging() {
        if (!isLog4jConfiguredForAuditLogging()) {
            MessageChannel auditEventChannel = this.applicationEventChannel;

            if (auditEventChannel != null) {
                Logger logger = Logger.getRootLogger();
                AuditServiceAppender auditServiceAppender = new AuditServiceAppender(auditEventChannel);
                auditServiceAppender.setName(this.auditServiceAppenderName);

                logger.addAppender(auditServiceAppender);

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
        AuditServiceAppender appender = (AuditServiceAppender) logger.getAppender(this.auditServiceAppenderName);
        if (appender != null) {
            appender.setAuditServiceAppenderLog4jPriority(this.auditServiceAppenderLog4jPriority);
        }
    }

    @Value(value = "#{config['audit.audit_service_appender_name']}")
    public void setAuditServiceAppenderName(String auditServiceAppenderName) {
        this.auditServiceAppenderName = auditServiceAppenderName;
    }

    @Value(value = "#{config['audit.audit_service_appender_log4j_priority']}")
    public void setAuditServiceAppenderLog4jPriority(String name) {
        this.auditServiceAppenderLog4jPriority = Level.toLevel(name, AuditServiceAppender.DEFAULT_AUDIT_SERVICE_APPENDER_LOG4J_PRIORITY);

        changeAuditServiceAppenderLog4jPriority();
    }

    @Value(value = "#{config['audit.mps_pass_severity']}")
    public void setMpsPassSeverity(String mpsPassSeverity) {
        this.mpsPassSeverity = AuditEvent.Severity.valueOf(mpsPassSeverity);
    }

    // Service und Dao Abhängigkeiten /////////////////////////////////////////

    private AuditEventMessageDao auditEventMessageDao;

    private MessagePassingService messagePassingService;

    private MessageChannel applicationEventChannel;

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
}
