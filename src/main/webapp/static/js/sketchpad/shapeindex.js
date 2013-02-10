/*
 * Copyright 2012-2013 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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
 * Implements a shape index data structure.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 09.02.2013 - 10:01
 */

define(function () {

    function ShapeIndex() {
        this.shapeIndex = {};
        this.shapeList = [];
        this.lastInsertedShapeId = null;
    }

    ShapeIndex.prototype.put = function (/* {Shapes.Shape} */ shape) {
        var shapeId = shape.id;

        if (!this.containsShape(shapeId)) {
            this.shapeIndex[shapeId] = shape;
            this.shapeList.push(shape);
            this.lastInsertedShapeId = Math.max(+this.lastInsertedShapeId, shape.id);
        }
    }

    ShapeIndex.prototype.getLastInsertedShapeId = function () {
        return this.lastInsertedShapeId;
    }

    ShapeIndex.prototype.containsShape = function (/* number */ shapeId) {
        var shapeId = +shapeId;
        return shapeId in this.shapeIndex;
    }

    ShapeIndex.prototype.list = function () {
        return this.shapeList;
    }

    ShapeIndex.prototype.size = function () {
        return this.shapeList.length;
    }

    return ShapeIndex;
});