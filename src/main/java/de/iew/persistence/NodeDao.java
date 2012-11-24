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

package de.iew.persistence;

import de.iew.domain.Node;

/**
 * Beschreibt eine Schnittstelle für den Zugriff auf
 * {@link Node}-Domainmodelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 10:06
 */
public interface NodeDao extends DomainModelDao<Node> {
    /**
     * Verschiebt die NestedSet-Grenzen aller Knoten im angegebenen Baum um
     * 1.
     *
     * @param treeId Die Id des Baumes.
     */
    public void incNestedSetBorders(long treeId);

    /**
     * Verschiebt die NestedSet-Grenzen um 2 aller Knoten im angegebenen
     * Baum, die größer sind als <code>fromNestedSetIndex</code>.
     *
     * @param treeId             Die Id des Baumes.
     * @param fromNestedSetIndex Die NestedSet-Grenze aber der verschoben
     *                           wird.
     */
    public void moveNestedSetBorder(long treeId, long fromNestedSetIndex);

    public void deleteNodesBetween(long treeId, long leftNestedSetIndex, long rightNestedSetIndex);
}
