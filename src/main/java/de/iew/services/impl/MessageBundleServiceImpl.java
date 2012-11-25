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
import de.iew.domain.MessageBundleStore;
import de.iew.domain.TextItem;
import de.iew.framework.i18n.LocaleUtils;
import de.iew.persistence.MessageBundleDao;
import de.iew.services.MessageBundleService;
import de.iew.services.UnsupportedBasenameException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Implementiert einen Dienst für die Verwaltung von MessageBundles.
 * <p>
 * Unterstützt:
 * </p>
 * <ul>
 * <li>Verwaltung der Sprachinhalte in einem Datenbackend</li>
 * <li>Abgleichen von Sprachdateien (Properties-Dateien) in das Datenbackend</li>
 * <li>Implementiert {@link org.springframework.context.MessageSource} und kann daher als MessageSource verwendet werden</li>
 * <li>Optionales Caching der Sprachinhalte</li>
 * </ul>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 03:25
 */
public class MessageBundleServiceImpl extends AbstractMessageSource implements MessageBundleService, ResourceLoaderAware, InitializingBean {

    private static final Log log = LogFactory.getLog(MessageBundleServiceImpl.class);

    private Locale defaultLocale;

    private ResourceLoader resourceLoader;

    private String[] messageBundles = new String[0];

    private MessageBundleDao messageBundleDao;

    private String cacheName = "java.text.MessageFormat";

    private CacheManager cacheManager;

    private volatile List<Locale> supportedLocales;

    public boolean isSupportedBasename(String basename) {
        this.messageBundleDao.getSupportedLocales();
        if (basename == null) {
            return true;
        }
        for (String check : this.messageBundles) {
            if (check.indexOf(basename) != -1) {
                return true;
            }
        }

        return false;
    }

    /*
    Hier reicht synchronized auf diesem Objekt, da wir nix im Cache
    speichern sondern nur lokal als Objektvariable. Jedes Exemplar des
    MessageBundleCache hat somit auch ein eigenes Exemplar.
     */
    public synchronized List<Locale> getSupportedLocales() {
        if (this.supportedLocales == null) {
            this.supportedLocales = this.messageBundleDao.getSupportedLocales();
        }
        return this.supportedLocales;
    }

    public MessageBundleStore getMessageBundle(Locale locale, String basename) throws UnsupportedBasenameException {
        if (!isSupportedBasename(basename)) {
            throw new UnsupportedBasenameException("A MessageBundle with basename " + basename + " is not supported.");
        }
        MessageBundleStore messageBundleStore;

        synchronized (MessageBundleService.class) {
            messageBundleStore = getBundleFromCache(locale, basename);
            if (messageBundleStore == null) {
                if (log.isTraceEnabled()) {
                    log.trace("Cache MISS: MessageBundle für Cache Key " + messageBundleKey(locale, basename) + " nicht im Cache. Lade aus Datenbank!!!");
                }
                messageBundleStore = loadBundle(locale, basename);
                putBundleIntoCach(messageBundleStore, basename);
            }
        }

        return messageBundleStore;
    }

    public MessageBundleStore getDefaultMessageBundle(String basename) throws UnsupportedBasenameException {
        return getMessageBundle(this.defaultLocale, basename);
    }

    protected MessageBundleStore loadBundle(Locale locale, String basename) {
        List<MessageBundle> messageBundles;
        if (basename == null) {
            messageBundles = this.messageBundleDao.findByLocale(locale.getLanguage(), locale.getCountry());
        } else {
            messageBundles = this.messageBundleDao.findByLocaleAndBasename(locale.getLanguage(), locale.getCountry(), basename);
        }

        MessageBundleStore messageBundleStore = new MessageBundleStore(locale, basename);
        for (MessageBundle messageBundle : messageBundles) {
            messageBundleStore.put(messageBundle);
        }

        return messageBundleStore;
    }

    protected MessageBundleStore getBundleFromCache(Locale locale, String basename) {
        MessageBundleStore messageBundleStore = null;
        String cacheKey = messageBundleKey(locale, basename);

        Cache cache = this.cacheManager.getCache(this.cacheName);
        if (cache != null) {
            Cache.ValueWrapper value = cache.get(cacheKey);
            if (value != null) {
                if (log.isTraceEnabled()) {
                    log.trace("Cache HIT: MessageBundle für Cache Key " + cacheKey + " im Cache vorhanden.");
                }
                messageBundleStore = (MessageBundleStore) value.get();
            }
        }
        return messageBundleStore;
    }

