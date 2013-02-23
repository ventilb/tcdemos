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

package de.iew.sketchpad.persistence;

import de.iew.framework.persistence.DomainModelDao;
import de.iew.sketchpad.domain.RgbColor;

import java.util.List;

/**
 * Beschreibt die Schnittstelle für den Zugriff auf die Farben des Sketch Pad
 * Moduls.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 16:03
 */
public interface RgbColorDao extends DomainModelDao<RgbColor> {

    /**
     * Liefert die konfigurierten Farben für das angegebene Sketch Pad.
     *
     * @param sketchPadId Die Id des Sketch Pads.
     * @return Liste der Farben.
     */
    public List<RgbColor> listColorsBySketchPadId(long sketchPadId);

    /**
     * Liefert die konfigurierte Farbe mit der angegebenen Id für das
     * angegebene Sketch Pad.
     *
     * @param sketchPadId Die Id des Sketch Pads.
     * @param colorId     Die Id der Farbe.
     * @return Die Farbe oder NULL wenn nicht gefunden.
     */
    public RgbColor getColorBySketchPadIdAndId(long sketchPadId, long colorId);
}
