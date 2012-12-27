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

package de.iew.services;

import de.iew.domain.DataSource;
import de.iew.services.datasource.DataSourceProperties;
import de.iew.framework.utils.NoSuchPropertyException;

/**
 * Beschreibt die Schnittstelle für Dienste, die Funktionen zu einer
 * Klasse an {@link DataSource}-Modellen bereitstellen.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 20:55
 */
public interface DataSourceService {

    /**
     * Erstellt ein Default-Exemplar des verwalteten
     * {@link DataSource}-Modells.
     *
     * @param dataSourceProperties Initiale Eigenschaften des Modells.
     * @return Das erstellte Modell.
     * @throws NoSuchPropertyException Wird gemeldet, wenn in <code>dataSourceProperties</code>
     *                                 auf eine nicht vorhandene Eigenschaft
     *                                 zugegriffen wird.
     */
    public DataSource createPersistedDefaultDataSource(DataSourceProperties dataSourceProperties) throws NoSuchPropertyException;
}
