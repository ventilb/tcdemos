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

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implements the {@link AccessDecisionVoter} interface to test web resource
 * access against access rule defined by our domain model.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 12.01.13 - 00:09
 */
public class WebResourceAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {

    private WebResourceAccessEvaluator webResourceAccessEvaluator;

    public WebResourceAccessDecisionVoter() {
    }

    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
        Collection<WebResourceAccessRuleConfigAttribute> configAttributes = findSupportedConfigAttributes(attributes);
        if (configAttributes.isEmpty()) {
            return ACCESS_ABSTAIN;
        }

        boolean granted = false;

        for (WebResourceAccessRuleConfigAttribute accessRule : configAttributes) {
            granted = this.webResourceAccessEvaluator.evaluate(authentication, object, accessRule);
        }

        return (granted ? ACCESS_GRANTED : ACCESS_DENIED);
    }

    /**
     * Sucht nach, von diesem Voter, unterstützten {@link ConfigAttribute}s.
     * <p>
     * Diese Methode ist inspiriert von {@link org.springframework.security.web.access.expression.WebExpressionVoter#findConfigAttribute(java.util.Collection).}
     * </p>
     *
     * @param configAttributesToCheck Liste der zu durchsuchenden {@link ConfigAttribute}s.
     * @return Das {@link ConfigAttribute} oder NULL wenn nicht vorhanden.
     * @see org.springframework.security.web.access.expression.WebExpressionVoter#findConfigAttribute(java.util.Collection)
     */
    protected Collection<WebResourceAccessRuleConfigAttribute> findSupportedConfigAttributes(Collection<ConfigAttribute> configAttributesToCheck) {
        List<WebResourceAccessRuleConfigAttribute> supportedConfigAttributes = new ArrayList<WebResourceAccessRuleConfigAttribute>();

        for (ConfigAttribute attribute : configAttributesToCheck) {
            if (supports(attribute)) {
                supportedConfigAttributes.add((WebResourceAccessRuleConfigAttribute) attribute);
            }
        }

        return supportedConfigAttributes;
    }

    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof WebResourceAccessRuleConfigAttribute;
    }

    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(FilterInvocation.class);
    }

    public void setWebResourceAccessEvaluator(WebResourceAccessEvaluator webResourceAccessEvaluator) {
        this.webResourceAccessEvaluator = webResourceAccessEvaluator;
    }
}
