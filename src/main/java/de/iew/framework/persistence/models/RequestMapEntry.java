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

package de.iew.framework.persistence.models;

import de.iew.framework.domain.security.WebResource;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.RequestMatcher;

import java.util.Collection;

/**
 * Implements a simple value object for the persistence layer to hold the values for a request map entry.
 * <p>
 * A request map entry is composed of the backing {@link WebResource} and Springs {@link RequestMatcher} and
 * {@link ConfigAttribute} stuff.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 04.02.13 - 21:13
 */
public class RequestMapEntry {

    private final RequestMatcher requestMatcher;

    private final WebResource webResource;

    private final Collection<ConfigAttribute> configAttributes;

    /**
     * Instantiates a new Request map entry.
     *
     * @param requestMatcher   the request matcher
     * @param webResource      the web resource
     * @param configAttributes the config attributes
     */
    public RequestMapEntry(RequestMatcher requestMatcher, WebResource webResource, Collection<ConfigAttribute> configAttributes) {
        this.requestMatcher = requestMatcher;
        this.webResource = webResource;
        this.configAttributes = configAttributes;
    }

    /**
     * Gets request matcher.
     *
     * @return the request matcher
     */
    public RequestMatcher getRequestMatcher() {
        return requestMatcher;
    }

    /**
     * Gets web resource.
     *
     * @return the web resource
     */
    public WebResource getWebResource() {
        return webResource;
    }

    /**
     * Gets config attributes.
     *
     * @return the config attributes
     */
    public Collection<ConfigAttribute> getConfigAttributes() {
        return configAttributes;
    }
}
