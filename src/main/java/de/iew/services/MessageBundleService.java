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

package de.iew.services;

import de.iew.domain.MessageBundleStore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Beschreibt die Schnittstelle für eine MessageBundle-Verwaltung.
 * <p>
 * MessageBundles sind sprachabhängige Schlüssel/Wert-Listen und werden für
 * die Interantionalisierung verwendet.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 03:25
 */
public interface MessageBundleService {

    /**
     * Liefert TRUE genau dann wenn der angegebene Basisname Messages
     * unterstützt wird, FALSE sonst.
     *
     * @param basename Der zu prüfende Basisname.
     * @return TRUE wenn der Basisname unterstützt wird.
     */
    public boolean isSupportedBasename(String basename);

    /**
     * Liefert die Liste der unterstützten Locales.
     * <p>
     * Das sind die Locales für die auch Messages ausgeliefert werden
     * können.
     * </p>
     *
     * @return Liste der unterstützten Locales. Leere Liste wenn nicht
     *         vorhanden.
     */
    public List<Locale> getSupportedLocales();

    /**
     * Liefert das MessageBundle für das angegebene {@link Locale}.
     *
     * @param locale   Das Locale.
     * @param basename Der Basisname.
     * @return Das MessageBundle für das Locale oder NULL wenn es nicht
     *         gefunden wurde.
     * @throws UnsupportedBasenameException Wenn der Basisname nicht
     *                                      unterstützt wird.
     */
    public MessageBundleStore getMessageBundle(Locale locale, String basename) throws UnsupportedBasenameException;

    /**
     * Liefert das MessageBundle für das Standard-Locale.
     *
     * @param basename Der Basisname.
     * @return Das MessageBundle für das Standard-Locale.
     * @throws UnsupportedBasenameException Wenn der Basisname nicht
     *                                      unterstützt wird.
     */
    public MessageBundleStore getDefaultMessageBundle(String basename) throws UnsupportedBasenameException;

    /**
     * Läd das MessageBundle aus den angegebenen Dati.
     * <p>
     * Der Basisname wird aus dem Dateinamen extrahiert. Versucht das {@link Locale}
     * aus dem Dateinamen zu extrahieren. Verwendet das Default-Locale wenn das nicht
     * möglich ist. Beispiele für Dateinamen sind:
     * </p>
     * <ul>
     * <li>classpath:i18n.messages</li>
     * <li>classpath:i18n.messages_de_DE</li>
     * <li>classpath:i18n.messages.properties</li>
     * <li>classpath:i18n.messages_de_DE.properties</li>
     * </ul>
     *
     * @param source Die Datei.
     * @throws IOException Wenn beim Laden ein Fehler auftrat.
     */
    public void loadMessageBundle(String source) throws IOException;

    /**
     * Läd das MessageBundle mit dem angegebenen Bezeichner.
     * <p>
     * Versucht das {@link Locale} aus dem angegebenen Bezeichner zu
     * extrahieren.  Verwendet das Default-Locale wenn das nicht
     * möglich ist. Beispiele für Bezeichner sind:
     * </p>
     * <ul>
     * <li>messages</li>
     * <li>messages_de_DE</li>
     * </ul>
     *
     * @param messageBundle    Das MessageBundle.
     * @param bundleIdentifier Der Bundlebezeichner.
     */
    public void loadMessageBundle(Properties messageBundle, String bundleIdentifier) throws IOException;
}
