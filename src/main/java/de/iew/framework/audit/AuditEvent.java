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

package de.iew.framework.audit;

import de.iew.framework.domain.audit.Severity;
import org.springframework.security.core.Authentication;

/**
 * Specifies an interface to declare application events as audit events.
 * <p>
 * Audit events will be intercepted by an {@link de.iew.services.AuditService}.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 24.01.13 - 21:03
 */
public interface AuditEvent {

    /**
     * Gets the authentication object which caused this audit event. May be
     * NULL.
     *
     * @return the authentication
     */
    public Authentication getAuthentication();

    /**
     * Gets the audit event severity.
     *
     * @return the severity
     */
    public Severity getSeverity();

    /**
     * Gets the audit event message.
     *
     * @return the message
     */
    public String getMessage();

    /**
     * Gets throwable.
     *
     * @return the throwable
     */
    public Throwable getThrowable();

}
