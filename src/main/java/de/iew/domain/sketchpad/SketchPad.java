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

package de.iew.domain.sketchpad;

import de.iew.domain.DataSource;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementiert das Domainmodell für ein Zeichenbrett.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 14:44
 */
@Entity
@Table(name = "sketch_sketch_pad")
@PrimaryKeyJoinColumn(name = "id")
public class SketchPad extends DataSource implements Serializable {

    private Date created;

    private State state;

    private List<RgbColor> colors = new ArrayList<RgbColor>();

    private List<Stroke> strokes = new ArrayList<Stroke>();

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "sketch_sketch_pad_color", joinColumns = {@JoinColumn(name = "sketch_pad_id")}, inverseJoinColumns = {@JoinColumn(name = "color_id")})
    public List<RgbColor> getColors() {
        return colors;
    }

    public void setColors(List<RgbColor> colors) {
        this.colors = colors;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "sketch_sketch_pad_stroke", joinColumns = {@JoinColumn(name = "sketch_pad_id")}, inverseJoinColumns = {@JoinColumn(name = "stroke_id")})
    public List<Stroke> getStrokes() {
        return strokes;
    }

    public void setStrokes(List<Stroke> strokes) {
        this.strokes = strokes;
    }

    public static enum State {
        INACTIVE,
        ACTIVE,
        FROZEN
    }
}
