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

import de.iew.framework.domain.DomainModel;
import de.iew.framework.domain.annotations.DSTitleProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Stellt Hilfsmethoden für den Umgang mit den {@link de.iew.framework.domain.DataSource}
 * Annotationen bereit.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 11:36
 */
public class DSAnnotationsIntrospector {

    /**
     * Liefert eine mit {@link DSTitleProperty} annotierte Methode zurück oder
     * NULL wenn keine solche Methode existiert.
     *
     * @param domainModel Das Domainmodell.
     * @return Mit {@link DSTitleProperty} annotierte Methode.
     */
    public static Method determineTitlePropertyMethod(DomainModel domainModel) {
        Annotation a;
        for (Method method : domainModel.getClass().getMethods()) {
            a = method.getAnnotation(DSTitleProperty.class);
            if (a != null) {
                return method;
            }
        }
        return null;
    }

    /**
     * Liefert den Wert einer mit {@link DSTitleProperty} annotierten Methode.
     * <p>
     * Meldet eine Exception, wenn beim Abfragen des Wertes ein unerwarteter
     * Fehler auftritt oder der Wert aus einem anderen grund nicht abgefragt
     * werden kann.
     * </p>
     *
     * @param m      Mit {@link DSTitleProperty} annotierte Methode.
     * @param target Das Zielobjekt.
     * @return Der Wert.
     * @throws Exception Wenn der Wert nicht abgefragt werden kann.
     */
    public static Object getTitlePropertyValue(Method m, DomainModel target) throws Exception {
        Annotation a = m.getAnnotation(DSTitleProperty.class);

        if (a != null) {
            return m.invoke(target);
        }

        throw new NotAnnotatedException(DSTitleProperty.class);
    }

}
