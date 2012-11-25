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

package de.iew.services.impl;

import de.iew.domain.MessageBundle;
import de.iew.persistence.MessageBundleDao;
import de.iew.services.MessageBundleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Properties;

import static junit.framework.Assert.*;


/**
 * Integrationstest zum Überprüfen der Synchronisierungsmethoden des
 * {@link MessageBundleService}.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 03:52
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml")
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional // Wichtig, sonst haben wir hier keine Transaktion und die Testdaten werden nicht zurück gerollt.
public class MessageBundleServiceIntegrationTest {

    @Autowired
    private MessageBundleService messageBundleService;

    @Autowired
    private MessageBundleDao messageBundleDao;

    @Value(value = "#{config['locale.default']}")
    private Locale defaultLocale;

    @Test
    public void testSynchronizeMessageBundle1() throws Exception {
        // Testfix erstellen
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/text_item_test.properties"));

        // Test
        this.messageBundleService.loadMessageBundle(properties, "text_item_test");

        // Auswertung
        MessageBundle mb = this.messageBundleDao.findByTextKeyAndLocale("msg.key1", this.defaultLocale.getLanguage(), this.defaultLocale.getCountry());
        assertNotNull(mb);
        assertEquals("text_item_test", mb.getBasename());
        assertEquals("msg.key1", mb.getTextKey());
        assertEquals("Sprachschlüssel 1", mb.getTextItem().getContent());
        assertEquals(this.defaultLocale.getLanguage(), mb.getTextItem().getLanguageCode());
        assertEquals(this.defaultLocale.getCountry(), mb.getTextItem().getCountryCode());

        mb = this.messageBundleDao.findByTextKeyAndLocale("msg.key2", this.defaultLocale.getLanguage(), this.defaultLocale.getCountry());
        assertNotNull(mb);
        assertEquals("text_item_test", mb.getBasename());
        assertEquals("msg.key2", mb.getTextKey());
        assertEquals("Sprachschlüssel 2", mb.getTextItem().getContent());
        assertEquals(this.defaultLocale.getLanguage(), mb.getTextItem().getLanguageCode());
        assertEquals(this.defaultLocale.getCountry(), mb.getTextItem().getCountryCode());

        mb = this.messageBundleDao.findByTextKeyAndLocale("msg.a_text_with_newlines.hint", this.defaultLocale.getLanguage(), this.defaultLocale.getCountry());
        assertNotNull(mb);
        assertEquals("text_item_test", mb.getBasename());
        assertEquals("msg.a_text_with_newlines.hint", mb.getTextKey());
        assertEquals("Das ist ein langer Text mit Newlines.\nKann das sein, dass das funktioniert?\nOder wie?\nDer Unit Test wird es zeigen.", mb.getTextItem().getContent());
        assertEquals(this.defaultLocale.getLanguage(), mb.getTextItem().getLanguageCode());
        assertEquals(this.defaultLocale.getCountry(), mb.getTextItem().getCountryCode());
    }

    @Test
    public void testSynchronizeMessageBundle2() throws Exception {
        // Testfix erstellen
        String file = "classpath:text_item_test.properties";

        // Test
        this.messageBundleService.loadMessageBundle(file);

        // Auswertung
        MessageBundle mb = this.messageBundleDao.findByTextKeyAndLocale("msg.key1", this.defaultLocale.getLanguage(), this.defaultLocale.getCountry());
        assertNotNull(mb);
        assertEquals("text_item_test", mb.getBasename());
        assertEquals("msg.key1", mb.getTextKey());
        assertEquals("Sprachschlüssel 1", mb.getTextItem().getContent());
        assertEquals(this.defaultLocale.getLanguage(), mb.getTextItem().getLanguageCode());
        assertEquals(this.defaultLocale.getCountry(), mb.getTextItem().getCountryCode());

        mb = this.messageBundleDao.findByTextKeyAndLocale("msg.key2", this.defaultLocale.getLanguage(), this.defaultLocale.getCountry());
        assertNotNull(mb);
        assertEquals("text_item_test", mb.getBasename());
        assertEquals("msg.key2", mb.getTextKey());
        assertEquals("Sprachschlüssel 2", mb.getTextItem().getContent());
        assertEquals(this.defaultLocale.getLanguage(), mb.getTextItem().getLanguageCode());
        assertEquals(this.defaultLocale.getCountry(), mb.getTextItem().getCountryCode());
    }

    @Test
    public void testSynchronizeMessageBundle3() throws Exception {
        // Testfix erstellen
        String file = "classpath:text_item_test_en_US.properties";

        // Test
        this.messageBundleService.loadMessageBundle(file);

        // Auswertung
        MessageBundle mb = this.messageBundleDao.findByTextKeyAndLocale("msg.key1", "en", "US");
        assertNotNull(mb);
        assertEquals("text_item_test", mb.getBasename());
        assertEquals("msg.key1", mb.getTextKey());
        assertEquals("Language Key 1", mb.getTextItem().getContent());
        assertEquals("en", mb.getTextItem().getLanguageCode());
        assertEquals("US", mb.getTextItem().getCountryCode());

        mb = this.messageBundleDao.findByTextKeyAndLocale("msg.key2", "en", "US");
        assertNotNull(mb);
        assertEquals("text_item_test", mb.getBasename());
        assertEquals("msg.key2", mb.getTextKey());
        assertEquals("Language Key 2", mb.getTextItem().getContent());
        assertEquals("en", mb.getTextItem().getLanguageCode());
        assertEquals("US", mb.getTextItem().getCountryCode());
    }
}
