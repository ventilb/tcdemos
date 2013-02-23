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

package de.iew.services;

import de.iew.framework.domain.audit.AuditEventMessage;
import de.iew.framework.domain.utils.CollectionHolder;

/**
 * Describes an interface to implement audit services to track whats happening
 * in the application.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 24.01.13 - 21:01
 */
public interface AuditService {

    /**
     * Gets audit event messages.
     *
     * @param firstItem the first item
     * @param itemCount the item count
     * @return the audit event messages
     */
    public CollectionHolder<AuditEventMessage> getAuditEventMessages(long firstItem, long itemCount);
}
