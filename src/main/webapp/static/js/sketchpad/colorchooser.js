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
 * Implementiert ein Modell für die Verwaltung von Farbangaben und stellt
 * einen Farbauswahldialog für das Sketchpad bereit.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 12:29
 */
define(['jquery', 'core'], function ($, core) {

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

        $.ajax({
            colorChooserScope: this, // Scopeverweis um im Callback auf den ColorChooser zugreifen zu können

            url: core.baseUrl('/sketchpad/listcolors.json'),
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
        $.ajax({
            url: core.baseUrl('/sketchpad/choosecolor.json'),
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

    return ColorChooser;

});