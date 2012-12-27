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

package de.iew.services.impl;

import de.iew.domain.*;
import de.iew.persistence.DataSourceDao;
import de.iew.persistence.NodeDao;
import de.iew.persistence.TreeDao;
import de.iew.persistence.TreeOperationDao;
import de.iew.services.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implementiert einen Knotendienst. Die Knoten werden sowohl in einer
 * Vater-Kind-Beziehung (Adjazenzlisten) als auch im NestedSets-Verfahren
 * organisiert.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://www.klempert.de/nested_sets/">http://www.klempert.de/nested_sets/</a>
 * @since 17.11.12 - 09:56
 */
@Service(value = "nodeService")
public class TreeServiceImpl implements TreeService {

    private TreeOperationDao treeOperationDao;

    private TreeDao treeDao;

    private NodeDao nodeDao;

    private DataSourceDao dataSourceDao;

    public Tree getTreeByLookupKey(String lookupKey) throws ModelNotFoundException {
        Tree tree = this.treeDao.findTreeByLookupKey(lookupKey);

        if (tree == null) {
            throw new ModelNotFoundException("The requested tree " + lookupKey + " was not found.");
        }
        return tree;
    }

    public Node migrateRootNode(long treeId) throws ModelNotFoundException {
        Tree tree = getTreeById(treeId);

        long nestedSetLeft = 1;
        long nestedSetRight = 2;

        Node newNode = new Node();
        newNode = this.nodeDao.save(newNode);

        newNode.setTree(tree);
        tree.getNodes().add(newNode);


        Node oldRoot = tree.getRoot();
        if (oldRoot != null) {
            nestedSetRight = oldRoot.getNestedSetRight() + 2;

            this.treeOperationDao.incNestedSetBorders(tree.getId());

            oldRoot.setParent(newNode);
            newNode.getChildren().add(oldRoot);
        }

        tree.setRoot(newNode);

        newNode.setNestedSetLeft(nestedSetLeft);
        newNode.setNestedSetRight(nestedSetRight);

        return newNode;
    }

    public Node appendNewNode(long treeId, long parentId, long dataSourceId) throws ModelNotFoundException {
        return appendNewNodeAt(treeId, parentId, dataSourceId, Integer.MAX_VALUE);
    }

    public Node appendNewNodeAt(long treeId, long parentId, long dataSourceId, int orderToAppendAt) throws ModelNotFoundException {
        Node parent = getNodeByTreeAndId(treeId, parentId);
        Tree tree = parent.getTree();
        List<Node> children = parent.getChildren();

        orderToAppendAt = Math.min(orderToAppendAt, children.size());

        long nestedSetRightToShiftFrom;

        if (orderToAppendAt <= 0) {
            nestedSetRightToShiftFrom = parent.getNestedSetLeft();
        } else {
            nestedSetRightToShiftFrom = children.get(orderToAppendAt - 1).getNestedSetRight();
        }

        for (Node child : children) {
            if (child.getOrdinalNumber() >= orderToAppendAt) {
                child.setOrdinalNumber(child.getOrdinalNumber() + 1);
            }
        }

        Node newNode = new Node();
        newNode.setOrdinalNumber(orderToAppendAt);
        newNode.setNestedSetLeft(nestedSetRightToShiftFrom + 1);
        newNode.setNestedSetRight(nestedSetRightToShiftFrom + 2);

        newNode.setTree(tree);
        tree.getNodes().add(newNode);

        newNode.setParent(parent);
        parent.getChildren().add(orderToAppendAt, newNode);

        newNode = this.treeOperationDao.save(newNode);

        DataSource dataSource = this.dataSourceDao.findById(dataSourceId);
        newNode.setDataSource(dataSource);

        return newNode;
    }

    public Node insertNewNodeBefore(long treeId, long nodeToInsertBefore, long dataSourceId) throws ModelNotFoundException {
        Node sibling = getNodeByTreeAndId(treeId, nodeToInsertBefore);

        return appendNewNodeAt(treeId, sibling.getParent().getId(), dataSourceId, sibling.getOrdinalNumber());
    }

