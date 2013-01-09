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

package de.iew.demos.model;

import de.iew.domain.sketchpad.Polygon;
import de.iew.domain.sketchpad.RgbColor;
import de.iew.domain.sketchpad.Segment;
import de.iew.domain.sketchpad.Stroke;
import de.iew.domain.utils.AbstractDomainModelVisitor;
import de.iew.persistence.hibernate.HbmUtils;

import java.util.List;

/**
 * Wandelt {@link Polygon}-Domainmodelle in {@link PolygonModel}-DTOs um.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 03.01.13 - 00:22
 */
public class PolygonToPolygonModelTransformer extends AbstractDomainModelVisitor<Polygon, PolygonModel> {

    private StrokeToStrokeModelTransformer strokeModelTransformer = new StrokeToStrokeModelTransformer();

    private ColorToColorModelTransformer colorModelTransformer = new ColorToColorModelTransformer();

    public PolygonModel visit(Polygon domainModel) {
        PolygonModel polygonModel = new PolygonModel();
        polygonModel.setId(domainModel.getId());
        polygonModel.setLineColor(this.colorModelTransformer.visit((RgbColor) HbmUtils.fromProxy(domainModel.getLineColor())));
        polygonModel.setSketchPadId(domainModel.getSketchPad().getId());
        polygonModel.setStroke(this.strokeModelTransformer.visit((Stroke) HbmUtils.fromProxy(domainModel.getStroke())));

        initSegments(polygonModel, domainModel);

        return polygonModel;
    }

    protected void initSegments(PolygonModel polygonModel, Polygon polygon) {
        List<Segment> segments = polygon.getSegments();

        double[] segs = new double[segments.size() * 2];

        int i = 0;
        for (Segment segment : segments) {
            segs[i] = segment.getX();
            segs[i + 1] = segment.getY();
            i += 2;
        }

        polygonModel.setSegmentCount(segments.size());
        polygonModel.setSegments(segs);
    }

}
