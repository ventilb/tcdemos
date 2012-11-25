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

package de.iew.domain;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Implementiert ein Domainmodell welches als Container für {@link MessageBundle}
 * Inhalte dient.
 * <p>
 * Dieses Modell ist selbst keine {@link javax.persistence.Entity}. Dieses
 * Modell soll das Cachen ganzer {@link MessageBundle}-Sets ermöglichen.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 18:07
 */
public class MessageBundleStore implements Serializable {

    private final Hashtable<String, String> messages = new Hashtable<String, String>();

    private final String languageCode;

    private final String countryCode;

    private final String basename;

    public MessageBundleStore(Locale locale) {
        this(locale, null);
    }

    public MessageBundleStore(Locale locale, String basename) {
        this(locale.getLanguage(), locale.getCountry(), basename);
    }

    public MessageBundleStore(String languageCode, String countryCode, String basename) {
        if (languageCode == null) {
            throw new IllegalArgumentException("languageCode can't be null");
        }
        if (countryCode == null) {
            throw new IllegalArgumentException("countryCode can't be null");
        }
        this.languageCode = languageCode;
        this.countryCode = countryCode;
        this.basename = basename;
    }

    public void put(MessageBundle messageBundle) {
        if (messageBundle != null) {
            TextItem textItem = messageBundle.getTextItem();

            if (!this.languageCode.equals(textItem.getLanguageCode()) || !this.countryCode.equals(textItem.getCountryCode())) {
                throw new IllegalArgumentException("Language- and/or Country-Code mismatch");
            }

            this.messages.put(messageBundle.getTextKey(), textItem.getContent());
        }
    }

    public boolean contains(String key) {
        return this.messages.containsKey(key);
    }

    public String get(String key) {
        return this.messages.get(key);
    }

    public Hashtable<String, String> getMessages() {
        return this.messages;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getBasename() {
        return basename;
    }
}
