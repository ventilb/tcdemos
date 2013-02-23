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

package de.iew.sketchpad.domain;

import de.iew.framework.domain.AbstractModel;
import de.iew.framework.domain.Order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementiert das Domainmodell für eine Farbe im RGB-Farbraum.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 11.11.12 - 13:12
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "colorType", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "de.iew.sketchpad.domain.RgbColor")
@Table(name = "sketch_color")
public class RgbColor extends AbstractModel implements Order, Serializable {

    private int ordinalNumber;

    private int r;
    private int g;
    private int b;

    private List<SketchPad> sketchPads = new ArrayList<SketchPad>();

    @Column
    public int getOrdinalNumber() {
        return this.ordinalNumber;
    }

    public void setOrdinalNumber(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    @Column
    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    @Column
    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    @Column
    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    // Brauchen wir für einige Hibernate Abfragen
    @ManyToMany(mappedBy = "colors", fetch = FetchType.LAZY)
    public List<SketchPad> getSketchPads() {
        return sketchPads;
    }

    public void setSketchPads(List<SketchPad> sketchPads) {
        this.sketchPads = sketchPads;
    }

    @Override
    public String toString() {
        return "RgbColor{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}
