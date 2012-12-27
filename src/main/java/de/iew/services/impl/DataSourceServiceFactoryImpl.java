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

import de.iew.domain.SimpleTextData;
import de.iew.services.DataSourceService;
import de.iew.services.DataSourceServiceFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Implementiert einen Dienst zum Auflösen von
 * {@link DataSourceService}-Implementierungen.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @TODO Ausbauen und Services bspw anhand einer speziellen Annotation deklarieren und in PostConstruct suchen lassen.
 * @since 30.11.12 - 20:56
 */
@Service(value = "dataSourceServiceFactory")
public class DataSourceServiceFactoryImpl implements DataSourceServiceFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    public DataSourceService lookupDataSourceService(String dataSourceClassname) {
        if (SimpleTextData.class.getName().equals(dataSourceClassname)) {
            return (DataSourceService) this.applicationContext.getBean("simpleTextDataDataSourceService");
        }
        throw new RuntimeException();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
