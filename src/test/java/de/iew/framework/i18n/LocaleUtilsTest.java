/*
 * Copyright 2012 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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

package de.iew.framework.i18n;

import org.ietf.jgss.Oid;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.*;

/**
 * Testfälle für {@link LocaleUtils}.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 13:48
 */
public class LocaleUtilsTest {
    @Test
    public void testIsSupportedLanguage() throws Exception {

    }

    @Test
    public void testIsSupportedCountry() throws Exception {

    }

    @Test
    public void testExtractBasename() throws Exception {
        assertNull(LocaleUtils.extractBasename(null));
        assertEquals("messages", LocaleUtils.extractBasename("messages"));
        assertEquals("messages", LocaleUtils.extractBasename("messages_de_DE"));
        assertEquals("messages_my_test", LocaleUtils.extractBasename("messages_my_test_de_DE"));
        assertEquals("messages_my_testde_DE", LocaleUtils.extractBasename("messages_my_testde_DE"));
    }

    @Test
    public void testExtractLocaleComponents() {
        String[] comps;

        comps = LocaleUtils.extractLocaleComponents(null);
        assertNull(comps);
        comps = LocaleUtils.extractLocaleComponents("messages");
        assertTrue(Arrays.equals(new String[]{"messages"}, comps));

        comps = LocaleUtils.extractLocaleComponents("messages_de_DE");
        assertTrue(Arrays.equals(new String[]{"messages", "de", "DE"}, comps));

        comps = LocaleUtils.extractLocaleComponents("messages_my_test_en_US");
        assertTrue(Arrays.equals(new String[]{"messages_my_test", "en", "US"}, comps));

        comps = LocaleUtils.extractLocaleComponents("messages_my_testde_DE");
        assertTrue(Arrays.equals(new String[]{"messages_my_testde_DE"}, comps));
    }
}

