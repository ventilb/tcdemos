/*
 * Copyright 2013 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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
 * Implementiert ein Modell für die Verwaltung von Strichangaben und stellt
 * einen Strichauswahldialog für das Sketchpad bereit.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 12:32
 */
define(['jquery', 'core'], function ($, core) {
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

        $.ajax({
            strokeChooserScope: this, // Scopeverweis um im Callback auf den StrokeChooser zugreifen zu können

            url: core.baseUrl('/sketchpad/liststrokes.json'),
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
            url: core.baseUrl('/sketchpad/choosestroke.json'),
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

    return StrokeChooser;

});