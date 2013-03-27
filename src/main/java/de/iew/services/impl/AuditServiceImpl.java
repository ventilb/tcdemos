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

import de.iew.framework.audit.AuditEvent;
import de.iew.framework.audit.Log4jAuditEvent;
import de.iew.framework.domain.audit.AuditEventMessage;
import de.iew.framework.domain.audit.Severity;
import de.iew.framework.domain.utils.CollectionHolder;
import de.iew.framework.persistence.AuditEventMessageDao;
import de.iew.services.AuditService;
import de.iew.framework.utils.IewApplicationEvent;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.Date;

/**
 * Implements a simple audit service.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 24.01.13 - 21:01
 */
//@Service(value = "auditService")
@ManagedResource(objectName = "de.iew:name=AuditService", description = "AuditService für wichtige System Ereignisse")
public class AuditServiceImpl implements AuditService {

    private static final Log log = LogFactory.getLog(AuditServiceImpl.class);

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

        AuditEventMessage auditEventMessage;
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
    }

    protected String toAuditEventMessage(String message, Throwable throwable) {
        StringBuilder sb = new StringBuilder(message);
        if (throwable != null) {
            sb.append(SystemUtils.LINE_SEPARATOR);
            sb.append(ExceptionUtils.getFullStackTrace(throwable));
        }
        return sb.toString();
    }

    // Service Activator method
    public void handleApplicationEventMessage(Message<?> message) {
        IewApplicationEvent iewApplicationEvent = (IewApplicationEvent) message.getPayload();
        if (iewApplicationEvent instanceof AuditEvent) {
            onAuditEvent(iewApplicationEvent.getSource(), (AuditEvent) iewApplicationEvent);
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
            if (evt instanceof Log4jAuditEvent) {
                date = new Date(((Log4jAuditEvent) evt).getAuditEventTimestamp());
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

    // Service und Dao Abhängigkeiten /////////////////////////////////////////

    private AuditEventMessageDao auditEventMessageDao;

    @Autowired
    public void setAuditEventMessageDao(AuditEventMessageDao auditEventMessageDao) {
        this.auditEventMessageDao = auditEventMessageDao;
    }

}
