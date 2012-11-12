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

package de.iew.demos.model;

/**
 * Implementiert ein einfaches Modell für die Verwaltung eines Rahmens, der
 * aus zwei Punkten aufgespannt wird.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 10.11.12 - 21:40
 */
public class ImageFrame {
    private int px1;

    private int py1;

    private int px2;

    private int py2;

    public int getPx1() {
        return px1;
    }

    public void setPx1(int px1) {
        this.px1 = px1;
    }

    public int getPy1() {
        return py1;
    }

    public void setPy1(int py1) {
        this.py1 = py1;
    }

    public int getPx2() {
        return px2;
    }

    public void setPx2(int px2) {
        this.px2 = px2;
    }

    public int getPy2() {
        return py2;
    }

    public void setPy2(int py2) {
        this.py2 = py2;
    }
}
