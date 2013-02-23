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
 * Implements a config attribute to manage a set of flags which will be used to
 * test user access by authentication state.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.01.13 - 20:37
 */
public class FlagConfigAttribute implements WebResourceAccessRuleConfigAttribute {

    private boolean permitAll = false;

    private boolean denyAll = false;

    private boolean isAnonymous = false;

    private boolean isRememberMe = false;

    private boolean isAuthenticated = false;

    private boolean isFullyAuthenticated = false;

    /**
     * Instantiates a new Flag config attribute.
     *
     * @param accessRule the access rule
     */
    public FlagConfigAttribute(WebResourceAccessRule accessRule) {
        this.permitAll = accessRule.isPermitAll();
        this.denyAll = accessRule.isDenyAll();
        this.isAnonymous = accessRule.isAnonymous();
        this.isRememberMe = accessRule.isRememberMe();
        this.isAuthenticated = accessRule.isAuthenticated();
        this.isFullyAuthenticated = accessRule.isFullyAuthenticated();
    }

    /**
     * Is permit all.
     *
     * @return the boolean
     */
    public boolean isPermitAll() {
        return permitAll;
    }

    /**
     * Is deny all.
     *
     * @return the boolean
     */
    public boolean isDenyAll() {
        return denyAll;
    }

    /**
     * Is anonymous.
     *
     * @return the boolean
     */
    public boolean isAnonymous() {
        return isAnonymous;
    }

    /**
     * Is remember me.
     *
     * @return the boolean
     */
    public boolean isRememberMe() {
        return isRememberMe;
    }

    /**
     * Is authenticated.
     *
     * @return the boolean
     */
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    /**
     * Is fully authenticated.
     *
     * @return the boolean
     */
    public boolean isFullyAuthenticated() {
        return isFullyAuthenticated;
    }

    /**
     * Always returns NULL.
     *
     * @return NULL
     */
    public String getAttribute() {
        return null;
    }
}