    public Node insertNewNodeAfter(long treeId, long nodeToInsertAfter, long dataSourceId) throws ModelNotFoundException {
        Node sibling = getNodeByTreeAndId(treeId, nodeToInsertAfter);

        return appendNewNodeAt(treeId, sibling.getParent().getId(), dataSourceId, sibling.getOrdinalNumber() + 1);
    }

    public Node deleteNodeAndMigrateChildren(long treeId, long nodeId) throws ModelNotFoundException {
        Node deletedNode = getNodeByTreeAndId(treeId, nodeId);
        Node parent = deletedNode.getParent();

        migrateMyChildrenToParent(deletedNode);
        removeMeFromParent(deletedNode);
        reorderNodeList(parent.getChildren());

        this.treeOperationDao.deleteSingleNode(treeId, nodeId);

        return deletedNode;
    }

    public Node deleteNodeAndSubtree(long treeId, long nodeId) throws ModelNotFoundException {
        Node deletedNode = getNodeByTreeAndId(treeId, nodeId);

        removeMeFromParent(deletedNode);

        Node parent = deletedNode.getParent();
        if (parent != null) {
            List<Node> children = parent.getChildren();

            reorderNodeList(children);
        }

        this.treeOperationDao.deleteNodesBetween(treeId, deletedNode.getNestedSetLeft(), deletedNode.getNestedSetRight());

        return deletedNode;
    }

    public Collection<Node> getAllNodes(long treeId) throws ModelNotFoundException {
        Tree tree = getTreeById(treeId);

        return tree.getNodes();
    }

    public Node getTreeRootNode(long treeId) throws ModelNotFoundException {
        Node rootNode;

        rootNode = this.treeOperationDao.findRootNodeForTree(treeId);

        if (rootNode == null) {
            throw new ModelNotFoundException("The root node for the requested tree " + treeId + " is not available.");
        }

        return rootNode;
    }

    public Collection<Node> getDirectChildNodes(long treeId, long parentId) throws ModelNotFoundException {
        Node parentNode = getNodeByTreeAndId(treeId, parentId);

        Collection<Node> nodes = parentNode.getChildren();

        return nodes;
    }

    public Tree getTreeById(long treeId) throws ModelNotFoundException {
        Tree tree = this.treeDao.findById(treeId);

        if (tree == null) {
            throw new ModelNotFoundException("The tree " + treeId + " was not found.");
        }

        return tree;
    }

    public Node getNodeByTreeAndId(long treeId, long nodeId) throws ModelNotFoundException {
        Node node = this.treeOperationDao.findNodeForTreeAndId(treeId, nodeId);

        if (node == null) {
            throw new ModelNotFoundException("The node " + nodeId + " was not found for the requested tree " + treeId + ".");
        }

        return node;
    }

    // Hilfsmethoden //////////////////////////////////////////////////////////

