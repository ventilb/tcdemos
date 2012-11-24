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

package de.iew.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.Properties;

/**
 * Verwaltet die Smartclient Konfiguration und speichert sie im
 * {@link ServletContext} damit in den Taglibs daraus zurückgegriffen werden
 * kann.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.11.12 - 20:35
 */
public class IscConfigurationServletContextInjector implements ServletContextAware, InitializingBean {

    public static final String CONTEXT_KEY = "de.iew.web.IscConfiguration.CONTEXT_KEY";

    private ServletContext servletContext;

    private Properties iscConfiguration;

    public void afterPropertiesSet() throws Exception {
        servletContext.setAttribute(CONTEXT_KEY, this.iscConfiguration);
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setIscConfiguration(Properties iscConfiguration) {
        this.iscConfiguration = iscConfiguration;
    }
}
