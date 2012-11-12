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

$(document).ready(function () {

    var sketchPadContainer = $('#sketch_pad canvas');

    var sketchPadCanvas = sketchPadContainer.get(0);
    var sketchPadContext2d = sketchPadCanvas.getContext('2d');

    if (sketchPadContext2d) {
        var paint = new Paint(sketchPadContext2d, sketchPadCanvas.width, sketchPadCanvas.height);
        paint.clearPaintOnDraw = true;

        var colorChooser = new ColorChooser($('#color_chooser'));
        var strokeChooser = new StrokeChooser($('#stroke_chooser'));

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

            jQuery.ajax({
                url: baseUrl('/sketchpad/newpolygon.json'),
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
                    var polygon = new Polygon();
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
                jQuery.ajax({
                    url: baseUrl('/sketchpad/closepolygon.json'),
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
                jQuery.ajax({
                    url: baseUrl('/sketchpad/addsegment.json'),
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
});

function createPolygon(/* Object */ definition, /* Paint */ paint, /* ColorChooser */ colorChooser, /* StrokeChooser */ strokeChooser) {
    var polygon = new Polygon();
    var lineColor = colorChooser.getColorById(definition.lineColor.id);
    var stroke = strokeChooser.getStrokeById(definition.stroke.id);

    decoratePolygon(polygon, lineColor, stroke);

    for (var i = 0; i < definition.segmentCount * 2; i += 2) {
        polygon.moveTo(definition.segments[i], definition.segments[i + 1]);
    }
    paint.addShape(polygon);

    return polygon;
}

function decoratePolygon(/* Polygon */ polygon, /* Color */ lineColor, /* Stroke */ stroke) {
    if (lineColor != null) {
        polygon.lineColor = lineColor.asCssString();
    }
    if (stroke) {
        polygon.lineWidth = stroke.strokeWidth;
    }
}

var pollingId = null;

function startPolling(/* Paint */ paint, /* ColorChooser */ colorChooser, /* StrokeChooser */ strokeChooser) {
    pollingId = setInterval(function () {
        jQuery.ajax({
            url: baseUrl('/sketchpad/listpolygons.json'),
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

function ColorChooser(/* HTMLElement */ uiElement) {
    /**
     * Das HTML-Container-Element in dem die Farben abgelegt werden.
     *
     * @type {HTMLElement}
     */
    this.ui = uiElement;

    /**
     * Die ausgewählte Farbe.
     *
     * @type {Color}
     */
    this.selectedColor = null;

    /**
     * Die verwalteten Farben. Bildet die Ids auf die Color Objekte ab.
     *
     * @type {Object}
     */
    this.colors = {};

    jQuery.ajax({
        colorChooserScope: this, // Scopeverweis um im Callback auf den ColorChooser zugreifen zu können

        url: baseUrl('/sketchpad/listcolors.json'),
        type: 'GET',
        success: function (colors) {
            var colorCount = colors.length;

            if (colorCount == 0) {
                throw new Error('Illegal State: No colors from server.');
            }
            var colorChooser = this.colorChooserScope;

            colorChooser.selectedColor = colorChooser.addColor(colors[0]);

            for (var i = 1; i < colorCount; i++) {
                colorChooser.addColor(colors[i]);
            }
        }
    });
}
ColorChooser.prototype.addColor = function (/* Object */ colorComponents) {
    var colorChooserScope = this;
    var li = $('<li class="color_chooser_item"></li>');

    var color = new Color(colorComponents, li);

    li.click(function () {
        colorChooserScope.selectColor(color);
    });

    this.ui.append(li);

    this.colors[color.id] = color;

    return color;
}
ColorChooser.prototype.getColorById = function (/* Integer */ id) {
    var color = null;
    if (id in this.colors) {
        color = this.colors[id];
    }
    return color;
}
ColorChooser.prototype.selectColor = function (/* Color */ color) {
    var colorChooserScope = this;
    jQuery.ajax({
        url: baseUrl('/sketchpad/choosecolor.json'),
        type: 'POST',
        data: {
            colorId: color.id
        },
        success: function (colorFromServer) {
            if (color.id == colorFromServer.id) {
                color.mixin(colorFromServer);
                colorChooserScope.selectedColor = color;
            }
        }
    });
}

function Color(/* Object */ colorComponents, /* HTMLElement|NULL */ uiElement) {
    /**
     * Das HTML-Element zur Darstellung dieser Farbe.
     *
     * @type {HTMLElement|NULL}
     */
    this.ui = uiElement;

    this.mixin(colorComponents);
}
Color.prototype.asCssString = function () {
    var color = this.r + ',' + this.g + ',' + this.b;
    if ('a' in this) {
        color = ('rgba(' + color + ',' + this.a);
    } else {
        color = ('rgb(' + color);
    }

    return color + ')';
}
Color.prototype.mixin = function (/* Object */ colorComponents) {
    delete this['a'];

    this.id = colorComponents.id;

    this.r = colorComponents.r;
    this.g = colorComponents.g;
    this.b = colorComponents.b;

    if ('a' in colorComponents) {
        this.a = colorComponents.a;
    }
    if (this.ui) {
        this.ui.css('background-color', this.asCssString());
    }
}

function StrokeChooser(/* HTMLElement */ uiElement) {
    /**
     * Das HTML-Container-Element in dem die Striche als Thumbnails abgelegt
     * werden.
     *
     * @type {HTMLElement}
     */
    this.ui = uiElement;

    /**
     * Die ausgewählte Stricheinstellung.
     *
     * @type {Stroke}
     */
    this.selectedStroke = null;

    /**
     * Die verwalteten Strich-Einstellungen. Bildet die Ids auf die
     * Stroke-Objekte ab.
     *
     * @type {Object}
     */
    this.strokes = {};

    jQuery.ajax({
        strokeChooserScope: this, // Scopeverweis um im Callback auf den StrokeChooser zugreifen zu können

        url: baseUrl('/sketchpad/liststrokes.json'),
        type: 'GET',
        success: function (strokes) {
            var strokeCount = strokes.length;

            if (strokeCount == 0) {
                throw new Error('Illegal State: No strokes from server.');
            }
            var strokeChooser = this.strokeChooserScope;

            strokeChooser.selectedStroke = strokeChooser.addStroke(strokes[0]);

            for (var i = 1; i < strokeCount; i++) {
                strokeChooser.addStroke(strokes[i]);
            }
        }
    });
}
StrokeChooser.prototype.addStroke = function (/* Object */ strokeComponents) {
    var strokeChooserScope = this;

    // Erstelle Linienmodell und Thumbnail dafür. Wir rendern das Thmumbnail für die Linie als <hr>
    var hr = $('<hr/>');
    var stroke = new Stroke(strokeComponents, hr);

    var li = $('<li class="stroke_chooser_item"></li>');
    li.append(hr);
    li.click(function () {
        strokeChooserScope.selectStroke(stroke);
    });

    this.ui.append(li);

    this.strokes[stroke.id] = stroke;

    return stroke;
}
StrokeChooser.prototype.selectStroke = function (/* Stroke */ stroke) {
    var strokeChooserScope = this;
    jQuery.ajax({
        url: baseUrl('/sketchpad/choosestroke.json'),
        type: 'POST',
        data: {
            strokeId: stroke.id
        },
        success: function (strokeFromServer) {
            if (stroke.id == strokeFromServer.id) {
                stroke.mixin(strokeFromServer);
                strokeChooserScope.selectedStroke = stroke;
            }
        }
    });
}
StrokeChooser.prototype.getStrokeById = function (/* Integer */ id) {
    var stroke = null;

    if (id in this.strokes) {
        stroke = this.strokes[id];
    }

    return stroke;
}

function Stroke(/* Object */ strokeComponents, /* HTMLElement|NULL */ uiElement) {
    /**
     * Das HTML-Element zur Darstellung dieser Linie.
     *
     * @type {HTMLElement|NULL}
     */
    this.ui = uiElement;

    this.mixin(strokeComponents);
}
Stroke.prototype.asCssString = function () {
    return this.strokeWidth + 'px';
}
Stroke.prototype.mixin = function (/* Object */ strokeComponents) {
    this.id = strokeComponents.id;

    this.strokeWidth = strokeComponents.strokeWidth;
    if (this.ui) {
        this.ui.css('height', this.asCssString());
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
        url: baseUrl(url),
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(colorObject),
        success: function (data) {
            console.log(data);
        }
    });
}