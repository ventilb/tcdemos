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

import de.iew.domain.security.WebResource;
import de.iew.framework.security.access.RequestMapbuilder;
import de.iew.persistence.WebResourceDao;
import de.iew.persistence.models.RequestMapEntry;
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

import javax.servlet.http.HttpServletRequest;
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

    private boolean initialized = false;

    private DefaultFilterInvocationSecurityMetadataSource metadataSource;

    private LinkedHashMap<RequestMatcher, WebResource> requestWebResourceMap;

    public synchronized void getFromDatabase() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestConfigAttributeMap;

        Collection<RequestMapEntry> requestMapEntries;

        try {
            if (!this.initialized) {
                requestMapEntries = this.webResourceDao.fetchRequestMap();
                requestConfigAttributeMap = RequestMapbuilder.buildRequestConfigAttributeMap(requestMapEntries);
                this.metadataSource = new DefaultFilterInvocationSecurityMetadataSource(requestConfigAttributeMap);

                this.requestWebResourceMap = RequestMapbuilder.buildRequestWebResourceMap(requestMapEntries);

                this.initialized = true;
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("MetadataSource und WebResourceMap wurden nicht aufgebaut wegen DAO-Fehlern.", e);
            }
            this.initialized = false;
            requestConfigAttributeMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
            this.metadataSource = new DefaultFilterInvocationSecurityMetadataSource(requestConfigAttributeMap);
            this.requestWebResourceMap = new LinkedHashMap<RequestMatcher, WebResource>();
        }
    }

    public WebResource getWebResource(HttpServletRequest request) {
        getFromDatabase();
        for (Map.Entry<RequestMatcher, WebResource> webResourceMapEntry : this.requestWebResourceMap.entrySet()) {
            if (webResourceMapEntry.getKey().matches(request)) {
                return webResourceMapEntry.getValue();
            }
        }
        return null;
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
