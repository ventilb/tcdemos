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

import de.iew.services.AuditService;
import de.iew.services.events.AuditEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * Implements a simple audit service.
 *
 * @author Manuel Schulze <mschulze@geneon.de>
 * @since 24.01.13 - 21:01
 */
@Service(value = "auditService")
public class AuditServiceImpl implements AuditService, ApplicationListener<ApplicationEvent> {

    private static final Log log = LogFactory.getLog(AuditServiceImpl.class);

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
    }
}
