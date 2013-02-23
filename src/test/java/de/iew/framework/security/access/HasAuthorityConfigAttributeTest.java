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

import static org.junit.Assert.assertEquals;

/**
 * JUnit test cases for the {@link HasAuthorityConfigAttribute} class.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 19.01.13 - 11:28
 */
public class HasAuthorityConfigAttributeTest {
    @Test
    public void testGetAttribute() throws Exception {
        // Testfix erstellen
        Authority authority = new Authority();
        authority.setSystemName("ROLE_JUNIT_TEST");

        WebResourceAccessRule rule = new WebResourceAccessRule();
        rule.setAuthority(authority);

        // Das Testobjekt erstellen
        HasAuthorityConfigAttribute configAttribute = new HasAuthorityConfigAttribute(rule);

        // Test und Auswertung
        assertEquals("ROLE_JUNIT_TEST", configAttribute.getAttribute());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() throws Exception {
        // Testfix erstellen

        // Das Testobjekt erstellen
        new HasAuthorityConfigAttribute(null);

        // Test und Auswertung
    }
}
