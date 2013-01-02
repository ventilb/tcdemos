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
 * Implementiert Grafikobjekte.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 13:22
 */

define(function () {
    // Shape Basisklasse //////////////////////////////////////////////////////////
    function Shape() {

        /**
         * Der Paint-Context.
         *
         * @type {Context}
         */
        this.context = null;

        /**
         * Flag ob dieses Shape aus dem Paint herausgepickt werden kann.
         *
         * @type {boolean}
         */
        this.pickable = true;

        /**
         * Flag ob dieses Shape verändert wurde.
         *
         * @type {boolean}
         */
        this.dirty = false;
    }

    Shape.prototype.draw = function () {
    }
    Shape.prototype.contains = function (/* Integer */ x, /* Integer */ y) {
        return false;
    }
    Shape.prototype.moveTo = function (/* Integer */ x, /* Integer */ y) {
    }
    Shape.prototype.toString = function () {
        return 'Shape[]';
    }
    Shape.prototype.getName = function () {
        var funcNameRegex = /function (.{1,})\(/;
        var results = (funcNameRegex).exec((this).constructor.toString());
        return (results && results.length > 1) ? results[1] : "";
    };

    // Rect Shape /////////////////////////////////////////////////////////////////
    Rect.prototype = new Shape();
    Rect.prototype.constructor = Rect;
    Rect.prototype.parent = Shape.prototype;
    function Rect(/* Integer */ rx, /* Integer */ ry, /* Integer */ rw, /* Integer */ rh) {
        this.rx = rx;
        this.ry = ry;
        this.rw = rw;
        this.rh = rh;

        this.locationChanged = function () {
            this.cx = this.rx + this.rw / 2;
            this.cy = this.ry + this.rh / 2;

            this.dirty = true;
        }
        this.locationChanged();
    }

    Rect.prototype.moveTo = function (/* Integer */ x, /* Integer */ y) {
        var oldX = this.rx;
        var oldY = this.ry;

        this.rx = x;
        this.ry = y;

        this.locationChanged();

        if (typeof this.moved == 'function') {
            this.moved(oldX, oldY);
        }
    }
    Rect.prototype.moveTopLeftTo = function (/* Integer */ x, /* Integer */ y) {
        var oldX = this.rx;
        var oldY = this.ry;

        var dx = x - oldX;
        var dy = y - oldY;

        this.rx = x;
        this.ry = y;
        this.rw = this.rw - dx;
        this.rh = this.rh - dy;

        this.locationChanged();

        if (typeof this.moved == 'function') {
            this.moved(oldX, oldY);
        }
    }
    Rect.prototype.moveBottomRightTo = function (/* Integer */ x, /* Integer */ y) {
        this.rw = x - this.rx;
        this.rh = y - this.ry;

        this.locationChanged();
    }
    Rect.prototype.draw = function () {
        this.context.fillStyle = 'rgba(0, 0, 255, 0.5)';
        this.context.fillRect(this.rx, this.ry, this.rw, this.rh);

        this.dirty = false;
    }
    Rect.prototype.contains = function (/* Integer */ x, /* Integer */ y) {
        return (x >= this.rx) && ((this.rx + this.rw) > x) && (y >= this.ry) && (this.ry + this.rh > y);
    }
    Rect.prototype.toString = function () {
        return 'Rect[x=' + this.rx + ',y=' + this.ry + ',w=' + this.rw + ',h=' + this.rh + ']';
    }

    // Polygon Shape //////////////////////////////////////////////////////////////
    Polygon.prototype = new Shape();
    Polygon.prototype.constructor = Polygon;
    Polygon.prototype.parent = Shape.prototype;
    function Polygon() {
        this.segments = [];

        this.lineColor = 'rgb(0,0,0)';
        this.lineWidth = 1;
    }

    Polygon.prototype.moveTo = function (/* Integer */ x, /* Integer */ y) {
        var lastSegment = this.getLastSegment();
        if (lastSegment != null && lastSegment.x == x && lastSegment.y == y) {
            return;
        }
        this.segments.push({x: x, y: y});
        this.dirty = true;
    }
    Polygon.prototype.getLastSegment = function () {
        var segmentCount = this.segments.length;
        if (segmentCount > 0) {
            return this.segments[segmentCount - 1];
        }
        return null;
    }
    Polygon.prototype.draw = function () {
        this.context.lineWidth = this.lineWidth;
        this.context.strokeStyle = this.lineColor;

        var segmentCount = this.segments.length;
        if (segmentCount >= 2) {
            this.context.beginPath();

            var segment = this.segments[0];
            this.context.moveTo(segment.x, segment.y);

            for (var i = 1; i < segmentCount; i++) {
                segment = this.segments[i];
                this.context.lineTo(segment.x, segment.y);
            }

            this.context.stroke();
            this.dirty = false;
        }
    }
    Polygon.prototype.toString = function () {
        return 'Polygon[pointCount=' + this.segments.length + ']';
    }

    return {
        Polygon: Polygon,
        Rect: Rect
    }
});