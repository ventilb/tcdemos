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

package de.iew.framework.domain.security;

import de.iew.framework.domain.AbstractModel;
import de.iew.framework.domain.principals.Authority;
import org.springframework.security.access.ConfigAttribute;

import javax.persistence.*;

/**
 * Implements a domain model for specifying access rules to our web resources.
 * <p>
 * This class models Spring Securities intercept-url "access" property as a
 * domain model.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.01.13 - 20:57
 */
@Entity
@Table(name = "web_resource_access_rule")
public class WebResourceAccessRule extends AbstractModel {
    private Discriminator ruleDiscriminator;

    private WebResource webResource;

    private Authority authority;

    private boolean permitAll = false;

    private boolean denyAll = false;

    private boolean isAnonymous = false;

    private boolean isRememberMe = false;

    private boolean isAuthenticated = false;

    private boolean isFullyAuthenticated = false;

    /**
     * Gets rule discriminator.
     *
     * @return the rule discriminator
     */
    @Column(name = "ruleDiscriminator", length = 255, nullable = false)
    @Enumerated(EnumType.STRING)
    public Discriminator getRuleDiscriminator() {
        return ruleDiscriminator;
    }

    /**
     * Sets rule discriminator.
     *
     * @param ruleDiscriminator the rule discriminator
     */
    public void setRuleDiscriminator(Discriminator ruleDiscriminator) {
        this.ruleDiscriminator = ruleDiscriminator;
    }

    /**
     * Gets web resource.
     *
     * @return the web resource
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "web_resource_id", nullable = false)
    public WebResource getWebResource() {
        return webResource;
    }

    /**
     * Sets web resource.
     *
     * @param webResource the web resource
     */
    public void setWebResource(WebResource webResource) {
        this.webResource = webResource;
    }

    /**
     * Gets authority.
     *
     * @return the authority
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id", nullable = true)
    public Authority getAuthority() {
        return authority;
    }

    /**
     * Sets authority.
     *
     * @param authority the authority
     */
    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    /**
     * Is permit all.
     *
     * @return the boolean
     */
    @Column(name = "permitAll", columnDefinition = "BIT")
    public boolean isPermitAll() {
        return permitAll;
    }

    /**
     * Sets permit all.
     *
     * @param permitAll the permit all
     */
    public void setPermitAll(boolean permitAll) {
        this.permitAll = permitAll;
    }

    /**
     * Is deny all.
     *
     * @return the boolean
     */
    @Column(name = "denyAll", columnDefinition = "BIT")
    public boolean isDenyAll() {
        return denyAll;
    }

    /**
     * Sets deny all.
     *
     * @param denyAll the deny all
     */
    public void setDenyAll(boolean denyAll) {
        this.denyAll = denyAll;
    }

    /**
     * Is anonymous.
     *
     * @return the boolean
     */
    @Column(name = "isAnonymous", columnDefinition = "BIT")
    public boolean isAnonymous() {
        return isAnonymous;
    }

    /**
     * Sets anonymous.
     *
     * @param anonymous the anonymous
     */
    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    /**
     * Is remember me.
     *
     * @return the boolean
     */
    @Column(name = "isRememberMe", columnDefinition = "BIT")
    public boolean isRememberMe() {
        return isRememberMe;
    }

    /**
     * Sets remember me.
     *
     * @param rememberMe the remember me
     */
    public void setRememberMe(boolean rememberMe) {
        isRememberMe = rememberMe;
    }

    /**
     * Is authenticated.
     *
     * @return the boolean
     */
    @Column(name = "isAuthenticated", columnDefinition = "BIT")
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    /**
     * Sets authenticated.
     *
     * @param authenticated the authenticated
     */
    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    /**
     * Is fully authenticated.
     *
     * @return the boolean
     */
    @Column(name = "isFullyAuthenticated", columnDefinition = "BIT")
    public boolean isFullyAuthenticated() {
        return isFullyAuthenticated;
    }

    /**
     * Sets fully authenticated.
     *
     * @param fullyAuthenticated the fully authenticated
     */
    public void setFullyAuthenticated(boolean fullyAuthenticated) {
        isFullyAuthenticated = fullyAuthenticated;
    }

    /**
     * Enumeration of web resource access rule discriminators.
     * <p>
     * Background: We need a simple solution to map the database rules to
     * our {@link ConfigAttribute} implementations. To keep it simple no
     * hibernate inheritance is planned here.
     * </p>
     */
    public static enum Discriminator {
        /**
         * The access rule denotes an {@link Authority}.
         */
        HAS_AUTHORITY,
        /**
         * The access rule is specified by a flag.
         */
        FLAG;

    }
}
