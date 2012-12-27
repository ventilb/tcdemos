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
 * Beschreibt eine Schnittstelle für eine Familie von Klassen zum auflösen
 * von Objekten zu Strings.
 * <p>
 * Wird beispielsweise verwendet um den Knotentitel für Bäume aus der
 * {@link de.iew.domain.DataSource} des Knotens zu ermitteln.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 12:02
 */
public interface StringResolver {

    /**
     * Löst aus dem angegebenen Objekt einen String heraus.
     *
     * @param resolveStringObject Das aufzulösende Objekt.
     * @return Der aufgelöste String.
     */
    public String resolveString(Object resolveStringObject);

}
