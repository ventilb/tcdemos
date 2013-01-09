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

package de.iew.persistence;

import de.iew.domain.sketchpad.SketchPad;

/**
 * Beschreibt die Schnittstelle für den Zurgiff auf die Zeichenflächen.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 17:27
 */
public interface SketchPadDao extends DomainModelDao<SketchPad> {

    /**
     * Liefert die aktive Zeichenfläche.
     *
     * @return Die aktive Zeichenfläche oder NULL wenn nicht gefunden.
     */
    public SketchPad findActive();
}
