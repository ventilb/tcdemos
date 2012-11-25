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
 * Beschreibt ein Domainmodell für die Verwaltung von MessageBundles.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 12:24
 */
@Entity
@Table(name = "message_bundle")
public class MessageBundle extends AbstractModel {

    private String textKey;

    private String basename;

    private TextItem textItem;

    @Column(length = 255, nullable = false, name = "text_key")
    public String getTextKey() {
        return textKey;
    }

    public void setTextKey(String textKey) {
        this.textKey = textKey;
    }

    @Column(length = 255, nullable = true, name = "basename")
    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    /**
     * Das {@link TextItem} zu diesem MessageBundle.
     * <p>
     * Diese Beziehung ist als {@link FetchType#EAGER} deklariert, da ein
     * MessageBundle ohne TextItem nicht viel Sinn macht.
     * </p>
     *
     * @return Das TextItem zu diesem MessageBundle.
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "text_item_id", nullable = false)
    public TextItem getTextItem() {
        return textItem;
    }

    public void setTextItem(TextItem textItem) {
        this.textItem = textItem;
    }
}
