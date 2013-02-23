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

package de.iew.framework.persistence;

import de.iew.framework.domain.Node;

/**
 * Beschreibt eine DAO-Schnittstelle mit Operationen auf Bäume.
 * <p>
 * Wir trennen die DAOs für die Domainmodelle (Knoten, Baum) von den
 * Operationen auf Knoten und Bäume, da die Operationen übergreifend
 * sind. Damit verhindern wir den Effekt, dass Methoden auf die DAOs der
 * Domainmodelle verstreut werden und versuchen damit die Redundanz in den
 * Funktionalitäten zu beschränken.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 10:07
 */
public interface TreeOperationDao {

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

    /**
     * Löscht den angegebenen Knoten im angegebenen Baum.
     * <p>
     * Hat keinen Effekt wenn der Knoten nicht existiert.
     * </p>
     *
     * @param treeId Die Id des Baumes.
     * @param nodeId Die Id des zu löschenden Knotens.
     */
    public void deleteSingleNode(long treeId, long nodeId);

    public void deleteNodesBetween(long treeId, long leftNestedSetIndex, long rightNestedSetIndex);

    public Node save(Node node);

    /**
     * Liefert die Wurzel für den angegebenen Baum.
     *
     * @param treeId Die Id des Baums.
     * @return Der angeforderte Knoten oder NULL wenn der Knoten nicht
     *         ermittelt werden kann.
     */
    public Node findRootNodeForTree(long treeId);

    /**
     * Liefert den Knoten mit der angegebenen Id für den angegebenen Baum.
     *
     * @param treeId Die Id des Baums.
     * @param nodeId Die Id des angeforderten Knotens.
     * @return Der angeforderte Knoten oder NULL wenn der Knoten nicht
     *         ermittelt werden kann.
     */
    public Node findNodeForTreeAndId(long treeId, long nodeId);
}
