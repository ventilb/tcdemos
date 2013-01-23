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

package de.iew.persistence.hibernate;

import de.iew.domain.ModelInstantiationException;
import de.iew.domain.security.WebResource;
import de.iew.domain.security.WebResourceAccessRule;
import de.iew.domain.security.WebResourcePatternMatcher;
import de.iew.persistence.WebResourceDao;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Implementation of the {@link WebResourceDao} interface to access our web
 * resource models through Hibernate.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.01.13 - 22:59
 */
@Repository(value = "webResourceDao")
public class HbmWebResourceDaoImpl extends AbstractHbmDomainModelDaoImpl<WebResource> implements WebResourceDao {
    public LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> fetchRequestMap() throws ModelInstantiationException {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();

        Collection<WebResource> webResources = findAll();

        RequestMatcher requestMatcher;
        Collection<ConfigAttribute> configAttributes;
        for (WebResource webResource : webResources) {
            configAttributes = createConfigAttributeFromWebResource(webResource);
            requestMatcher = createRequestMatcherFromWebResource(webResource);

            requestMap.put(requestMatcher, configAttributes);
        }

        return requestMap;
    }

    private Collection<ConfigAttribute> createConfigAttributeFromWebResource(WebResource webResource) throws ModelInstantiationException {
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();

        ConfigAttribute configAttribute;
        for (WebResourceAccessRule accessRule : webResource.getAccessRules()) {
            configAttribute = accessRule.getRuleDiscriminator().newConfigAttributeInstance(accessRule);
            configAttributes.add(configAttribute);
        }

        return configAttributes;
    }

    private RequestMatcher createRequestMatcherFromWebResource(WebResource webResource) {
        return WebResourcePatternMatcher.createRequestMatcher(webResource);
    }


}
