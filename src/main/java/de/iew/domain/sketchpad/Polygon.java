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

package de.iew.domain.sketchpad;

import de.iew.domain.DataSource;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@Entity
@Table(name = "sketch_polygon")
@PrimaryKeyJoinColumn(name = "id")
public class Polygon extends DataSource implements Serializable {

    private SketchPad sketchPad;

    private RgbColor lineColor;

    private Stroke stroke;

    private List<Segment> segments = new ArrayList<Segment>();

    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sketch_pad_id")
    public SketchPad getSketchPad() {
        return sketchPad;
    }

    public void setSketchPad(SketchPad sketchPad) {
        this.sketchPad = sketchPad;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_color_id")
    public RgbColor getLineColor() {
        return lineColor;
    }

    public void setLineColor(RgbColor lineColor) {
        this.lineColor = lineColor;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stroke_id")
    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "polygon", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    @Column
    @Enumerated(value = EnumType.STRING)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public static enum State {
        OPEN,
        CLOSED
    }
}
