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

package de.iew.framework.domain;

/**
 * Exception, die gemeldet wird wenn ein Domainmodell nicht erstellt werden
 * kann oder im Zusammenhang der Domainmodelle abhängige Objekte nicht
 * erstellt werden konnten.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.01.13 - 21:16
 */
public class ModelInstantiationException extends Exception {

    public ModelInstantiationException() {
    }

    public ModelInstantiationException(String message) {
        super(message);
    }

    public ModelInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelInstantiationException(Throwable cause) {
        super(cause);
    }
}
