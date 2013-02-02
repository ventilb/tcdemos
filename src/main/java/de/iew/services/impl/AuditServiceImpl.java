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
import de.iew.persistence.AuditEventMessageDao;
import de.iew.services.AuditService;
import de.iew.services.events.AuditEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

/**
 * Implements a simple audit service.
 *
 * @author Manuel Schulze <mschulze@geneon.de>
 * @since 24.01.13 - 21:01
 */
@Service(value = "auditService")
public class AuditServiceImpl implements AuditService, ApplicationListener<ApplicationEvent> {

    private static final Log log = LogFactory.getLog(AuditServiceImpl.class);

    public CollectionHolder<AuditEventMessage> getAuditEventMessages(long firstItem, long itemCount) {
        if (log.isDebugEnabled()) {
            log.debug("Lade AuditMessages ab " + firstItem + " mit Anzahl " + itemCount + ".");
        }

        long totalCount = this.auditEventMessageDao.count();
        if (log.isDebugEnabled()) {
            log.debug("Es sind " + totalCount + " AuditMessages vorhanden.");
        }

        Collection<AuditEventMessage> auditMessages = this.auditEventMessageDao.findAll(firstItem, itemCount);
        CollectionHolder<AuditEventMessage> collectionHolder = new CollectionHolder<AuditEventMessage>(auditMessages, firstItem, totalCount);

        return collectionHolder;
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AuditEvent) {
            onAuditEvent(event.getSource(), (AuditEvent) event);
        }
    }

    /**
     * Handles the audit event.
     *
     * @param source the event source
     * @param evt    the audit event
     */
    public void onAuditEvent(Object source, AuditEvent evt) {
        if (log.isDebugEnabled()) {
            log.debug("Received Audit Event " + evt + " from " + source);
        }

        String principal = null;

        Authentication authentication = evt.getAuthentication();
        if (authentication != null) {
            principal = authentication.getName();
        }

        AuditEventMessage auditEventMessage = new AuditEventMessage();
        auditEventMessage.setTimestamp(new Date());
        auditEventMessage.setPrincipal(principal);
        auditEventMessage.setSeverity(evt.getSeverity());
        auditEventMessage.setMessage(evt.getMessage());

        this.auditEventMessageDao.save(auditEventMessage);
    }

    // Service und Dao Abhängigkeiten /////////////////////////////////////////

    private AuditEventMessageDao auditEventMessageDao;

    @Autowired
    public void setAuditEventMessageDao(AuditEventMessageDao auditEventMessageDao) {
        this.auditEventMessageDao = auditEventMessageDao;
    }
}
