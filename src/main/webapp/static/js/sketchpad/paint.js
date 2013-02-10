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
 * Implementiert eine Klasse zum Zeichnen der Grafikobjekte.
 * <p>
 * Arbeitet auf einem {@link ShapeIndex} für die Verwaltung der zu zeichnenden Objekte.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 11.11.2012 - 00:42
 */

define(function () {

    return function (/* {CanvasRenderingContext2D} */ context, /* {ShapeIndex} */ shapeIndex, /* number */ width, /* number */ height) {
        var paintScope = this;

        /*
         Prüfe ob requestAnimationFrame unterstützt wird. Wenn nicht, dann greifen wir auf den alten setIntervall
         Mechanismus zurück.

         @see https://developer.mozilla.org/en-US/docs/DOM/window.requestAnimationFrame
         */
        var requestAnimationFrame = window.requestAnimationFrame ||
            window.mozRequestAnimationFrame ||
            window.webkitRequestAnimationFrame ||
            window.msRequestAnimationFrame;

        this.supportsRequestAnimationFrame = !!requestAnimationFrame;

        if (this.supportsRequestAnimationFrame) {
            window.requestAnimationFrame = requestAnimationFrame;
        }

        var animator = null;
        var animating = false;

        this.clearPaintOnDraw = false;
        this.paintShape = null;

        /**
         * Die aktuellen Koordinaten der linken oberen Ecke des Canvas. Wir benötigen sie nachdem das Canvas verschoben
         * wurde um die Positionen der Zeichenopjekte zu berechnen.
         *
         * @type {number}
         */
        this.canvasX = 0;
        this.canvasY = 0;

        this.setPaintShape = function (/* Shapes.Shape */ paintShape) {
            this.paintShape = paintShape;
        }

        this.getPaintShape = function () {
            return this.paintShape;
        }

        this.pickShapeAt = function (/* Integer */ x, /* Integer */ y) {
            var shape;
            var shapes = shapeIndex.list();
            for (var shapeNum in shapes) {
                shape = shapes[shapeNum];
                if (shape.pickable && shape.contains(x, y)) {
                    return shape;
                }
            }
            return null;
        }

        this.draw = function () {
            if (paintScope.clearPaintOnDraw) {
                paintScope.clearPaint();
            }

            var shape;
            var shapes = shapeIndex.list();
            for (var i = 0; i < shapes.length; i++) {
                shape = shapes[i];

                if (shape.dirty || paintScope.clearPaintOnDraw) {
                    shape.draw(context);
                }
            }

            if (paintScope.paintShape != null) {
                paintScope.paintShape.draw(context);
            }

            if (animating && paintScope.supportsRequestAnimationFrame) {
                requestAnimationFrame(paintScope.draw);
            }
        }

        this.translate = function (x, y) {
            this.canvasX += x;
            this.canvasY += y;
            context.translate(x, y);
        }

        this.clearPaint = function () {
            context.save();
            context.setTransform(1, 0, 0, 1, 0, 0);
            this.clearArea(0, 0, width, height);
            context.restore();
        }

        this.clearArea = function (x, y, w, h) {
            context.clearRect(x, y, w, h);
        }

        this.animate = function () {
            if (animating) {
                return;
            }
            animating = true;

            var paint = this;
            paint.draw();

            if (!this.supportsRequestAnimationFrame) {
                console.log('Browser does not support requestAnimationFrame. Falling back to setIntervall().');
                animator = setInterval(function () {
                    paint.draw();
                }, 1000 / 60);
            }
        }

        this.stopAnimation = function () {
            if (animating) {
                if (!this.supportsRequestAnimationFrame) {
                    clearInterval(animator);
                }
                animating = false;
            }
        }
    }

});
