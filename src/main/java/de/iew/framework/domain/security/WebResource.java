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
import de.iew.framework.domain.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a domain model to represent a web resource.
 * <p>
 * Web resources are identified by an URI pattern.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 15.01.13 - 23:34
 */
@Entity
@Table(name = "web_resource")
public class WebResource extends AbstractModel implements Order {
    /**
     * The URI pattern
     */
    private String pattern;

    /**
     * HTTP method (GET, POST, usw) or NULL for any method.
     */
    private String method;

    private String requiresChannel;

    private String uniqueAccesskey;

    private int ordinalNumber;

    private boolean requestCacheable;

    private WebResourcePatternMatcher patternMatcher = WebResourcePatternMatcher.ANT;

    private List<WebResourceAccessRule> accessRules = new ArrayList<WebResourceAccessRule>();

    /**
     * Gets URI pattern.
     *
     * @return the pattern
     */
    @Column(name = "pattern", length = 255)
    public String getPattern() {
        return pattern;
    }

    /**
     * Sets URI pattern.
     *
     * @param pattern the pattern
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Gets HTTP method or NULL for any method.
     *
     * @return the method
     */
    @Column(name = "method", length = 10)
    public String getMethod() {
        return method;
    }

    /**
     * Sets HTTP method or NULL for any method.
     *
     * @param method the method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Gets requires channel such as http or https.
     *
     * @return the requires channel
     */
    @Column(name = "requiresChannel", length = 10)
    public String getRequiresChannel() {
        return requiresChannel;
    }

    /**
     * Sets requires channel such as http or https.
     *
     * @param requiresChannel the requires channel
     */
    public void setRequiresChannel(String requiresChannel) {
        this.requiresChannel = requiresChannel;
    }

    /**
     * Gets unique accesskey.
     *
     * @return the unique accesskey
     */
    @Column(name = "uniqueAccesskey", length = 255)
    public String getUniqueAccesskey() {
        return uniqueAccesskey;
    }

    /**
     * Sets unique accesskey.
     *
     * @param uniqueAccesskey the unique accesskey
     */
    public void setUniqueAccesskey(String uniqueAccesskey) {
        this.uniqueAccesskey = uniqueAccesskey;
    }

    /**
     * Gets ordinal number.
     *
     * @return the ordinal number
     */
    @Column(name = "ordinalNumber")
    public int getOrdinalNumber() {
        return ordinalNumber;
    }

    /**
     * Sets ordinal number.
     *
     * @param ordinalNumber the ordinal number
     */
    public void setOrdinalNumber(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    /**
     * Is request cacheable.
     *
     * @return the boolean
     */
    @Column(name = "requestCacheable", columnDefinition = "BIT")
    public boolean isRequestCacheable() {
        return requestCacheable;
    }

    /**
     * Sets request cacheable.
     *
     * @param requestCacheable the request cacheable
     */
    public void setRequestCacheable(boolean requestCacheable) {
        this.requestCacheable = requestCacheable;
    }

    /**
     * Gets the URI pattern matcher.
     *
     * @return the pattern matcher
     */
    @Column(name = "patternMatcher", length = 255)
    @Enumerated(EnumType.STRING)
    public WebResourcePatternMatcher getPatternMatcher() {
        return patternMatcher;
    }

    /**
     * Sets the URI pattern matcher.
     *
     * @param patternMatcher the pattern matcher
     */
    public void setPatternMatcher(WebResourcePatternMatcher patternMatcher) {
        this.patternMatcher = patternMatcher;
    }

    /**
     * Gets access rules.
     *
     * @return the access rules
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "webResource")
    public List<WebResourceAccessRule> getAccessRules() {
        return accessRules;
    }

    /**
     * Sets access rules.
     *
     * @param accessRules the access rules
     */
    public void setAccessRules(List<WebResourceAccessRule> accessRules) {
        this.accessRules = accessRules;
    }
}
