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

package de.iew.persistence;

import de.iew.domain.MessageBundle;

import java.util.List;
import java.util.Locale;

/**
 * Beschreibt die DAO-Schnittstelle für das MessageBundle-Datenbackend.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 12:39
 */
public interface MessageBundleDao extends DomainModelDao<MessageBundle> {

    /**
     * Liefert den {@link MessageBundle}-Eintrag für den angegebenen Key.
     *
     * @param textKey      Der Schlüssel für den Eintrag.
     * @param languageCode Der Sprach-Code.
     * @param countryCode  Der Länder-Code.
     * @return Den {@link MessageBundle}-Eintrag zum angegebenen Key oder NULL
     *         wenn nicht vorhanden.
     */
    public MessageBundle findByTextKeyAndLocale(String textKey, String languageCode, String countryCode);

    /**
     * Liefert die MessageBundle-Einträge für die angegebene Sprache.
     *
     * @param languageCode Der Sprach-Code.
     * @param countryCode  Der Länder-Code.
     * @return Liste der MessageBundle-Einträge.
     */
    public List<MessageBundle> findByLocale(String languageCode, String countryCode);

    /**
     * Liefert die MessageBundle-Einträge für die angegebene Sprache für den
     * angegebenen Basisnamen.
     *
     * @param languageCode Der Sprach-Code.
     * @param countryCode  Der Länder-Code.
     * @param basename     Der Basisname. Darf NULL sein.
     * @return Liste der MessageBundle-Einträge.
     */
    public List<MessageBundle> findByLocaleAndBasename(String languageCode, String countryCode, String basename);

    /**
     * Liefert die Liste der unterstützten Locales.
     *
     * @return Lister der unterstützten Locales. Leere Liste wenn nicht
     *         vorhanden.
     */
    public List<Locale> getSupportedLocales();
}
