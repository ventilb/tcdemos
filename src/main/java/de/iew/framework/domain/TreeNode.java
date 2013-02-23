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

package de.iew.framework.domain;

/**
 * Beschreibt allgemein die Schnittstelle für die Implementierung eines
 * Baumknotens.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 29.11.12 - 00:15
 */
public interface TreeNode {
    /**
     * Liefert den Baum, zu dem dieser Knoten gehört. Liefert NULL wenn dieser
     * Knoten zu keinem Baum gehört.
     *
     * @return Der Baum dieses Knotens.
     */
    public Tree getTree();

    /**
     * Konfiguriert den Baum dieses Knotens.
     *
     * @param tree Der Baum dieses Knotens. NULL wenn dieser Knoten zu
     *             keinem Baum gehört.
     */
    public void setTree(Tree tree);

    /**
     * Liefert das Datenobjekt, das mit diesem Knoten verknüpft ist.
     *
     * @return Das Datenobjekt zu diesem Knoten.
     */
    public DataSource getDataSource();

    /**
     * Konfiguriert das Datenobjekt für diesen Knoten.
     *
     * @param dataSource Das Datenobjekt für diesen Knoten.
     */
    public void setDataSource(DataSource dataSource);

}
