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

import de.iew.domain.sketchpad.RgbColor;
import de.iew.domain.utils.AbstractDomainModelVisitor;

/**
 * Wandelt {@link RgbColor}-Domainmodelle in {@link RgbColorModel}-DTOs um.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 03.01.13 - 03:02
 */
public class ColorToColorModelTransformer extends AbstractDomainModelVisitor<RgbColor, RgbColorModel> {
    public RgbColorModel visit(RgbColor domainModel) {
        RgbColorModel colorModel = new RgbColorModel();
        colorModel.setId(domainModel.getId());
        colorModel.setOrdinalNumber(domainModel.getOrdinalNumber());
        colorModel.setR(domainModel.getR());
        colorModel.setG(domainModel.getG());
        colorModel.setB(domainModel.getB());

        return colorModel;
    }
}
