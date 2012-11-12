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

package de.iew.sketchpad.services;

import de.iew.sketchpad.services.model.*;

/**
 * Beschreibt die Schnittstelle für den Sketchpad-Dienst.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 11.11.12 - 10:27
 */
public interface SketchPadService {

    public Polygons listAllPolygons();

    public Polygon createPolygon(String sessionId, double x, double y, long lineColorId, long strokeId);

    public boolean extendPolygon(String sessionId, long polygonId, double x, double y);

    public boolean closePolygon(String sessionId, long polygonId, double x, double y);

    public Colors listAllColors();

    public RgbColor chooseColor(String sessionId, long colorId);

    public Strokes listAllStrokes();

    public Stroke chooseStroke(String sessionId, long strokeId);

    /**
     * Liefert das Polygon mit der angegebenen Id.
     *
     * @param polygonId Die Id des Polygons.
     * @return Das Polygon.
     * @throws java.util.NoSuchElementException
     *          Wenn das angeforderte Polygon nicht vorhanden ist.
     */
    public Polygon getPolygonById(long polygonId);
}
