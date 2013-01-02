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
 * @since 11.11.12 - 00:34
 */

define(['jquery', 'core', './strokechooser', './colorchooser', './paint', './shapes'], function ($, core, /* function of StrokeChooser */ StrokeChooser, /* function of ColorChooser */ ColorChooser, /* function of Paint */ Paint, Shapes) {
    function createPolygon(/* Object */ definition, /* function of Paint */ paint, /* function of ColorChooser */ colorChooser, /* function of StrokeChooser */ strokeChooser) {
        var polygon = new Shapes.Polygon();
        var lineColor = colorChooser.getColorById(definition.lineColor.id);
        var stroke = strokeChooser.getStrokeById(definition.stroke.id);

        decoratePolygon(polygon, lineColor, stroke);

        for (var i = 0; i < definition.segmentCount * 2; i += 2) {
            polygon.moveTo(definition.segments[i], definition.segments[i + 1]);
        }
        paint.addShape(polygon);

        return polygon;
    }

    function decoratePolygon(/* Shapes.Polygon */ polygon, /* Color */ lineColor, /* Stroke */ stroke) {
        if (lineColor != null) {
            polygon.lineColor = lineColor.asCssString();
        }
        if (stroke) {
            polygon.lineWidth = stroke.strokeWidth;
        }
    }

    var pollingId = null;

    function startPolling(/* function of Paint */ paint, /* function of ColorChooser */ colorChooser, /* function of StrokeChooser */ strokeChooser) {
        pollingId = setInterval(function () {
            jQuery.ajax({
                url: core.baseUrl('/sketchpad/listpolygons.json'),
                type: 'GET',
                success: function (data) {
                    paint.stopAnimation();
                    paint.removeAllShapes();

                    var polygonCount = data.length;
                    for (var i = 0; i < polygonCount; i++) {
                        createPolygon(data[i], paint, colorChooser, strokeChooser);
                    }

                    paint.animate(1000);
                }
            });
        }, 1000);
    }

    function stopPolling() {
        clearInterval(pollingId);
    }

    return function (/* String */ containerId) {
            var sketchPadContainer = $(containerId + ' canvas');

            var sketchPadCanvas = sketchPadContainer.get(0);
            var sketchPadContext2d = sketchPadCanvas.getContext('2d');

            if (sketchPadContext2d) {
                var paint = new Paint(sketchPadContext2d, sketchPadCanvas.width, sketchPadCanvas.height);
                paint.clearPaintOnDraw = true;

                var colorChooser = new ColorChooser($(containerId + ' .color_chooser'));
                var strokeChooser = new StrokeChooser($(containerId + ' .stroke_chooser'));

                startPolling(paint, colorChooser, strokeChooser);

                var paintPolygon = null;
                sketchPadContainer.mousedown(function (evt) {
                    stopPolling();
                    $(document).get(0).onselectstart = function () {
                        return false;
                    }

                    var sketchPadContainerPageOffset = sketchPadContainer.offset(); // Position auf der Seite für Mausberechnung
                    var mx = (evt.pageX - sketchPadContainerPageOffset.left);
                    var my = (evt.pageY - sketchPadContainerPageOffset.top);
                    var lineColor = colorChooser.selectedColor;
                    var stroke = strokeChooser.selectedStroke;

                    $.ajax({
                        url: core.baseUrl('/sketchpad/newpolygon.json'),
                        type: 'POST',
                        data: {
                            x: mx,
                            y: my,
                            lineColorId: lineColor.id,
                            strokeId: stroke.id
                        },
                        success: function (data) {
                            /*
                             Erstelle und konfiguriere Polygon hier lokal um
                             Race-Conditions zu vermeiden, da die Webservice Aufrufe
                             asynchron sind.
                             */
                            var polygon = new Shapes.Polygon();
                            polygon.id = data;

                            decoratePolygon(polygon, lineColor, stroke);

                            polygon.moveTo(mx, my);

                            paintPolygon = polygon
                            paint.addShape(polygon);

                            paint.stopAnimation();
                            paint.animate(50);
                        }
                    });

                });

                sketchPadContainer.mouseup(function (evt) {
                    /*
                     Kopiere das Polygon erst in den lokalen Kontext um Race-Conditions
                     zu vermeiden, da die Webservice Aufrufe asynchron sind.
                     */
                    var polygon = paintPolygon;
                    paintPolygon = null;

                    $(document).get(0).onselectstart = function () {
                        return true;
                    }

                    var sketchPadContainerPageOffset = sketchPadContainer.offset(); // Position auf der Seite für Mausberechnung
                    var mx = (evt.pageX - sketchPadContainerPageOffset.left);
                    var my = (evt.pageY - sketchPadContainerPageOffset.top);

                    if (polygon != null) {
                        $.ajax({
                            url: core.baseUrl('/sketchpad/closepolygon.json'),
                            type: 'POST',
                            data: {
                                polygonId: polygon.id,
                                x: mx,
                                y: my
                            },
                            success: function (data) {
                                polygon.moveTo(mx, my);

                                startPolling(paint, colorChooser, strokeChooser);
                            }
                        });
                    }
                });

                // @TODO Vektorzeit einführen, da die Webservice Aufrufe asynchron sind und wir am Server die Reihenfolge herstellen müssen
                sketchPadContainer.mousemove(function (evt) {
                    /*
                     Kopiere das Polygon erst in den lokalen Kontext um Race-Conditions
                     zu vermeiden, da die Webservice Aufrufe asynchron sind.
                     */
                    var polygon = paintPolygon;

                    var sketchPadContainerPageOffset = sketchPadContainer.offset(); // Position auf der Seite für Mausberechnung
                    var mx = (evt.pageX - sketchPadContainerPageOffset.left);
                    var my = (evt.pageY - sketchPadContainerPageOffset.top);

                    if (polygon != null) {
                        $.ajax({
                            url: core.baseUrl('/sketchpad/addsegment.json'),
                            type: 'POST',
                            data: {
                                polygonId: polygon.id,
                                x: mx,
                                y: my
                            },
                            success: function (data) {
                                polygon.moveTo(mx, my);
                            }
                        });
                    }
                });

            }
        }

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
