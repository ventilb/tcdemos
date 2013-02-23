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

package de.iew.services.impl;

import de.iew.framework.domain.DataSource;
import de.iew.framework.domain.SimpleTextData;
import de.iew.framework.domain.TextItem;
import de.iew.framework.domain.TextItemCollection;
import de.iew.framework.persistence.SimpleTextDataDao;
import de.iew.services.DataSourceService;
import de.iew.services.datasource.DataSourceProperties;
import de.iew.framework.utils.NoSuchPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Implementiert einen Dienst für die Verwaltung einfacher Textbausteine als
 * {@link DataSource}.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 20:58
 */
@Service(value = "simpleTextDataDataSourceService")
public class SimpleTextDataDataSourceServiceImpl implements DataSourceService {

    private SimpleTextDataDao simpleTextDataDao;

    private Locale defaultLocale;

    public DataSource createPersistedDefaultDataSource(DataSourceProperties dataSourceProperties) throws NoSuchPropertyException {
        TextItemCollection textItemCollection = new TextItemCollection();

        TextItem textItem = new TextItem();
        textItem.setContent((String) dataSourceProperties.getProperty("title"));
        textItem.setLanguageCode(this.defaultLocale.getLanguage());
        textItem.setCountryCode(this.defaultLocale.getCountry());

        SimpleTextData simpleTextData = new SimpleTextData();

        textItemCollection.getTextItems().add(textItem);

        simpleTextData.setTextItemCollection(textItemCollection);

        return this.simpleTextDataDao.save(simpleTextData);
    }

    @Autowired
    public void setSimpleTextDataDao(SimpleTextDataDao simpleTextDataDao) {
        this.simpleTextDataDao = simpleTextDataDao;
    }

    @Value(value = "#{config['locale.default']}")
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }
}
