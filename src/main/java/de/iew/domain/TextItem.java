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

/**
 * Beschreibt ein Domainmodell für die Verwaltung eines Textschnipsels.
 * <p>
 * Dieses Domainmodell wird von anderen Modellen verwendet um sprachabhängige
 * Inhalte für das Modell zu verwalten. Zum Beispiel können hiermit Knotentitel
 * übersetzt werden.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 03:05
 */
@Entity
@Table(name = "text_item")
public class TextItem extends AbstractModel {

    private String languageCode;

    private String countryCode;

    private String content;

    private TextItemCollection textItemCollection;

    @Column(length = 5, nullable = false, name = "language_code")
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Column(length = 5, nullable = false, name = "country_code")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Lob
    @Column(nullable = false, name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_item_collection_id", nullable = true)
    public TextItemCollection getTextItemCollection() {
        return textItemCollection;
    }

    public void setTextItemCollection(TextItemCollection textItemCollection) {
        this.textItemCollection = textItemCollection;
    }
}
