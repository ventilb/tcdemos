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

package de.iew.demos.model;

import de.iew.sketchpad.domain.Stroke;
import de.iew.framework.domain.utils.AbstractDomainModelVisitor;

/**
 * Wandelt {@link de.iew.sketchpad.domain.Stroke}-Domainmodelle in {@link StrokeModel}-DTOs um.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 03.01.13 - 02:29
 */
public class StrokeToStrokeModelTransformer extends AbstractDomainModelVisitor<Stroke, StrokeModel> {
    public StrokeModel visit(Stroke domainModel) {
        StrokeModel strokeModel = new StrokeModel();
        strokeModel.setId(domainModel.getId());
        strokeModel.setOrdinalNumber(domainModel.getOrdinalNumber());
        strokeModel.setStrokeWidth(domainModel.getStrokeWidth());

        return strokeModel;
    }

}
