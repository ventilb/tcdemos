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

package de.iew.services.impl;

import de.iew.persistence.WebResourceDao;
import de.iew.services.WebResourceSecurityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Default implementation for the {@link WebResourceSecurityService} interface.
 * <p>
 * Also implements {@link FilterInvocationSecurityMetadataSource} so this
 * implementation can be used in spring security to protect the web resources
 * with rules defined with our domain models.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.01.13 - 23:01
 */
@Service(value = "webResourceSecurityService")
public class WebResourceSecurityServiceImpl implements WebResourceSecurityService, FilterInvocationSecurityMetadataSource {

    private static final Log log = LogFactory.getLog(WebResourceSecurityServiceImpl.class);

    private volatile boolean initialized = false;

    private DefaultFilterInvocationSecurityMetadataSource metadataSource;

    public synchronized void getFromDatabase() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap;

        try {
            if (!this.initialized) {
                requestMap = this.webResourceDao.fetchRequestMap();
                this.metadataSource = new DefaultFilterInvocationSecurityMetadataSource(requestMap);
                this.initialized = true;
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("MetadataSource wurde nicht aufgebaut wegen DAO-Fehlern.", e);
            }
            this.initialized = false;
            requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
            this.metadataSource = new DefaultFilterInvocationSecurityMetadataSource(requestMap);
        }
    }

    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        getFromDatabase();
        return this.metadataSource.getAttributes(object);
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        getFromDatabase();
        return this.metadataSource.getAllConfigAttributes();
    }

    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    // Service und Dao Abhängigkeiten /////////////////////////////////////////

    private WebResourceDao webResourceDao;

    @Autowired
    public void setWebResourceDao(WebResourceDao webResourceDao) {
        this.webResourceDao = webResourceDao;
    }
}
