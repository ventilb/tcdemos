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

package de.iew.sketchpad.domain;

import de.iew.framework.domain.AbstractModel;
import de.iew.framework.domain.Order;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Implementiert ein Domainmodell für die Verwaltung eines Segments innerhalb
 * eines Polygons.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 20:43
 */
@Entity
@Table(name = "sketch_polygon_segment")
public class Segment extends AbstractModel implements Order, Serializable {

    private int ordinalNumber;

    private double x;

    private double y;

    private Polygon polygon;

    @Column
    public int getOrdinalNumber() {
        return this.ordinalNumber;
    }

    public void setOrdinalNumber(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    @Column
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Column
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "polygon_id", nullable = false)
    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}
