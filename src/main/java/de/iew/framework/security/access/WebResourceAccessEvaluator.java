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

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

/**
 * Implements a class to test an {@link Authentication} instance against a
 * protected web resource.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 19.01.13 - 11:19
 */
public class WebResourceAccessEvaluator {

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    private PermissionEvaluator permissionEvaluator = new DenyAllPermissionEvaluator();

    private RoleHierarchy roleHierarchy;

    /**
     * Dispatches the config attribute to {@link #evaluate(org.springframework.security.core.Authentication, org.springframework.security.web.FilterInvocation, FlagConfigAttribute)}
     * or {@link #evaluate(org.springframework.security.core.Authentication, org.springframework.security.web.FilterInvocation, HasAuthorityConfigAttribute)}
     * depending on the instance.
     *
     * @param authentication   the authentication
     * @param filterInvocation the filter invocation
     * @param configAttribute  the config attribute
     * @return the boolean
     */
    public boolean evaluate(Authentication authentication, FilterInvocation filterInvocation, WebResourceAccessRuleConfigAttribute configAttribute) {
        if (configAttribute instanceof HasAuthorityConfigAttribute) {
            return evaluate(authentication, filterInvocation, (HasAuthorityConfigAttribute) configAttribute);
        } else if (configAttribute instanceof FlagConfigAttribute) {
            return evaluate(authentication, filterInvocation, (FlagConfigAttribute) configAttribute);
        }
        throw new IllegalArgumentException("Unsupported web resource access rule config attribute.");
    }

    /**
     * Evaluate the {@link HasAuthorityConfigAttribute} instance.
     *
     * @param authentication              the authentication
     * @param filterInvocation            the filter invocation
     * @param hasAuthorityConfigAttribute the has authority config attribute
     * @return the boolean
     */
    public boolean evaluate(Authentication authentication, FilterInvocation filterInvocation, HasAuthorityConfigAttribute hasAuthorityConfigAttribute) {
        SecurityExpressionOperations ops = createSecurityExpressionRoot(authentication, filterInvocation);
        return ops.hasRole(hasAuthorityConfigAttribute.getAttribute());
    }

    /**
     * Evaluate the {@link FlagConfigAttribute} instance.
     *
     * @param authentication      the authentication
     * @param filterInvocation    the filter invocation
     * @param flagConfigAttribute the flag config attribute
     * @return the boolean
     */
    public boolean evaluate(Authentication authentication, FilterInvocation filterInvocation, FlagConfigAttribute flagConfigAttribute) {
        SecurityExpressionOperations ops = createSecurityExpressionRoot(authentication, filterInvocation);
        if (flagConfigAttribute.isAnonymous()) {
            return ops.isAnonymous();
        } else if (flagConfigAttribute.isPermitAll()) {
            return ops.permitAll();
        } else if (flagConfigAttribute.isDenyAll()) {
            return ops.denyAll();
        } else if (flagConfigAttribute.isRememberMe()) {
            return ops.isRememberMe();
        } else if (flagConfigAttribute.isAuthenticated()) {
            return ops.isAuthenticated();
        } else if (flagConfigAttribute.isFullyAuthenticated()) {
            return ops.isFullyAuthenticated();
        }
        throw new IllegalArgumentException("Illegal flag config attribute: All flags are false");
    }

    /**
     * Create security expression root.
     * <p>
     * Implements methods to test permissions.
     * </p>
     *
     * @param authentication the authentication
     * @param fi             the fi
     * @return the security expression operations
     */
    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
        WebSecurityExpressionRoot root = new WebSecurityExpressionRoot(authentication, fi);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }

    /**
     * Gets permission evaluator.
     *
     * @return the permission evaluator
     */
    public PermissionEvaluator getPermissionEvaluator() {
        return permissionEvaluator;
    }

    /**
     * Sets permission evaluator.
     *
     * @param permissionEvaluator the permission evaluator
     */
    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
    }

    /**
     * Gets role hierarchy.
     *
     * @return the role hierarchy
     */
    public RoleHierarchy getRoleHierarchy() {
        return roleHierarchy;
    }

    /**
     * Sets role hierarchy.
     *
     * @param roleHierarchy the role hierarchy
     */
    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

}
