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

package de.iew.services;

import de.iew.domain.security.WebResource;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface description for implementing web resource services.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.01.13 - 23:00
 */
public interface WebResourceSecurityService {

    /**
     * Returns the {@link WebResource} instance which matches the given HTTP request.
     * <p>
     * The web resources are checked in their {@link de.iew.domain.Order}. This method returns NULL if none of the web
     * resources matches the given request.
     * </p>
     *
     * @param request the request
     * @return the web resource
     */
    public WebResource getWebResource(HttpServletRequest request);
}
