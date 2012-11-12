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
 * Implementiert ein paar einfache Javascript Funktionen zum Zeichnen auf
 * Canvas-Objekten.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 10.11.12 - 16:59
 */

$(document).ready(function () {
    var element = $('#photo_editor canvas');
    var offset = element.offset();

    var photoCanvas = element.get(0);
    var photoContext = photoCanvas.getContext('2d');

    if (photoContext) {
        var paint = new Paint(photoContext, photoCanvas.width, photoCanvas.height);
        paint.clearPaintOnDraw = true;

        var topLeftRect = new Rect(0, 0, 20, 20);
        var bottomRightRect = new Rect(0, 0, 20, 20);

        buildImageFrame(paint, topLeftRect, bottomRightRect);

        paint.animate(1000);

        var dragShape = null;
        var dx = 0;
        var dy = 0;
        element.mousedown(function (evt) {
            $(document).get(0).onselectstart = function () {
                return false;
            }

            var mx = (evt.pageX - offset.left);
            var my = (evt.pageY - offset.top);

            dragShape = paint.pickShapeAt(mx, my);
            if (dragShape != null) {
                dx = mx - dragShape.rx;
                dy = my - dragShape.ry;

                paint.stopAnimation();
                paint.animate(50);
            }
        });

        element.mouseup(function (evt) {
            $(document).get(0).onselectstart = function () {
                return true;
            }

            var mx = (evt.pageX - offset.left);
            var my = (evt.pageY - offset.top);

            if (dragShape != null) {
                dragShape.moveTo(mx - dx, my - dy);

                paint.stopAnimation();
                paint.animate(1000);
            }
            dragShape = null;
        });

        element.mousemove(function (evt) {
            var mx = (evt.pageX - offset.left);
            var my = (evt.pageY - offset.top);

            if (dragShape != null) {
                switch (dragShape.getName()) {
                    case 'Rect':
                        jQuery.ajax({
                            url: baseUrl('/photo/setimageframe.json'),
                            type: 'POST',
                            contentType: 'application/json',
                            data: JSON.stringify({
                                px1: topLeftRect.rx,
                                py1: topLeftRect.ry,
                                px2: bottomRightRect.rx,
                                py2: bottomRightRect.ry
                            }),
                            success: function (data) {
                                dragShape.moveTo(mx - dx, my - dy);
                            }
                        });
                        break;
                }
            }
        });

    }
});

function buildImageFrame(/* Paint */ paint, /* Rect */ topLeftRect, /* Rect */ bottomRightRect) {
    jQuery.ajax({
        url: baseUrl('/photo/getimageframe.json'),
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            topLeftRect.moveTo(data.px1, data.py1);
            bottomRightRect.moveTo(data.px2, data.py2);

            var rect3 = new Rect(topLeftRect.cx, topLeftRect.cy, bottomRightRect.cx - topLeftRect.cx, bottomRightRect.cy - topLeftRect.cy);
            rect3.pickable = false;

            topLeftRect.moved = function () {
                rect3.moveTopLeftTo(this.cx, this.cy);
            }
            bottomRightRect.moved = function () {
                rect3.moveBottomRightTo(this.cx, this.cy);
            }

            paint.addShape(rect3);
            paint.addShape(topLeftRect);
            paint.addShape(bottomRightRect);
        }
    });

}
