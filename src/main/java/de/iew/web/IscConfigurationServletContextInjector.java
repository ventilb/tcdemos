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

import de.iew.web.isc.spring.IscRequestMethodArgumentResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Manages the Smartclient configuration.
 * <p>
 * Stores the configuration in the {@link ServletContext}. We need the Smartclient
 * configuration for our Smartclient taglib.
 * </p>
 * <p>
 * Configures an {@link IscRequestMethodArgumentResolver} instance for resolving
 * Smartclient request meta data at request.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.11.12 - 20:35
 */
public class IscConfigurationServletContextInjector implements ServletContextAware, InitializingBean, BeanPostProcessor {

    public static final String CONTEXT_KEY = "de.iew.web.IscConfiguration.CONTEXT_KEY";

    private ServletContext servletContext;

    private Properties iscConfiguration;

    public void afterPropertiesSet() throws Exception {
        servletContext.setAttribute(CONTEXT_KEY, this.iscConfiguration);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Adds a {@link de.iew.web.isc.spring.IscRequestMethodArgumentResolver} instance to Springs {@link RequestMappingHandlerAdapter}
     * bean for resolving Smartclient meta data during the request.
     * </p>
     * <code>
     * public Model fetchAction(@IscRequest DSRequest dsRequest) throws Exception;
     * </code>
     *
     * @see <a href="https://jira.springsource.org/browse/SPR-8648">https://jira.springsource.org/browse/SPR-8648</a>
     */
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (o instanceof RequestMappingHandlerAdapter) {
            RequestMappingHandlerAdapter requestMappingHandlerAdapter = (RequestMappingHandlerAdapter) o;


            List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = requestMappingHandlerAdapter.getCustomArgumentResolvers();
            if (handlerMethodArgumentResolvers == null) {
                handlerMethodArgumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
            }
            handlerMethodArgumentResolvers.add(new IscRequestMethodArgumentResolver());
            requestMappingHandlerAdapter.setCustomArgumentResolvers(handlerMethodArgumentResolvers);

        }
        return o;
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setIscConfiguration(Properties iscConfiguration) {
        this.iscConfiguration = iscConfiguration;
    }
}
