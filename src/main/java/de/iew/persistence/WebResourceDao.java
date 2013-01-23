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

package de.iew.persistence;

import de.iew.domain.ModelInstantiationException;
import de.iew.domain.security.WebResource;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.RequestMatcher;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * DAO interface description to manage {@link WebResource} domain model
 * instances.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.01.13 - 22:56
 */
public interface WebResourceDao extends DomainModelDao<WebResource> {

    /**
     * Returns a spring security compatible request map for the web resources.
     *
     * @return the linked hash map
     * @throws ModelInstantiationException the model instantiation exception
     */
    public LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> fetchRequestMap() throws ModelInstantiationException;
}
