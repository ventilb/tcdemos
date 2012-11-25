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

import de.iew.domain.ModelNotFoundException;
import de.iew.domain.Node;
import de.iew.domain.Tree;
import de.iew.persistence.NodeDao;
import de.iew.persistence.TreeDao;
import de.iew.services.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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

    private TreeDao treeDao;

    private NodeDao nodeDao;

    public Node prependNewTreeRootNode(String title, long treeId) throws ModelNotFoundException {
        Tree tree = getTreeById(treeId);

        long nestedSetLeft = 1;
        long nestedSetRight = 2;

        Node newNode = new Node();
        newNode.setTitle(title);
        newNode = this.nodeDao.save(newNode);

        newNode.setTree(tree);
        tree.getNodes().add(newNode);


        Node oldRoot = tree.getRoot();
        if (oldRoot != null) {
            nestedSetRight = oldRoot.getNestedSetRight() + 2;

            this.nodeDao.incNestedSetBorders(tree.getId());

            oldRoot.setParent(newNode);
            newNode.getChildren().add(oldRoot);
        }

        tree.setRoot(newNode);

        newNode.setNestedSetLeft(nestedSetLeft);
        newNode.setNestedSetRight(nestedSetRight);

        return newNode;
    }

    public Node appendNewNode(String title, long treeId, long parentId) throws ModelNotFoundException {
        return appendNewNodeAt(title, treeId, parentId, Integer.MAX_VALUE);
    }

    public Node appendNewNodeAt(String title, long treeId, long parentId, int orderToAppendAt) throws ModelNotFoundException {
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
            if (child.getOrderInLevel() >= orderToAppendAt) {
                child.setOrderInLevel(child.getOrderInLevel() + 1);
            }
        }

        Node newNode = new Node();
        newNode.setTitle(title);
        newNode.setOrderInLevel(orderToAppendAt);
        newNode.setNestedSetLeft(nestedSetRightToShiftFrom + 1);
        newNode.setNestedSetRight(nestedSetRightToShiftFrom + 2);

        newNode.setTree(tree);
        tree.getNodes().add(newNode);

        newNode.setParent(parent);
        parent.getChildren().add(orderToAppendAt, newNode);

        newNode = this.nodeDao.save(newNode);

        return newNode;
    }

    public Node insertNewNodeBefore(String title, long treeId, long nodeToInsertBefore) throws ModelNotFoundException {
        Node sibling = getNodeByTreeAndId(treeId, nodeToInsertBefore);

        return appendNewNodeAt(title, treeId, sibling.getParent().getId(), sibling.getOrderInLevel());
    }

    public Node insertNewNodeAfter(String title, long treeId, long nodeToInsertAfter) throws ModelNotFoundException {
        Node sibling = getNodeByTreeAndId(treeId, nodeToInsertAfter);

        return appendNewNodeAt(title, treeId, sibling.getParent().getId(), sibling.getOrderInLevel() + 1);
    }

    public Node deleteNodeAndSubtree(long treeId, long nodeId) throws ModelNotFoundException {
        Node deletedNode = getNodeByTreeAndId(treeId, nodeId);
        int deletedNodeOrder = deletedNode.getOrderInLevel();

        Node parent = deletedNode.getParent();
        if (parent != null) {
            List<Node> children = parent.getChildren();

            Node childIt;
            for (int i = deletedNodeOrder; i < children.size(); i++) {
                childIt = children.get(i);
                childIt.setOrderInLevel(childIt.getOrderInLevel() - 1);
            }

            children.remove(deletedNode);
            deletedNode.setParent(null);
        }

        this.nodeDao.deleteNodesBetween(treeId, deletedNode.getNestedSetLeft(), deletedNode.getNestedSetRight());

        deletedNode.setId(null);
        return deletedNode;
    }

    public Collection<Node> getAllNodes(long treeId) throws ModelNotFoundException {
        Tree tree = getTreeById(treeId);

        return tree.getNodes();
    }

    public Node getTreeRootNode(long treeId) throws ModelNotFoundException {
        Node rootNode;

        rootNode = this.treeDao.findRootNodeForTree(treeId);

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
        Node node = this.treeDao.findNodeForTreeAndId(treeId, nodeId);

        if (node == null) {
            throw new ModelNotFoundException("The node " + nodeId + " was not found for the requested tree " + treeId + ".");
        }
        return node;
    }

    @Autowired
    public void setTreeDao(TreeDao treeDao) {
        this.treeDao = treeDao;
    }

    @Autowired
    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }
}