    /**
     * Hilfsmethode: Migriert die Kinder des angegebenen Knotens zum Vater des
     * angegebenen Knotens.
     * <p>
     * Fügt die Kinder an die Stelle des angegebenen Knotens, fortlaufend ein.
     * Die Geschwister des angegebenen Knotens folgen dann nach den Kindern des
     * Knotens. Die Ordnungszahlen werden entsprechend angepasst, sodass die
     * Reihenfolge erhalten bleibt.
     * Korrigiert nicht die Ordnungszahlen der Geschwister des Knotens sodass
     * möglicherweise trotzdem Lücken in den Ordnungszahlen vorhanden sein
     * können.
     * </p>
     *
     * @param me Der Knoten.
     */
    public void migrateMyChildrenToParent(Node me) {
        int myOrdinalNumber = me.getOrdinalNumber();

        Node parent = me.getParent();
        if (parent != null) {
            List<Node> myChildren = me.getChildren();
            int myChildrenCount = myChildren.size();

            // Verschiebe die Ordnungszahlen der Knoten nach mir um für meine
            // Kinder Platz zu schaffen.
            // TODO: Daraus lässt sich eine eigene Methode machen.
            List<Node> mySiblingsAndMe = parent.getChildren();
            Collections.sort(mySiblingsAndMe, Order.ASCENDING);
            for (int i = mySiblingsAndMe.size() - 1; i >= 0; i--) {
                Node siblingOrMe = mySiblingsAndMe.get(i);
                if (siblingOrMe.getOrdinalNumber() > myOrdinalNumber) {
                    siblingOrMe.setOrdinalNumber(siblingOrMe.getOrdinalNumber() + myChildrenCount);
                }
            }

            Node[] subTreeNodes = myChildren.toArray(new Node[myChildrenCount]);

            // Wichtig, sonst werden die Kinder evtl. in der falschen
            // Reihenfolge eingefügt
            Arrays.sort(subTreeNodes, Order.ASCENDING);

            for (Node node : subTreeNodes) {
                // Hiermit stellen wir sicher, dass die Knoten nach mir
                // eingefügt werden.
                myOrdinalNumber++;

                // Wichtig, sonst würden die Knoten beim nachträglichen
                // Korrigeren der Ordnungszahlen falsch sortiert werden.
                node.setOrdinalNumber(myOrdinalNumber);
                myChildren.remove(node);

                node.setParent(parent);
                parent.getChildren().add(myOrdinalNumber, node);
            }
        }
    }

    /**
     * Hilfsmethode: Korrigiert die Ordinalzahlen der Kinder des Vaters des
     * angegebenen Knotens.
     * <p>
     * MaW. korrigiert die Ordinalzahlen der Geschwister des angegebenen
     * Knotens.
     * </p>
     *
     * @param me Der Knoten.
     */
    public void reorderMyParentChildren(Node me) {
        Node parent = me.getParent();
        if (parent != null) {
            reorderNodeList(parent.getChildren());
        }
    }

    /**
     * Hilfsmethode: Korrigiert die Ordnungszahlen in der angegebenen
     * Knotenliste.
     * <p>
     * Die Ordnungszahlen werden fortlaufend ab 0 aktualisiert wobei die
     * Originalreihenfolge der Knoten nach ihrer Ordnungszahl erhalten bleibt.
     * </p>
     * <p>
     * Die Reihenfolge der Knoten in der angegebenen Knotenliste spielt keine
     * Rolle. Auch ist die Reihenfolge in der Knotenliste nach dem Korrigieren
     * unbestimmt.
     * </p>
     *
     * @param nodes Die Knotenliste.
     */
    public void reorderNodeList(List<Node> nodes) {
        Collections.sort(nodes, Order.ASCENDING);

        int ordinalNumber = 0;
        for (Node node : nodes) {
            node.setOrdinalNumber(ordinalNumber);

            ordinalNumber++;
        }
    }

    /**
     * Hilfsmethode: Entfernt den angegebenen Knoten von seinm Vaterknoten.
     * <p>
     * Hat keinen Effekt wenn der Knoten keinen Vater hat.
     * </p>
     *
     * @param me Der Knoten.
     */
    public void removeMeFromParent(Node me) {
        Node parent = me.getParent();
        if (parent != null) {
            List<Node> children = parent.getChildren();

            children.remove(me);
            me.setParent(null);
        }
    }

    // Setter und Getter //////////////////////////////////////////////////////

    @Autowired
    public void setTreeOperationDao(TreeOperationDao treeOperationDao) {
        this.treeOperationDao = treeOperationDao;
    }

    @Autowired
    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }

    @Autowired
    public void setTreeDao(TreeDao treeDao) {
        this.treeDao = treeDao;
    }

    @Autowired
    public void setDataSourceDao(DataSourceDao dataSourceDao) {
        this.dataSourceDao = dataSourceDao;
    }

}
