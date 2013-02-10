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
 * Implementiert die Javascript-seitigen Funktionen für das Sketchpad.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 11.11.2012 - 00:34
 */

define(['jquery', 'core', './strokechooser', './colorchooser', './paint', './shapes', './shapeindex'], function ($, core, /* {StrokeChooser} */ StrokeChooser, /* {ColorChooser} */ ColorChooser, /* {Paint} */ Paint, /* {Shapes} */ Shapes, /* {ShapeIndex} */ ShapeIndex) {

    function SketchPad(/* String */ containerId, /* number */ sketchPadId) {
        var sketchPadScope = this;

        // HTML Objekte
        this.sketchPadContainer = $(containerId + ' canvas');
        this.sketchPadCanvas = this.sketchPadContainer.get(0);
        this.sketchPadContext2d = this.sketchPadCanvas.getContext('2d');

        // Objekteigenschaften, Ids und Zustände
        this.sketchPadId = sketchPadId;
        this.pollingId = null;
        this.lastInsertedPolygonId = null;
        /**
         * Die Funktion des Zeichenbretts. PAINT = Zeichnen, PAN = Verschieben
         *
         * @type {String}
         */
        this.operationMode = null;

        this.mousePanningX = -1;
        this.mousePanningY = -1;

        var operationTimerId = null;
        var operationFirstMove = false;

        // Zusätzliche Zeichentools
        this.shapeIndex = new ShapeIndex();
        this.colorChooser = new ColorChooser($(containerId + ' .color_chooser'), this.sketchPadId);
        this.strokeChooser = new StrokeChooser($(containerId + ' .stroke_chooser'), this.sketchPadId);

        if (this.sketchPadContext2d) {
            this.paint = new Paint(this.sketchPadContext2d, this.shapeIndex, this.sketchPadCanvas.width, this.sketchPadCanvas.height);
            this.paint.clearPaintOnDraw = true;
            this.paint.animate();

            sketchPadScope.startPolling();

            $(document).keydown(function (evt) {
                sketchPadScope.paint.clearPaint();
                switch (evt.keyCode) {
                    case 37:
                        // Links
                        sketchPadScope.paint.translate(-50, 0);
                        break;
                    case 38:
                        // Hoch
                        sketchPadScope.paint.translate(0, -50);
                        break;
                    case 39:
                        // Rechts
                        sketchPadScope.paint.translate(50, 0);
                        break;
                    case 40:
                        // Unten
                        sketchPadScope.paint.translate(0, 50);
                        break;
                }
            });

            $(this.sketchPadContainer).bind('mousedown', function (evt) {
                sketchPadScope.operationMode = 'PAINT';
                sketchPadScope.mousePanningX = -1;
                sketchPadScope.mousePanningY = -1;

                operationFirstMove = true;
                operationTimerId = setTimeout(function () {
                    sketchPadScope.operationMode = 'PAN';
                    sketchPadScope.setSketchPadCursor('move');
                }, 1000);

            })

            $(this.sketchPadContainer).bind('mousemove', function (evt) {
                clearTimeout(operationTimerId);

                /*
                 Damit wir in Chrome eigene Mauscursor beim Draggen und Zeichnen haben

                 @see http://stackoverflow.com/questions/11106955/change-cursor-over-html5-canvas-when-dragging-in-chrome
                 */
                $(document).get(0).onselectstart = function () {
                    return false;
                }

                switch (sketchPadScope.operationMode) {
                    case 'PAINT':
                        if (operationFirstMove) {
                            sketchPadScope.mouseCreatePolygon(evt);
                            operationFirstMove = false;
                        } else {
                            sketchPadScope.mouseMovePolygon(evt)
                        }
                        break;
                    case 'PAN':
                        sketchPadScope.mousePanCanvas(evt);
                        break;
                }
            });

            $(this.sketchPadContainer).bind('mouseup', function (evt) {
                clearTimeout(operationTimerId);

                switch (sketchPadScope.operationMode) {
                    case 'PAINT':
                        sketchPadScope.mouseClosePolygon(evt);
                        break;
                }

                sketchPadScope.operationMode = null;
                sketchPadScope.setSketchPadCursor('auto');
            });
        }
    }

    SketchPad.prototype.mouseCreatePolygon = function (evt) {
        this.stopPolling();
        $(document).get(0).onselectstart = function () {
            return false;
        }

        var sketchPadContainerPageOffset = this.sketchPadContainer.offset(); // Position auf der Seite für Mausberechnung
        var mx = (evt.pageX - sketchPadContainerPageOffset.left) - this.paint.canvasX;
        var my = (evt.pageY - sketchPadContainerPageOffset.top) - this.paint.canvasY;
        var lineColor = this.colorChooser.selectedColor;
        var stroke = this.strokeChooser.selectedStroke;

        var polygon = new Shapes.Polygon();
        polygon.dirty = true;

        this.decoratePolygon(polygon, lineColor, stroke);

        polygon.moveTo(mx, my);

        this.paint.setPaintShape(polygon);
    }

    SketchPad.prototype.mouseMovePolygon = function (evt) {
        var polygon = this.paint.getPaintShape();

        if (polygon != null) {
            var sketchPadContainerPageOffset = this.sketchPadContainer.offset(); // Position auf der Seite für Mausberechnung
            var mx = (evt.pageX - sketchPadContainerPageOffset.left) - this.paint.canvasX;
            var my = (evt.pageY - sketchPadContainerPageOffset.top) - this.paint.canvasY;

            polygon.moveTo(mx, my);
        }
    }

    SketchPad.prototype.mouseClosePolygon = function (evt) {
        var polygon = this.paint.getPaintShape();

        if (polygon != null) {
            var sketchPadContainerPageOffset = this.sketchPadContainer.offset(); // Position auf der Seite für Mausberechnung
            var mx = (evt.pageX - sketchPadContainerPageOffset.left) - this.paint.canvasX;
            var my = (evt.pageY - sketchPadContainerPageOffset.top) - this.paint.canvasY;
            var lineColor = this.colorChooser.selectedColor;
            var stroke = this.strokeChooser.selectedStroke;

            polygon.moveTo(mx, my);

            var sketchPadScope = this;
            $.ajax({
                url: core.baseUrl('/sketchpad/newpolygon.json'),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                type: 'POST',
                data: JSON.stringify({
                    sketchPadId: sketchPadScope.sketchPadId,
                    lineColorId: lineColor.id,
                    strokeId: stroke.id,
                    segments: polygon.getSegments()
                }),
                success: function (data) {
                    if (data) {
                        polygon.id = data;

                        sketchPadScope.shapeIndex.put(polygon);

                        sketchPadScope.startPolling();
                    }
                    sketchPadScope.paint.setPaintShape(null);
                }
            });
        }
    }

    SketchPad.prototype.mousePanCanvas = function (evt) {
        var sketchPadContainerPageOffset = this.sketchPadContainer.offset();
        var mx = (evt.pageX - sketchPadContainerPageOffset.left);
        var my = (evt.pageY - sketchPadContainerPageOffset.top);

        var deltaToPanX = 0;
        var deltaToPanY = 0;

        var doPan = this.mousePanningX >= 0 && this.mousePanningY >= 0;

        if (this.mousePanningX >= 0) {
            deltaToPanX = mx - this.mousePanningX;
        }
        if (this.mousePanningY >= 0) {
            deltaToPanY = my - this.mousePanningY;
        }

        if (doPan) {
            this.paint.translate(deltaToPanX, deltaToPanY);
        }

        this.mousePanningX = mx;
        this.mousePanningY = my;
    }

    SketchPad.prototype.setSketchPadCursor = function (/* String */ cursor) {
        $(this.sketchPadContainer).css('cursor', cursor);
    }

    SketchPad.prototype.createPolygon = function (/* Object */ polygonForm) {
        var polygon = new Shapes.Polygon();
        polygon.id = polygonForm.polygonId;
        polygon.segments = polygonForm.segments;
        polygon.dirty = true;

        var lineColor = this.colorChooser.getColorById(polygonForm.lineColorId);
        var stroke = this.strokeChooser.getStrokeById(polygonForm.strokeId);

        this.decoratePolygon(polygon, lineColor, stroke);

        return polygon;
    }

    SketchPad.prototype.decoratePolygon = function (/* {Shapes.Polygon} */ polygon, /* {Color} */ lineColor, /* {Stroke} */ stroke) {
        if (lineColor) {
            polygon.lineColor = lineColor.asCssString();
        }
        if (stroke) {
            polygon.lineWidth = stroke.strokeWidth;
        }
    }

    SketchPad.prototype.startPolling = function () {
        var sketchPadScope = this;

        this.pollingId = setInterval(function () {

            jQuery.ajax({
                url: core.baseUrl('/sketchpad/listpolygons.json'),
                type: 'GET',
                data: {
                    sketchPadId: sketchPadScope.sketchPadId,
                    lastInsertedShapeId: sketchPadScope.lastInsertedPolygonId
                },
                success: function (data) {
                    var polygonCount = data.length;
                    for (var i = 0; i < polygonCount; i++) {
                        var polygon = sketchPadScope.createPolygon(data[i]);
                        sketchPadScope.shapeIndex.put(polygon);

                        sketchPadScope.lastInsertedPolygonId = Math.max(sketchPadScope.lastInsertedPolygonId, polygon.id);
                    }
                }
            });
        }, 1000);
    }

    SketchPad.prototype.stopPolling = function () {
        clearInterval(this.pollingId);
    }

    return SketchPad;

// Deprecated /////////////////////////////////////////////////////////////////
    function createColorString(/* Array */ colorComponents) {
        var componentCount = colorComponents.length;

        var color = '';
        if (componentCount == 4) {
            color += 'rgba(';
        } else if (componentCount == 3) {
            color += 'rgb(';
        } else {
            throw new Error('Illegal color specification');
        }
        color += colorComponents.join(',');

        return color + ')';
    }

    function choseColor(/* String */ color) {
        // Das Farbobjekt, das wir am Ende an den Server senden wollen
        var colorObject = {};
        var url = null;

        // Ein wenig Pattern parsen
        var pattern = null;
        if (color.indexOf('rgba') == 0) {
            pattern = /rgba\((.+)\)/;
            url = '/sketchpad/setrgbacolor.json';
        } else if (color.indexOf('rgb') == 0) {
            pattern = /rgb\((.+)\)/;
            url = '/sketchpad/setrgbcolor.json';
        } else {
            throw new Error('Illegal color specification');
        }

        if (!pattern.test(color)) {
            throw new Error('Illegal color specification');
        }

        pattern.exec(color);

        var components = (RegExp.$1).split(',');
        switch (components.length) {
            case 4:
                colorObject.a = +components[3];
            case 3:
                colorObject.r = +components[0];
                colorObject.g = +components[1];
                colorObject.b = +components[2];
                break;
            default:
                throw new Error('Illegal color specification');
        }

        // Und ab damit zum Server
        jQuery.ajax({
            url: core.baseUrl(url),
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(colorObject),
            success: function (data) {
                console.log(data);
            }
        });
    }

});
