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

import de.iew.sketchpad.domain.Polygon;
import de.iew.sketchpad.domain.Segment;
import de.iew.sketchpad.domain.Stroke;
import de.iew.framework.domain.utils.AbstractDomainModelVisitor;
import de.iew.framework.persistence.hibernate.HbmUtils;
import de.iew.sketchpad.domain.RgbColor;

import java.util.List;

/**
 * Wandelt {@link de.iew.sketchpad.domain.Polygon}-Domainmodelle in {@link PolygonForm}-DTOs um.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 03.01.13 - 00:22
 */
public class PolygonToPolygonFormTransformer extends AbstractDomainModelVisitor<Polygon, PolygonForm> {

    public PolygonForm visit(Polygon domainModel) {
        Stroke stroke = (Stroke) HbmUtils.fromProxy(domainModel.getStroke());
        RgbColor color = (RgbColor) HbmUtils.fromProxy(domainModel.getLineColor());

        PolygonForm polygonForm = new PolygonForm();
        polygonForm.setPolygonId(domainModel.getId());
        polygonForm.setLineColorId(color.getId());
        polygonForm.setStrokeId(stroke.getId());

        initSegments(polygonForm, domainModel);

        return polygonForm;
    }

    protected void initSegments(PolygonForm polygonForm, Polygon polygon) {
        List<Segment> segments = polygon.getSegments();

        SegmentForm[] segmentForms = new SegmentForm[segments.size()];

        Segment segment;
        for (int i = 0; i < segments.size(); i++) {
            segment = segments.get(i);
            segmentForms[i] = new SegmentForm(segment.getX(), segment.getY());
        }

        polygonForm.setSegments(segmentForms);
    }

}
