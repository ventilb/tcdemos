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

package de.iew.persistence.mock;

import de.iew.domain.Node;
import de.iew.persistence.NodeDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Klassenkommentar.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 10:36
 */
public class MockNodeDaoImpl extends AbstractMockDomainModelDaoImpl<Node> implements NodeDao {
    public void incNestedSetBorders(long treeId) {
        for (Node node : findAll()) {
            if (node.getTree().getId() == treeId) {
                node.setNestedSetLeft(node.getNestedSetLeft() + 1);
                node.setNestedSetRight(node.getNestedSetRight() + 1);
            }
        }
    }

    public void moveNestedSetBorder(long treeId, long fromNestedSetIndex) {
        for (Node node : findAll()) {
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

    public void deleteNodesBetween(long treeId, long leftNestedSetIndex, long rightNestedSetIndex) {
        List<Node> deleteCandidates = new ArrayList<Node>();

        Node[] nodes = findAll().toArray(new Node[0]);
        for (Node node : nodes) {
            if (node.getTree().getId() == treeId) {
                if (node.getNestedSetLeft() >= leftNestedSetIndex && node.getNestedSetRight() <= rightNestedSetIndex) {
                    if (node.equals(node.getTree().getRoot())) {
                        node.getTree().setRoot(null);
                    }

                    deleteCandidates.add(node);
                    remove(node);
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
}
