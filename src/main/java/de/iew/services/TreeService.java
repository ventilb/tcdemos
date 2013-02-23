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

import de.iew.framework.domain.ModelNotFoundException;
import de.iew.framework.domain.Node;

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
     * @param treeId Die Id des Baumes.
     * @return Die neu erstellte Wurzel.
     * @throws ModelNotFoundException Wenn das Baum-Domainmodell nicht
     *                                ermittelt wurde.
     */
    public Node migrateRootNode(long treeId) throws ModelNotFoundException;

    /**
     * Fügt ein neues Kind mit dem angegebenen Vater hinzu. Das Kind wird an
     * die Kinder des Vaters angehängt.
     *
     * @param treeId       Die Id des Baums.
     * @param parentId     Die Id des Vater-Knotens.
     * @param dataSourceId Die Id der {@link de.iew.framework.domain.DataSource},
     *                     die mit dem Knoten verknüpft werden soll.
     * @return Der erstellte Knoten.
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Node appendNewNode(long treeId, long parentId, long dataSourceId) throws ModelNotFoundException;

    /**
     * Fügt einen neuen Knoten als Bruder vor dem Knoten <code>nodeToInsertBefore</code>
     * im angegebenen Baum, ein.
     *
     * @param treeId             Die Id des Baums.
     * @param nodeToInsertBefore Die Id des Knotens vor dem eingefügt werden soll.
     * @param dataSourceId       Die Id der {@link de.iew.framework.domain.DataSource},
     *                           die mit dem Knoten verknüpft werden soll.
     * @return Der erstellte Knoten.
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Node insertNewNodeBefore(long treeId, long nodeToInsertBefore, long dataSourceId) throws ModelNotFoundException;

    /**
     * Fügt einen neuen Knoten als Bruder nach dem Knoten <code>nodeToInsertAfter</code>
     * im angegebenen Baum, ein.
     *
     * @param treeId            Die Id des Baums.
     * @param nodeToInsertAfter Die Id des Knotens dem dem eingefügt werden soll.
     * @param dataSourceId      Die Id der {@link de.iew.framework.domain.DataSource},
     *                          die mit dem Knoten verknüpft werden soll.
     * @return Der erstellte Knoten.
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Node insertNewNodeAfter(long treeId, long nodeToInsertAfter, long dataSourceId) throws ModelNotFoundException;

    /**
     * Löscht den angegebenen Knoten im angegebenen Baum.
     * <p>
     * Es wird nur der Knoten gelöscht. Die Kinder dieses Knotens werden an der
     * Stelle an der, der zu löschende Knoten stand, eingefügt.
     * </p>
     *
     * @param treeId Die Id des Baums.
     * @param nodeId Die Knoten-Id des zu löschenden Knotens.
     * @return Der gelöschte Knoten.
     * @throws ModelNotFoundException Wenn der Knoten nicht im Baum existiert.
     */
    public Node deleteNodeAndMigrateChildren(long treeId, long nodeId) throws ModelNotFoundException;

    /**
     * Löscht den gesamten Teilbaum ab dem angegebenen Knoten im angegebenen Baum.
     * <p>
     * Der Teilbaum wird inklusive des angegebenen Wurzelknotens gelöscht. Die
     * gelöschte Wurzel wird zurückgegeben. Die Id des zurückgegebenen Knotens
     * wird nicht auf NULL gesetzt.
     * </p>
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
