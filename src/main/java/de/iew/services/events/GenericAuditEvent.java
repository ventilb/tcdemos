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

package de.iew.services.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;

/**
 * Extension of the spring {@link ApplicationEvent} class and provides an
 * implementation of the {@link AuditEvent} interface.
 * <p>
 * This event can be used to fire generic audit events.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.02.13 - 23:04
 */
public class GenericAuditEvent extends ApplicationEvent implements AuditEvent {

    private long auditEventTimestamp;

    private Authentication authentication;

    private Severity severity;

    private String message;

    private Throwable throwable;

    public GenericAuditEvent(Object source, long auditEventTimestamp, Authentication authentication, Severity severity, String message) {
        this(source, auditEventTimestamp, authentication, severity, message, null);
    }

    public GenericAuditEvent(Object source, long auditEventTimestamp, Authentication authentication, Severity severity, String message, Throwable throwable) {
        super(source);
        this.auditEventTimestamp = auditEventTimestamp;
        this.authentication = authentication;
        this.severity = severity;
        this.message = message;
        this.throwable = throwable;
    }

    public long getAuditEventTimestamp() {
        return auditEventTimestamp;
    }

    public Authentication getAuthentication() {
        return this.authentication;
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public String getMessage() {
        return this.message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
