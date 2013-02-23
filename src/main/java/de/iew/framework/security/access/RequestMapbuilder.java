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

package de.iew.framework.security.access;

import de.iew.framework.domain.security.WebResource;
import de.iew.framework.persistence.models.RequestMapEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.RequestMatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Builder to create spring security compatible and other request maps.
 * <p>
 * Spring security compatible means the maps can be used to setup {@link org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource}
 * instances.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 04.02.13 - 22:07
 */
public class RequestMapbuilder {
    private static final Log log = LogFactory.getLog(RequestMapbuilder.class);

    /**
     * The load factor used to initialise the request map (HashMap). Java uses a load factor of 0.75f as default load
     * factor. This value is fine for me but {@link HashMap#DEFAULT_LOAD_FACTOR} is not accessible from here.
     */
    protected static final float REQUEST_MAP_LOAD_FACTOR = 0.75f;

    /**
     * Builds a spring security compatibly request map ({@link LinkedHashMap} data structure.
     *
     * @param requestMapEntries the request map entries
     * @return the request map
     */
    public static LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> buildRequestConfigAttributeMap(Collection<RequestMapEntry> requestMapEntries) {
        // Calculate the initial capacity for our LinkedHashMap
        float requestmapEntrySize = (float) requestMapEntries.size();
        int mapInitialCapacity = (int) Math.ceil(requestmapEntrySize / REQUEST_MAP_LOAD_FACTOR);

        if (log.isDebugEnabled()) {
            log.debug("Initialisiere die LinkedHashMap mit einer Kapazität von " + mapInitialCapacity + " Einträgen.");
        }

        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>(mapInitialCapacity);

        for (RequestMapEntry requestMapEntry : requestMapEntries) {
            requestMap.put(requestMapEntry.getRequestMatcher(), requestMapEntry.getConfigAttributes());
        }

        return requestMap;
    }

    /**
     * Builds a request web resource map data structure.
     * <p>
     * The data structure maps a {@link RequestMatcher} instance to the {@link WebResource} instance.
     * </p>
     *
     * @param requestMapEntries the request map entries.
     * @return the request web resource map
     */
    public static LinkedHashMap<RequestMatcher, WebResource> buildRequestWebResourceMap(Collection<RequestMapEntry> requestMapEntries) {
        // Calculate the initial capacity for our LinkedHashMap
        float requestmapEntrySize = (float) requestMapEntries.size();
        int mapInitialCapacity = (int) Math.ceil(requestmapEntrySize / REQUEST_MAP_LOAD_FACTOR);

        if (log.isDebugEnabled()) {
            log.debug("Initialisiere die LinkedHashMap mit einer Kapazität von " + mapInitialCapacity + " Einträgen.");
        }

        LinkedHashMap<RequestMatcher, WebResource> requestMap = new LinkedHashMap<RequestMatcher, WebResource>();

        for (RequestMapEntry requestMapEntry : requestMapEntries) {
            requestMap.put(requestMapEntry.getRequestMatcher(), requestMapEntry.getWebResource());
        }

        return requestMap;
    }
}
