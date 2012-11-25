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
import java.util.Set;

/**
 * Beschreibt ein Domainmodell für die Verwaltung von Textschnipsel.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 03:10
 */
@Entity
@Table(name = "text_item_collection")
public class TextItemCollection extends AbstractModel {

    private Set<TextItem> textItems = new HashSet<TextItem>();

    @OneToMany(mappedBy = "textItemCollection", fetch = FetchType.EAGER)
    public Set<TextItem> getTextItems() {
        return textItems;
    }

    public void setTextItems(Set<TextItem> textItems) {
        this.textItems = textItems;
    }
}
