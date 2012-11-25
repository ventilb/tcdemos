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

import java.util.Locale;

/**
 * Hilfsfunktionen für den Umgang mit Sprachen, Ländern und {@link Locale}s.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 13:31
 */
public class LocaleUtils {

    /**
     * Prüft ob der angegebene Language-Code von {@link Locale} unterstützt
     * wird.
     *
     * @param languageToCheck Der zu prüfende Language-Code.
     * @return TRUE wenn des Language-Code unterstützt wird, FALSE sonst.
     */
    public static boolean isSupportedLanguage(String languageToCheck) {
        if (languageToCheck == null) {
            return false;
        }
        String[] languages = Locale.getISOLanguages();
        for (String languageToTest : languages) {
            if (languageToCheck.equalsIgnoreCase(languageToTest)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prüft ob der angegebene Länder-Code von {@link Locale} unterstützt
     * wird.
     *
     * @param countryToCheck Der zu prüfende Länder-Code.
     * @return TRUE wenn des Länder-Code unterstützt wird, FALSE sonst.
     */
    public static boolean isSupportedCountry(String countryToCheck) {
        if (countryToCheck == null) {
            return false;
        }
        String[] countries = Locale.getISOCountries();
        for (String countryToTest : countries) {
            if (countryToCheck.equalsIgnoreCase(countryToTest)) {
                return true;
            }
        }
        return false;
    }

    public static String extractBasename(String name) {
        if (name == null) {
            return null;
        }

        return extractLocaleComponents(name)[0];
    }

    /**
     * Extrahiert, falls vorhanden den Basisnamen und Sprach- sowie Ländercode
     * aus dem angegebenen Namen. Beispiele:
     * <ul>
     * <li>messages_de_DE wird zu [messages, de, DE]</li>
     * <li>messages wird zu [messages]</li>
     * <li>messages_test_de_DE wird zu [messages_test, de, DE]</li>
     * <li>messagesde_DE wird zu [messagesde_DE]</li>
     * </ul>
     *
     * @param name Der zu zerlegene Name.
     * @return Liste der Komponenten.
     */
    public static String[] extractLocaleComponents(String name) {
        if (name == null) {
            return null;
        }
        String basename = name;

        String[] comps;

        String[] fileParts = basename.split("_");
        if (fileParts.length >= 3
                && LocaleUtils.isSupportedLanguage(fileParts[fileParts.length - 2])
                && LocaleUtils.isSupportedCountry(fileParts[fileParts.length - 1])) {
            String suffix = fileParts[fileParts.length - 2] + "_" + fileParts[fileParts.length - 1];

            basename = basename.substring(0, basename.length() - suffix.length() - 1);

            comps = new String[]{
                    basename,
                    fileParts[fileParts.length - 2],
                    fileParts[fileParts.length - 1]
            };

        } else {
            comps = new String[]{
                    basename
            };
        }

        return comps;
    }
}
