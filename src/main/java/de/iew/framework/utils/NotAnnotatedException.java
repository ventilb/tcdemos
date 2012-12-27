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

package de.iew.framework.utils;

/**
 * Wird gemeldet wenn eine Annotation erwartet wird, die aber nicht gesetzt
 * ist.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 11:47
 */
public class NotAnnotatedException extends Exception {

    private Class expectedAnnotation;

    public NotAnnotatedException(Class expectedAnnotation) {
        this(expectedAnnotation, null, null);
    }

    public NotAnnotatedException(Class expectedAnnotation, String message) {
        this(expectedAnnotation, message, null);
    }

    public NotAnnotatedException(Class expectedAnnotation, String message, Throwable cause) {
        super(message, cause);
        this.expectedAnnotation = expectedAnnotation;
    }

    public NotAnnotatedException(Class expectedAnnotation, Throwable cause) {
        this(expectedAnnotation, null, cause);
    }

    public Class getExpectedAnnotation() {
        return expectedAnnotation;
    }
}
