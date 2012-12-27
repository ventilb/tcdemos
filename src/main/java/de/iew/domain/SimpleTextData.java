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

import de.iew.domain.annotations.DSTitleProperty;

import javax.persistence.*;

/**
 * Beschreibt ein einfaches Demomodell für die Verwaltung von
 * Textschnipsel als {@link DataSource}.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 11:05
 */
@Entity
@Table(name = "simple_text_data")
@PrimaryKeyJoinColumn(name = "id")
public class SimpleTextData extends DataSource {

    private TextItemCollection textItemCollection;

    @DSTitleProperty
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "text_item_collection_id")
    public TextItemCollection getTextItemCollection() {
        return textItemCollection;
    }

    public void setTextItemCollection(TextItemCollection textItemCollection) {
        this.textItemCollection = textItemCollection;
    }
}
