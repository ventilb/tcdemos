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

import de.iew.framework.domain.security.WebResourceAccessRule;

/**
 * Implements a config attribute to manage an {@link de.iew.framework.domain.principals.Authority}
 * system name for web resource access rule evaluation.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.01.13 - 20:19
 */
public class HasAuthorityConfigAttribute implements WebResourceAccessRuleConfigAttribute {

    private String authorityName;

    /**
     * Instantiates a new Has authority config attribute.
     *
     * @param accessRule the access rule
     */
    public HasAuthorityConfigAttribute(WebResourceAccessRule accessRule) {
        if (accessRule == null) {
            throw new IllegalArgumentException("accessRule can't be null");
        }
        this.authorityName = accessRule.getAuthority().getSystemName();
    }

    /**
     * Returns the authorities system name.
     *
     * @return the authorities system name
     */
    public String getAttribute() {
        return this.authorityName;
    }
}
