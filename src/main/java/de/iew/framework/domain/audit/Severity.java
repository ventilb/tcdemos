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

package de.iew.framework.domain.audit;

/**
 * Enum of the audit events severity's.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 12.02.2013 - 22:55
 */
public enum Severity {
    /**
     * Used to track calls through the system.
     */
    TRACE,
    /**
     * Used to print out developer informations.
     */
    DEBUG,
    /**
     * Default. Used for informational purposes.
     */
    INFO,
    /**
     * Something went wrong. You should investigate the cause.
     */
    WARN,
    /**
     * Something definitively went wrong. Administrators should be notified
     * about these issues.
     */
    CRITICAL;

    public boolean isHigherOrEqualAs(Severity other) {
        return this.ordinal() >= other.ordinal();
    }
}