    protected void putBundleIntoCach(MessageBundleStore messageBundleStore, String basename) {
        Cache cache = this.cacheManager.getCache(this.cacheName);
        if (cache != null) {
            String cacheKey = messageBundleKey(messageBundleStore.getLanguageCode(), messageBundleStore.getCountryCode(), basename);
            cache.put(cacheKey, messageBundleStore);
        }
    }

    protected String messageBundleKey(Locale locale, String basename) {
        return messageBundleKey(locale.getLanguage(), locale.getCountry(), basename);
    }

    protected String messageBundleKey(String languageCode, String countryCode, String basename) {
        if (basename == null || basename.length() == 0) {
            return languageCode + "_" + countryCode;
        } else {
            return basename + "_" + languageCode + "_" + countryCode;
        }
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        MessageFormat mf = null;
        try {
            MessageBundleStore messageBundleStore = getMessageBundle(locale, null);
            if (messageBundleStore == null || !messageBundleStore.contains(code)) {
                messageBundleStore = getDefaultMessageBundle(null);
            }

            String message = messageBundleStore.get(code);
            if (message != null) {
                mf = new MessageFormat(message);
            }
        } catch (UnsupportedBasenameException e) {
            if (log.isErrorEnabled()) {
                log.error("Fehler beim Auflösen des Message-Key " + code + ".", e);
            }
        }
        return mf;
    }

    public void synchronizeMessageBundles() throws IOException {
        for (String messageBundle : this.messageBundles) {
            if (!messageBundle.endsWith(".properties")) {
                messageBundle += ".properties";
            }
            loadMessageBundle(messageBundle);
        }
    }

    public void loadMessageBundle(String source) throws IOException {
        Resource resource = this.resourceLoader.getResource(source);

        String basename = resource.getFile().getName();
        if (basename.endsWith(".properties")) {
            basename = basename.substring(0, basename.length() - ".properties".length());
        }

        if (log.isDebugEnabled()) {
            log.debug("Basisname der zu importierenden Datei " + basename + ".");
        }

        Properties properties = new Properties();
        properties.load(resource.getInputStream());

        loadMessageBundle(properties, basename);
    }

    public void loadMessageBundle(Properties messageBundle, String bundleIdentifier) throws IOException {
        Locale locale = this.defaultLocale;


        String[] components = LocaleUtils.extractLocaleComponents(bundleIdentifier);
        if (components.length == 3) {
            bundleIdentifier = components[0];
            locale = new Locale(components[1], components[2]);
        }

        loadMessageBundle(messageBundle, bundleIdentifier, locale);
    }

    public void loadMessageBundle(Properties messageBundle, String basename, Locale locale) {
        if (log.isDebugEnabled()) {
            log.debug("Synchronisiere MessageBundle " + basename + " für Locale " + locale + ".");
        }

        MessageBundle mb;
        for (String messageKey : messageBundle.stringPropertyNames()) {
            if (log.isTraceEnabled()) {
                log.trace(messageKey + " -> " + messageBundle.getProperty(messageKey));
            }
            mb = this.messageBundleDao.findByTextKeyAndLocale(messageKey, locale.getLanguage(), locale.getCountry());
            if (mb == null) {
                TextItem textItem = new TextItem();
                textItem.setLanguageCode(locale.getLanguage());
                textItem.setCountryCode(locale.getCountry());
                textItem.setContent(messageBundle.getProperty(messageKey));

                mb = new MessageBundle();
                mb.setTextKey(messageKey);
                mb.setBasename(basename);
                mb.setTextItem(textItem);

                this.messageBundleDao.save(mb);
            }
        }
    }

    public void afterPropertiesSet() throws Exception {
        synchronizeMessageBundles();
    }

    public void setMessageBundles(String[] messageBundles) {
        this.messageBundles = messageBundles;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Value(value = "#{config['locale.default']}")
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Autowired
    public void setMessageBundleDao(MessageBundleDao messageBundleDao) {
        this.messageBundleDao = messageBundleDao;
    }

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
