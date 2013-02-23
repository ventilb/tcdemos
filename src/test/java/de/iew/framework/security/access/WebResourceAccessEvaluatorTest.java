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

import de.iew.framework.domain.principals.Authority;
import de.iew.framework.domain.security.WebResourceAccessRule;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Implements unit test cases to test the {@link WebResourceAccessEvaluator}
 * class.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 19.01.13 - 11:22
 */
public class WebResourceAccessEvaluatorTest {

    /**
     * Testet den Fall, dass der Benutzer in der Rolle der geschützten
     * Web-Ressource ist. Ergebnis Zugriff erlaubt.
     *
     * @throws Exception
     */
    @Test
    public void testEvaluateHasAuthorityConfigAttributeUserIsInRole() throws Exception {
        // Testfix erstellen
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_JUNIT_TEST"));
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken("JUnit", "JUnit", grantedAuthorities);

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        Authority authority = new Authority();
        authority.setSystemName("ROLE_JUNIT_TEST");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setAuthority(authority);

        HasAuthorityConfigAttribute configAttribute = new HasAuthorityConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertTrue(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    /**
     * Test den Fall, dass der Benutzer nicht in der Rolle der geschützten
     * Web-Ressource ist. Ergebnis Zugriff verweigert.
     *
     * @throws Exception
     */
    @Test
    public void testEvaluateHasAuthorityConfigAttributeUserIsNotInRole() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newUserAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        Authority authority = new Authority();
        authority.setSystemName("ROLE_JUNIT_TEST");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setAuthority(authority);

        HasAuthorityConfigAttribute configAttribute = new HasAuthorityConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertFalse(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    private Authentication newUserAuthenticationToken() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken("JUnit", "JUnit", grantedAuthorities);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateFlagConfigAttributeAllFlagsAreFalseException() {
        // Testfix erstellen
        Authentication authenticationToken = newAnonymousAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        /// Alle Flags sind per Default FALSE

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute);
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsPermitAll() throws Exception {
        // Fall 1: Nutzer ist Anonym //////////////////////////////////////////
        // Testfix erstellen
        Authentication authenticationToken = newAnonymousAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setPermitAll(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertTrue(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));

        // Fall 2: Nutzer ist angemeldet //////////////////////////////////////
        // Testfix erstellen
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_JUNIT_TEST"));
        authenticationToken = new UsernamePasswordAuthenticationToken("JUnit", "JUnit", grantedAuthorities);

        rule = new WebResourceAccessRule();
        rule.setPermitAll(true);

        configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertTrue(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsDenyAll() throws Exception {
        // Fall 1: Nutzer ist Anonym //////////////////////////////////////////
        // Testfix erstellen
        Authentication authenticationToken = newAnonymousAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setDenyAll(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertFalse(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));

        // Fall 2: Nutzer ist angemeldet //////////////////////////////////////
        // Testfix erstellen
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_JUNIT_TEST"));
        authenticationToken = new UsernamePasswordAuthenticationToken("JUnit", "JUnit", grantedAuthorities);

        rule = new WebResourceAccessRule();
        rule.setDenyAll(true);

        configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertFalse(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }


    @Test
    public void testEvaluateFlagConfigAttributeIsAnonymous() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newAnonymousAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setAnonymous(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertTrue(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsNotAnonymous() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newUserAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setAnonymous(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertFalse(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsRememberMeGranted() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newRememberMeAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setRememberMe(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertTrue(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsRememberMeDeniedAsUser() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newUserAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setRememberMe(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertFalse(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsRememberMeDeniedAsAnonymous() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newAnonymousAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setRememberMe(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertFalse(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsAuthenticatedDeniedAsAnonymous() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newAnonymousAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setAuthenticated(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertFalse(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsAuthenticatedGrantedAsUser() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newUserAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setAuthenticated(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertTrue(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    /**
     * Is Authenticated lässt den Nutzer auch durch, wenn er als Remember-Me
     * authentifieziert wurde. Im Gegensatz dazu würde der Zugriff bei
     * Is Fully Authenticated verweigert.
     *
     * @throws Exception
     */
    @Test
    public void testEvaluateFlagConfigAttributeIsAuthenticatedGrantedAsRememberMe() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newRememberMeAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setAuthenticated(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertTrue(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsFullyAuthenticatedGrantedAsUser() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newUserAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setFullyAuthenticated(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertTrue(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsFullyAuthenticatedDeniedAsAnonymous() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newAnonymousAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setFullyAuthenticated(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertFalse(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    private Authentication newAnonymousAuthenticationToken() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        return new AnonymousAuthenticationToken("anonymous", "anonymous", grantedAuthorities);
    }

    @Test
    public void testEvaluateFlagConfigAttributeIsFullyAuthenticatedDeniedAsRememberMe() throws Exception {
        // Testfix erstellen
        Authentication authenticationToken = newRememberMeAuthenticationToken();

        FilterInvocation filterInvocation = new FilterInvocation("/junit", "GET");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setFullyAuthenticated(true);

        FlagConfigAttribute configAttribute = new FlagConfigAttribute(rule);

        // Das Testobjekt erstellen
        WebResourceAccessEvaluator webResourceAccessEvaluator = new WebResourceAccessEvaluator();

        // Test und Auswertung
        assertFalse(webResourceAccessEvaluator.evaluate(authenticationToken, filterInvocation, configAttribute));
    }

    private Authentication newRememberMeAuthenticationToken() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new RememberMeAuthenticationToken("remember-me", "remember-me", grantedAuthorities);
    }
}
