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

package de.iew.framework.persistence.mock;

import de.iew.framework.domain.Node;
import de.iew.framework.domain.Tree;
import de.iew.framework.persistence.NodeDao;
import de.iew.framework.persistence.TreeDao;
import de.iew.framework.persistence.TreeOperationDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock-Implementierung der {@link de.iew.framework.persistence.TreeOperationDao}-Schnittstelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 10:08
 */
public class MockTreeOperationDaoImpl implements TreeOperationDao {

    private TreeDao treeDao;

    private NodeDao nodeDao;

    public Node save(Node domainModel) {
        Tree tree = domainModel.getTree();

        long nestedSetRightToShiftFrom = domainModel.getNestedSetRight() - 2;

        moveNestedSetBorder(tree.getId(), nestedSetRightToShiftFrom);
        return this.nodeDao.save(domainModel);
    }

    public void incNestedSetBorders(long treeId) {
        for (Node node : this.nodeDao.findAll()) {
            if (node.getTree().getId() == treeId) {
                node.setNestedSetLeft(node.getNestedSetLeft() + 1);
                node.setNestedSetRight(node.getNestedSetRight() + 1);
            }
        }
    }

    public void moveNestedSetBorder(long treeId, long fromNestedSetIndex) {
        for (Node node : this.nodeDao.findAll()) {
            if (node.getTree().getId() == treeId) {
                if (node.getNestedSetRight() > fromNestedSetIndex) {
                    node.setNestedSetRight(node.getNestedSetRight() + 2);
                }

                if (node.getNestedSetLeft() > fromNestedSetIndex) {
                    node.setNestedSetLeft(node.getNestedSetLeft() + 2);
                }
            }
        }
    }

    public void deleteSingleNode(long treeId, long nodeId) {
        Node deleteNode = this.nodeDao.findById(nodeId);

        deleteNode.getTree().getNodes().remove(deleteNode);
        deleteNode.setTree(null);

        this.nodeDao.remove(deleteNode);

        Node[] nodes = this.nodeDao.findAll().toArray(new Node[0]);
        for (Node node : nodes) {
            if (node.getTree().getId() == treeId) {
                if (node.getNestedSetLeft() >= deleteNode.getNestedSetLeft() && node.getNestedSetLeft() <= deleteNode.getNestedSetRight()) {
                    node.setNestedSetLeft(node.getNestedSetLeft() - 1);
                    node.setNestedSetRight(node.getNestedSetRight() - 1);
                }
            }
        }

        for (Node node : nodes) {
            if (node.getTree().getId() == treeId) {
                if (node.getNestedSetLeft() > deleteNode.getNestedSetRight()) {
                    node.setNestedSetLeft(node.getNestedSetLeft() - 2);
                }
            }
        }

        for (Node node : nodes) {
            if (node.getTree().getId() == treeId) {
                if (node.getNestedSetRight() > deleteNode.getNestedSetRight()) {
                    node.setNestedSetRight(node.getNestedSetRight() - 2);
                }
            }
        }

    }

    public void deleteNodesBetween(long treeId, long leftNestedSetIndex, long rightNestedSetIndex) {
        List<Node> deleteCandidates = new ArrayList<Node>();

        Node[] nodes = this.nodeDao.findAll().toArray(new Node[0]);
        for (Node node : nodes) {
            if (node.getTree().getId() == treeId) {
                if (node.getNestedSetLeft() >= leftNestedSetIndex && node.getNestedSetRight() <= rightNestedSetIndex) {
                    if (node.equals(node.getTree().getRoot())) {
                        node.getTree().setRoot(null);
                    }

                    deleteCandidates.add(node);
                    this.nodeDao.remove(node);
                }
            }
        }

        for (Node node : nodes) {
            if (node.getTree().getId() == treeId) {
                if (node.getNestedSetLeft() > rightNestedSetIndex) {
                    node.setNestedSetLeft(node.getNestedSetLeft() - Math.round(rightNestedSetIndex - leftNestedSetIndex + 1));
                }
                if (node.getNestedSetRight() > rightNestedSetIndex) {
                    node.setNestedSetRight(node.getNestedSetRight() - Math.round(rightNestedSetIndex - leftNestedSetIndex + 1));
                }
            }
        }

        for (Node node : deleteCandidates) {
            node.getTree().getNodes().remove(node);
            node.setTree(null);
        }
    }

    public Node findRootNodeForTree(long treeId) {
        Node treeRoot = null;

        for (Tree tree : this.treeDao.findAll()) {
            if (tree.getId() == treeId) {
                treeRoot = tree.getRoot();
            }
        }

        return treeRoot;
    }

    public Node findNodeForTreeAndId(long treeId, long nodeId) {
        for (Tree tree : this.treeDao.findAll()) {
            if (tree.getId() == treeId) {
                for (Node node : tree.getNodes()) {
                    if (node.getId() == nodeId) {
                        return node;
                    }
                }
            }
        }
        return null;
    }

    public void setTreeDao(TreeDao treeDao) {
        this.treeDao = treeDao;
    }

    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }
}
