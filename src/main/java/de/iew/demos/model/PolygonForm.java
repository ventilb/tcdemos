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

/**
 * Implements a form to transfer polygon data between web and service layer.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 07.02.2013 - 23:44
 */
public class PolygonForm {
    private long polygonId;

    private long sketchPadId;

    private SegmentForm[] segments;

    private long lineColorId;

    private long strokeId;

    public long getPolygonId() {
        return polygonId;
    }

    public void setPolygonId(long polygonId) {
        this.polygonId = polygonId;
    }

    public long getSketchPadId() {
        return sketchPadId;
    }

    public void setSketchPadId(long sketchPadId) {
        this.sketchPadId = sketchPadId;
    }

    public SegmentForm[] getSegments() {
        return segments;
    }

    public void setSegments(SegmentForm[] segments) {
        this.segments = segments;
    }

    public long getLineColorId() {
        return lineColorId;
    }

    public void setLineColorId(long lineColorId) {
        this.lineColorId = lineColorId;
    }

    public long getStrokeId() {
        return strokeId;
    }

    public void setStrokeId(long strokeId) {
        this.strokeId = strokeId;
    }
}
