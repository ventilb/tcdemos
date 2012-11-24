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
import de.iew.domain.Tree;

import java.util.Collection;

/**
 * Beschreibt die Schnittstelle für den Zugriff auf
 * {@link Tree}-Domainmodelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 10:07
 */
public interface TreeDao extends DomainModelDao<Tree> {

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
