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

package de.iew.services;

import de.iew.domain.ModelNotFoundException;
import de.iew.domain.Node;

import java.util.Collection;

/**
 * Beschreibt die Schnittstelle für einen Dienst zur Verwaltung von Baum-
 * Datenstrukturen.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 09:55
 */
public interface TreeService {

    /**
     * Fügt eine neue Wurzel mit dem angegebenen Titel in den angegebenen Baum
     * ein.
     *
     * @param title  Der Titel der Wurzel.
     * @param treeId Die Id des Baumes.
     * @return Die neu erstellte Wurzel.
     * @throws ModelNotFoundException Wenn das Baum-Domainmodell nicht
     *                                ermittelt wurde.
     */
    public Node prependNewTreeRootNode(String title, long treeId) throws ModelNotFoundException;

    /**
     * Fügt ein neues Kind mit dem angegebenen Vater hinzu. Das Kind wird an
     * die Kindes des Vatera angehängt.
     *
     * @param title    Der Knotentitel.
     * @param treeId   Die Id des Baums.
     * @param parentId Die Id des Vater-Knotens.
     * @return Der erstellte Knoten.
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Node appendNewNode(String title, long treeId, long parentId) throws ModelNotFoundException;

    /**
     * Fügt einen neuen Knoten als Bruder vor dem Knoten <code>nodeToInsertBefore</code>
     * im angegebenen Baum, ein.
     *
     * @param title              Der Knotentitel.
     * @param treeId             Die Id des Baums.
     * @param nodeToInsertBefore Die Id des Knotens vor dem eingefügt werden soll.
     * @return Der erstellte Knoten.
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Node insertNewNodeBefore(String title, long treeId, long nodeToInsertBefore) throws ModelNotFoundException;

    /**
     * Fügt einen neuen Knoten als Bruder nach dem Knoten <code>nodeToInsertAfter</code>
     * im angegebenen Baum, ein.
     *
     * @param title             Der Knotentitel.
     * @param treeId            Die Id des Baums.
     * @param nodeToInsertAfter Die Id des Knotens dem dem eingefügt werden soll.
     * @return Der erstellte Knoten.
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Node insertNewNodeAfter(String title, long treeId, long nodeToInsertAfter) throws ModelNotFoundException;

    /**
     * Löscht den gesamten Teilbaum ab dem angegebenen Knoten im angegebenen Baum.
     *
     * @param treeId Die Id des Baums.
     * @param nodeId Die Knoten-Id des zu löschenden Teilbaums.
     * @return Die Wurzel des gelöschten Teilbaums.
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Node deleteNodeAndSubtree(long treeId, long nodeId) throws ModelNotFoundException;

    /**
     * Liefert alle Knoten des angegebenen Baumes.
     *
     * @param treeId Die Id des Baums.
     * @return Die Knoten des Baumes.
     * @throws ModelNotFoundException Wenn der Baum nicht existiert.
     */
    public Collection<Node> getAllNodes(long treeId) throws ModelNotFoundException;

    /**
     * Liefert den Wurzelknoten für den angegebenen Baum.
     *
     * @param treeId Die Id des Baums.
     * @return Der Wurzelknoten.
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Node getTreeRootNode(long treeId) throws ModelNotFoundException;

    /**
     * Liefert die Kinder des Knotens <code>parentId</code> aus dem angegbenen
     * Baum.
     *
     * @param treeId   Die Id des Baums.
     * @param parentId Die Id des Knotens dessen Kinder geliefert werden sollen.
     * @return Die Liste der Kinder. Leere Liste wenn der Knoten keine Kinder
     *         hat (Also ein Blatt ist.
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Collection<Node> getDirectChildNodes(long treeId, long parentId) throws ModelNotFoundException;

    /**
     * Liefert den Knoten mit der angegebenen Id im angegebenen Baum.
     *
     * @param treeId Die Id des Baums.
     * @param nodeId Die Id des Knotens.
     * @return Der angeforderte Knoten des angegebenen Baums,
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Node getNodeByTreeAndId(long treeId, long nodeId) throws ModelNotFoundException;
}
