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

package de.iew.framework.persistence.hibernate;

import de.iew.framework.domain.ModelInstantiationException;
import de.iew.framework.domain.security.WebResource;
import de.iew.framework.domain.security.WebResourceAccessRule;
import de.iew.framework.domain.security.WebResourcePatternMatcher;
import de.iew.framework.security.access.FlagConfigAttribute;
import de.iew.framework.security.access.HasAuthorityConfigAttribute;
import de.iew.framework.persistence.WebResourceDao;
import de.iew.framework.persistence.models.RequestMapEntry;
import org.apache.commons.beanutils.ConstructorUtils;
import org.hibernate.Session;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

/**
 * Implementation of the {@link WebResourceDao} interface to access our web
 * resource models through Hibernate.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.01.13 - 22:59
 */
@Repository(value = "webResourceDao")
public class HbmWebResourceDaoImpl extends AbstractHbmDomainModelDaoImpl<WebResource> implements WebResourceDao {

    private static final Hashtable<WebResourceAccessRule.Discriminator, Class> CONFIG_ATTRIBUTE_DISCRIMINATOR_MAP = new Hashtable<WebResourceAccessRule.Discriminator, Class>();

    static {
        CONFIG_ATTRIBUTE_DISCRIMINATOR_MAP.put(WebResourceAccessRule.Discriminator.FLAG, FlagConfigAttribute.class);
        CONFIG_ATTRIBUTE_DISCRIMINATOR_MAP.put(WebResourceAccessRule.Discriminator.HAS_AUTHORITY, HasAuthorityConfigAttribute.class);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Evicts the loaded {@link WebResource} models from the hibernate session. We want to cache the web resources
     * independent from hibernate in the service. All required associations will be also evicted from the hibernate
     * session. Don't fear the Hibernate Exceptions :-)
     * </p>
     */
    public Collection<RequestMapEntry> fetchRequestMap() throws ModelInstantiationException {
        Session session = getCurrentSession();

        List<RequestMapEntry> requestMapEntries = new ArrayList<RequestMapEntry>();

        Collection<WebResource> webResources = findAllOrderedAscending();

        RequestMapEntry requestMapEntry;
        RequestMatcher requestMatcher;
        Collection<ConfigAttribute> configAttributes;
        for (WebResource webResource : webResources) {
            requestMatcher = createRequestMatcherFromWebResource(webResource);
            configAttributes = createConfigAttributeFromWebResource(webResource);

            session.evict(webResource);
            requestMapEntry = new RequestMapEntry(requestMatcher, webResource, configAttributes);
            requestMapEntries.add(requestMapEntry);
        }

        return requestMapEntries;
    }

    private Collection<ConfigAttribute> createConfigAttributeFromWebResource(WebResource webResource) throws ModelInstantiationException {
        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();

        ConfigAttribute configAttribute;
        for (WebResourceAccessRule accessRule : webResource.getAccessRules()) {
            configAttribute = newConfigAttributeInstance(accessRule);
            configAttributes.add(configAttribute);
        }

        return configAttributes;
    }

    private RequestMatcher createRequestMatcherFromWebResource(WebResource webResource) {
        if (WebResourcePatternMatcher.ANT.equals(webResource.getPatternMatcher())) {
            return new AntPathRequestMatcher(webResource.getPattern(), webResource.getMethod());
        }
        throw new UnsupportedPatternMatcherException("The requested web resource pattern matcher is not supported.");
    }

    /**
     * Creates a new config attribute instance from a given web resource access rule.
     *
     * @param accessRule the web resource access rule
     * @return the config attribute
     * @throws ModelInstantiationException Thrown if the config attribute could not be created
     */
    private ConfigAttribute newConfigAttributeInstance(WebResourceAccessRule accessRule) throws ModelInstantiationException {
        Class discriminatorClass = CONFIG_ATTRIBUTE_DISCRIMINATOR_MAP.get(accessRule.getRuleDiscriminator());
        try {
            return (ConfigAttribute) ConstructorUtils.invokeConstructor(discriminatorClass, accessRule);
        } catch (Exception e) {
            throw new ModelInstantiationException("Create config attribute constructor call failed on class " + discriminatorClass + ".", e);
        }
    }

}
