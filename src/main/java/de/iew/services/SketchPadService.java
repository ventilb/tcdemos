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

package de.iew.services;

import de.iew.domain.ModelNotFoundException;
import de.iew.domain.sketchpad.Polygon;
import de.iew.domain.sketchpad.RgbColor;
import de.iew.domain.sketchpad.SketchPad;
import de.iew.domain.sketchpad.Stroke;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Beschreibt die Schnittstelle für den Sketchpad-Dienst.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 11.11.12 - 10:27
 */
public interface SketchPadService {

    public SketchPad getActiveSketchPad() throws ModelNotFoundException;

    public List<Polygon> listAllPolygons(long sketchPadId);

    public Polygon createPolygon(Authentication sketchPadUser, long sketchPadId, double x, double y, long lineColorId, long strokeId) throws ModelNotFoundException;

    public boolean extendPolygon(Authentication sketchPadUser, long polygonId, double x, double y) throws ModelNotFoundException;

    public boolean closePolygon(Authentication sketchPadUser, long polygonId, double x, double y) throws ModelNotFoundException;

    public List<RgbColor> listAllColors(long sketchPadId);

    public RgbColor chooseColor(Authentication sketchPadUser, long sketchPadId, long colorId) throws ModelNotFoundException;

    public List<Stroke> listAllStrokes(long sketchPadId);

    public Stroke chooseStroke(Authentication sketchPadUser, long sketchPadId, long strokeId) throws ModelNotFoundException;

}
