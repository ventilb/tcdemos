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

package de.iew.domain.sketchpad;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementiert das Domainmodell für die Verwaltung einer Liste von
 * Strich-Einstellungen ({@link Stroke}-Modellen).
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://stackoverflow.com/questions/10864049/map-json-array-of-objects-to-requestbody-listt-using-jackson">JSON-Array in Liste serialisieren</a>
 * @since 11.11.12 - 17:48
 */
public class Strokes extends ArrayList<Stroke> {
    public Strokes(int initialCapacity) {
        super(initialCapacity);
    }

    public Strokes() {
    }

    public Strokes(Collection<? extends Stroke> c) {
        super(c);
    }
}