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

package de.iew.services.datasource;

import de.iew.framework.utils.NoSuchPropertyException;

/**
 * Beschreibt eine Schnittstelle für die abstrakte Verwaltung von Eigenschaften
 * einer {@link de.iew.framework.domain.DataSource}.
 * <p>
 * Es wird eine Schnittstelle benötigt um beispielsweise beim Anlegen neuer
 * Knoten die initialen Daten der zugeordneten {@link de.iew.framework.domain.DataSource}
 * angeben zu können.
 * </p>
 * <p>
 * Wird in den meisten Fällen im Zusammenhang mit {@link de.iew.web.dto.WebDTO}
 * Implementierungen verwendet um die Daten vom Controller zum entsprechenden
 * {@link de.iew.services.DataSourceService} durchzureichen.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 00:24
 */
public interface DataSourceProperties {

    /**
     * Liefert den Wert für die angegebene Eigenschaft.
     *
     * @param name Der Name der Eigenschaft.
     * @return Der Wert zu der angegebenen Eigenschaft.
     * @throws NoSuchPropertyException Wird gemeldet wenn keine solche
     *                                 Eigenschaft definiert wurde.
     */
    public Object getProperty(String name) throws NoSuchPropertyException;

}
