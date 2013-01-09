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

import de.iew.web.dto.WebDTO;

/**
 * Implementiert ein WebDTO für die Übermittlung von Polygondaten.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 03.01.13 - 00:18
 */
public class PolygonModel extends WebDTO {

    private long id;

    private long sketchPadId;

    private RgbColorModel lineColor;

    private StrokeModel stroke;

    private double[] segments;

    private int segmentCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSketchPadId() {
        return sketchPadId;
    }

    public void setSketchPadId(long sketchPadId) {
        this.sketchPadId = sketchPadId;
    }

    public RgbColorModel getLineColor() {
        return lineColor;
    }

    public void setLineColor(RgbColorModel lineColor) {
        this.lineColor = lineColor;
    }

    public StrokeModel getStroke() {
        return stroke;
    }

    public void setStroke(StrokeModel stroke) {
        this.stroke = stroke;
    }

    public double[] getSegments() {
        return segments;
    }

    public void setSegments(double[] segments) {
        this.segments = segments;
    }

    public int getSegmentCount() {
        return segmentCount;
    }

    public void setSegmentCount(int segmentCount) {
        this.segmentCount = segmentCount;
    }
}
