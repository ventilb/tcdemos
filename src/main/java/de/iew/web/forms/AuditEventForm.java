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

package de.iew.web.forms;

import de.iew.web.dto.WebDTO;

/**
 * Form object to transfer audit event message data between controller and browser.
 * <p>
 * This class is required because Smartclient request timestamps in a specific format.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://smartclient.com/docs/8.3/a/b/c/go.html#group..dateFormatAndStorage">http://smartclient.com/docs/8.3/a/b/c/go.html#group..dateFormatAndStorage</a>
 * @since 25.03.13 - 22:32
 */
public class AuditEventForm extends WebDTO {

    private long id;

    private String timestamp;

    private String principal;

    private String severity;

    private String message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
