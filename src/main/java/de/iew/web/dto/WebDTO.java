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

package de.iew.web.dto;

import de.iew.framework.utils.NoSuchPropertyException;
import de.iew.services.datasource.DataSourceProperties;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Abstrakte Basisklasse für die Implementierung von DTO-Objekten für die
 * Vermittlung der Daten zwischen Web- und Serviceschicht.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 13:17
 */
public abstract class WebDTO implements DataSourceProperties {

    /**
     * {@inheritDoc}
     * <p>
     * Verwendet Commons-Beanutils um den Getter für die angeforderte
     * Eigenschaft aufzurufen.
     * </p>
     */
    public Object getProperty(String name) throws NoSuchPropertyException {
        try {
            return PropertyUtils.getProperty(this, name);
        } catch (Exception e) {
            throw new NoSuchPropertyException("The requested property " + name + " could not be resolved.", e);
        }
    }
}
