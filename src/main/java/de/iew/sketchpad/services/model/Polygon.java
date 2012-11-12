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

package de.iew.sketchpad.services.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Implementiert das Domainmodell für ein Polygon.
 * <p>
 * Ein Polygon besteht aus einer Punktliste, die durch Linien miteinander
 * verbunden werden.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 10.11.12 - 23:27
 */
public class Polygon implements Serializable {

    private Long id;

    private double[] segments = new double[100];

    private int segmentCount = 0;

    private RgbColor lineColor;

    private Stroke stroke;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RgbColor getLineColor() {
        return lineColor;
    }

    public void setLineColor(RgbColor lineColor) {
        this.lineColor = lineColor;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public synchronized boolean addSegment(double x, double y) {

        int arrayIndex = this.segmentCount * 2;

        if (this.segmentCount > 0) {
            double lastx = this.segments[arrayIndex - 2];
            double lasty = this.segments[arrayIndex - 1];
            if (x == lastx && y == lasty) {
                return false;
            }
        }

        resize();
        this.segments[arrayIndex] = x;
        this.segments[arrayIndex + 1] = y;
        this.segmentCount++;
        return true;
    }

    public synchronized int getSegmentCount() {
        return this.segmentCount;
    }

    public double[] getSegments() {
        return this.segments;
    }

    protected void resize() {
        if (this.segmentCount == this.segments.length / 2) {
            this.segments = Arrays.copyOf(this.segments, this.segments.length + 100);
        }
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "segmentCount=" + segmentCount +
                '}';
    }
}
