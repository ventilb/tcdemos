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

import de.iew.services.MessageBundleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static junit.framework.Assert.*;

/**
 * Integrationstest zum Überprüfen der MessageSource-Integration der
 * {@link MessageBundleServiceImpl}-Implementierung.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 15:33
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml")
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional // Wichtig, sonst haben wir hier keine Transaktion und die Testdaten werden nicht zurück gerollt.
public class MessageSourceServiceIntegrationTest {
    private static final Log log = LogFactory.getLog(MessageSourceServiceIntegrationTest.class);

    @Autowired
    private MessageBundleService messageBundleService;

    @Autowired
    private MessageSource messageSource;


    @Value(value = "#{config['locale.default']}")
    private Locale defaultLocale;

    @Before
    public void setUp() throws Exception {
        String file = "classpath:text_item_test_en_US.properties";
        this.messageBundleService.loadMessageBundle(file);

        file = "classpath:text_item_test.properties";
        this.messageBundleService.loadMessageBundle(file);
    }

    @Test
    public void testResolveCode() {
        if (log.isDebugEnabled()) {
            log.debug("===== Starte MessageSourceServiceIntegrationTest Testcase =====");
        }

        // Testfix erstellen
        Locale enUs = new Locale("en", "US");

        // Test
        assertEquals("Sprachschl\u00fcssel 1", this.messageSource.getMessage("msg.key1", null, this.defaultLocale));
        assertEquals("Sprachschl\u00fcssel 1", this.messageSource.getMessage("msg.key1", null, this.defaultLocale));
        assertEquals("Sprachschl\u00fcssel 1", this.messageSource.getMessage("msg.key1", null, this.defaultLocale));
        assertEquals("Language Key 1", this.messageSource.getMessage("msg.key1", null, enUs));
        assertEquals("Sprachschl\u00fcssel 1", this.messageSource.getMessage("msg.key1", null, this.defaultLocale));
        assertEquals("Language Key 1", this.messageSource.getMessage("msg.key1", null, enUs));
        assertEquals("Sprachschl\u00fcssel 1", this.messageSource.getMessage("msg.key1", null, this.defaultLocale));

        assertEquals("Mein Name ist Max Mustermann.", this.messageSource.getMessage("msg.key3", new String[]{"Max Mustermann"}, enUs));
        assertEquals("Mein Name ist Max Mustermann.", this.messageSource.getMessage("msg.key3", new String[]{"Max Mustermann"}, this.defaultLocale));

        if (log.isDebugEnabled()) {
            log.debug("===== Beende MessageSourceServiceIntegrationTest Testcase =====");
        }
    }

}
