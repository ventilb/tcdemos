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

package de.iew.framework.utils;

import de.iew.framework.domain.TextItem;
import de.iew.framework.domain.TextItemCollection;

import java.util.Locale;

/**
 * {@inheritDoc}
 * <p>
 * Implementiert einen {@link StringResolver}, der einen Titel zu einem
 * gegebenen {@link Locale} auswerten kann.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 12:03
 */
public class LocaleStringResolver implements StringResolver {

    private Locale locale;

    /**
     * {@inheritDoc}
     * <p>
     * Unterstützt speziell {@link TextItemCollection} Modelle für {@link Locale}
     * abhängige Titel. Für alle anderen Modelle wird {@link Object#toString()}
     * verwendet.
     * </p>
     */
    public String resolveString(Object resolveStringObject) {
        if (resolveStringObject instanceof TextItemCollection) {
            TextItemCollection textItemCollection = (TextItemCollection) resolveStringObject;
            return resolveTitle(textItemCollection);
        }
        return resolveStringObject.toString();
    }

    public String resolveTitle(TextItemCollection textItemCollection) {
        Locale locale = localeToResolve();

        TextItem textItem = textItemCollection.lookupTextItemForLocale(locale);
        return textItem.getContent();
    }

    protected Locale localeToResolve() {
        Locale locale = this.locale;
        if (this.locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
