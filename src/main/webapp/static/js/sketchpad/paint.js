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

/**
 * Implementiert eine Klasse für die Verwaltung der Grafikobjekte und zum
 * Zeichnen der Grafikobjekte.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 11.11.12 - 00:42
 */

define(function () {

    return function (context, width, height) {
        var animator = null;
        var animating = false;

        this.context = context;
        this.clearPaintOnDraw = false;
        this.shapes = new Array();

        this.addShape = function (/* function of Shapes.Shape */ shape) {
            shape.context = this.context;
            this.shapes.push(shape);
        }

        this.removeAllShapes = function () {
            this.shapes.length = 0;
        }

        this.pickShapeAt = function (/* Integer */ x, /* Integer */ y) {
            var shape = null;
            var length = this.shapes.length;
            for (var i = length - 1; i >= 0; i--) {
                shape = this.shapes[i];
                if (shape.pickable && shape.contains(x, y)) {
                    return shape;
                }
            }
            return null;
        }

        this.draw = function () {
            if (this.clearPaintOnDraw) {
                this.clearPaint();
            }

            var shape = null;
            var shapeCount = this.shapes.length;
            for (var i = 0; i < shapeCount; i++) {
                shape = this.shapes[i];
                if (shape.dirty || this.clearPaintOnDraw) {
                    shape.draw();
                }
            }
        }

        this.clearPaint = function () {
            this.clearArea(0, 0, width, height);
        }

        this.clearArea = function (x, y, w, h) {
            this.context.clearRect(x, y, w, h);
        }

        this.animate = function (/* Integer */ refresh) {
            if (animating) {
                return;
            }
            var paint = this;

            paint.draw();
            animator = setInterval(function () {
                paint.draw();
            }, refresh);
            animating = true;
        }

        this.stopAnimation = function () {
            if (animating) {
                clearInterval(animator);
                animating = false;
            }
        }
    }

});
