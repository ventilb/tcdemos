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

package de.iew.framework.spring.web.savedrequest;

import de.iew.domain.security.WebResource;
import de.iew.services.WebResourceSecurityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implements a {@link javax.servlet.Filter} to check incoming requests against our web resources.
 * <p>
 * Stores the request in the request cache if a matching web resource was found for the request and the web resource
 * has it's {@link WebResource#requestCacheable} flag TRUE. Otherwise this method doesn't have any effect.
 * </p>
 * <p>
 * Background about this filter is, if the user clicks the login button we need a solution to save the last request so he
 * gets a redirect to there. Spring can save the last request but often this leads to JSON requests being stored.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 03.02.13 - 15:21
 */
public class WebResourceRequestSaveFilter extends GenericFilterBean {

    private static final Log log = LogFactory.getLog(WebResourceRequestSaveFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        WebResource webResource = this.webResourceSecurityService.getWebResource(httpServletRequest);

        if (log.isDebugEnabled()) {
            log.debug("Habe web resource gefunden, die auf den Request matched " + webResource);
        }

        if (webResource != null && webResource.isRequestCacheable()) {
            this.portalRequestCache.saveRequest(httpServletRequest, httpServletResponse);
        }

        chain.doFilter(request, response);
    }

    // Spring Abhängigkeiten //////////////////////////////////////////////////

    private RequestCache portalRequestCache;

    private WebResourceSecurityService webResourceSecurityService;

    @Autowired(required = false)
    public void setPortalRequestCache(RequestCache portalRequestCache) {
        this.portalRequestCache = portalRequestCache;
    }

    @Autowired
    public void setWebResourceSecurityService(WebResourceSecurityService webResourceSecurityService) {
        this.webResourceSecurityService = webResourceSecurityService;
    }
}
