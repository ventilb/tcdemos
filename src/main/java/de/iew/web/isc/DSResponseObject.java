/*
 * Copyright 2012 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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

package de.iew.web.isc;

import org.springframework.ui.ExtendedModelMap;

/**
 * Implementiert ein Objekt für das Versenden von Smartclient Webservice
 * Antworten.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.11.12 - 23:27
 */
public class DSResponseObject extends DSResponseAbstract {

    public DSResponseObject() {
        init();
    }

    public DSResponseObject(Object data) {
        init(data);
    }

    private void init() {
        setStatus(STATUS_SUCCESS);
    }

    private void init(Object data) {
        setStatus(STATUS_SUCCESS);
        setObjectData(data);
    }

    public void setObjectData(Object data) {
        getResponseModel().put("data", data);
    }
}
