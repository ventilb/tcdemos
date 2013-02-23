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
import de.iew.sketchpad.domain.Polygon;

import java.util.List;

/**
 * Beschreibt die Schnittstelle für den Zugriff auf
 * {@link de.iew.sketchpad.domain.Polygon}-Domainmodelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 17:45
 */
public interface PolygonDao extends DomainModelDao<Polygon> {

    /**
     * Liefert die Anzahl der Segment des angegebenen Polygons.
     *
     * @param polygonId Die Id des Polygons.
     * @return Die Anzahl der Segmente des Polygons.
     */
    public long getSegmentCount(long polygonId);

    /**
     * Listet die Polygone des angegebenen Sketch Pads.
     *
     * @param sketchPadId Die Id des Sketch Pads.
     * @return Liste der Polygone.
     */
    public List<Polygon> listPolygons(long sketchPadId);

    /**
     * Returns all {@link Polygon} models from the given sketch pad starting with the given id.
     * <p>
     * The polygon with the given id is not included in the result.
     * </p>
     *
     * @param sketchPadId   the sketch pad id
     * @param fromPolygonId the polygon id to start from
     * @return list of polygons matching the criteria
     */
    public List<Polygon> listPolygonsFrom(long sketchPadId, long fromPolygonId);
}
