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

package de.iew.persistence.mock.fixtures;

import de.iew.domain.Node;
import de.iew.domain.Tree;
import de.iew.persistence.NodeDao;
import de.iew.persistence.TreeDao;

import javax.annotation.PostConstruct;

/**
 * Erstellt das folgende Szenario.
 * <pre>
 * +- rootNode(1)    ----> tree(1) 0:[1, 6]
 * |  |
 * |  +- child1(2)   ----> tree(1) 1:[4, 5]
 * |  |
 * |  +- child2(3)   ----> tree(1) 0:[2, 3]
 * </pre>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 12:53
 */
public class TreeFixture1 {

    private TreeDao treeDao;

    private NodeDao nodeDao;

    @PostConstruct
    public void setUp() {
        Tree tree = new Tree();
        tree.setId(1l);
        tree = this.treeDao.save(tree);

        Node rootNode = new Node();
        rootNode.setId(1l);
        rootNode.setOrderInLevel(0);
        rootNode = this.nodeDao.save(rootNode);
        rootNode.setNestedSetLeft(1);
        rootNode.setNestedSetRight(6);

        Node child1 = new Node();
        child1.setId(2l);
        child1.setOrderInLevel(1);
        child1 = this.nodeDao.save(child1);
        child1.setNestedSetLeft(4);
        child1.setNestedSetRight(5);

        Node child2 = new Node();
        child2.setId(3l);
        child2.setOrderInLevel(0);
        child2 = this.nodeDao.save(child2);
        child2.setNestedSetLeft(2);
        child2.setNestedSetRight(3);

        // Wurzel für den Baum setzen
        tree.setRoot(rootNode);
        rootNode.setTree(tree);

        // Die Knoten des Baumes setzen
        tree.getNodes().add(rootNode);
        rootNode.setTree(tree);

        tree.getNodes().add(child1);
        child1.setTree(tree);

        tree.getNodes().add(child2);
        child2.setTree(tree);

        // Vater Kind Beziehungen setzen; Die Reihenfolge für getChildren().add() ist wichtig
        child2.setParent(rootNode);
        rootNode.getChildren().add(child2);

        child1.setParent(rootNode);
        rootNode.getChildren().add(child1);

    }

    public void setTreeDao(TreeDao treeDao) {
        this.treeDao = treeDao;
    }

    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }
}
