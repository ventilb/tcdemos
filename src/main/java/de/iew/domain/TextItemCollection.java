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

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/**
 * Beschreibt ein Domainmodell für die Verwaltung von Textschnipsel.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 03:10
 */
@Entity
@Table(name = "text_item_collection")
public class TextItemCollection extends AbstractModel implements Iterable<TextItem> {

    private Set<TextItem> textItems = new HashSet<TextItem>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "text_item_collection_id")
    public Set<TextItem> getTextItems() {
        return textItems;
    }

    public void setTextItems(Set<TextItem> textItems) {
        this.textItems = textItems;
    }

    // Helper Methoden ////////////////////////////////////////////////////////

    /**
     * Liefert eine Iterator-Sicht auf diese Sammlung an Textschnipsel.
     * <pre>
     * TextItemCollection tic = ....// Get Collection
     * for (TextItem textItem : tic) {
     *    // Do something with the text item
     * }
     * </pre>
     *
     * @return Iterator.
     */
    @Transient
    public Iterator<TextItem> iterator() {
        return this.textItems.iterator();
    }

    /**
     * Liefert eine Array-Sicht auf diese Textschnipsel-Sammlung.
     * <p>
     * Liefert ein leeres Array wenn diese Sammlung keine Textschnipsel
     * enthält.
     * </p>
     *
     * @return Array of {@link TextItem}.
     */
    @Transient
    public TextItem[] toArray() {
        Set<TextItem> textItems = getTextItems();
        return new TextItem[textItems.size()];
    }

    @Transient
    public TextItem lookupTextItemForLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("locale can't be null");
        }
        for (TextItem textItem : getTextItems()) {
            if (locale.getLanguage().equalsIgnoreCase(textItem.getLanguageCode())
                    && locale.getCountry().equalsIgnoreCase(textItem.getCountryCode())) {
                return textItem;
            }
        }
        return null;
    }
}
