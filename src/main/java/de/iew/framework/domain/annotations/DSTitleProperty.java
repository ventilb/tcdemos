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

package de.iew.framework.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation um eine Methode für die Titeleigenschaft zu deklarieren.
 * <p>
 * Einige Module benötigen für die Anzeige einen Titel. Mit dieser Annotation
 * kann eine Methode für die Rückgabe eines Titels markiert werden. Eine so
 * deklarierte Methode muss einen Wert zurückgeben.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 11:27
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) // Wichtig, damit die Annotation zur Laufzeit ausgewertet werden kann
public @interface DSTitleProperty {
}
