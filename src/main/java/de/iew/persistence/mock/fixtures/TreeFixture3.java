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
 * +- rootNode1(1)    ---> tree1(1) 0:[1, 10]
 * |  |
 * |  +- child1(2)    ---> tree1(1) 1:[4, 9]
 * |  |  |
 * |  |  +- child3(4) ---> tree1(1) 1:[7, 8]
 * |  |  |
 * |  |  +- child4(5) ---> tree1(1) 0:[5, 6]
 * |  |
 * |  +- child2(3)    ---> tree1(1) 0:[2, 3]
 *
 * +- rootNode2(6)    ---> tree2(2) 0:[1, 6]
 * |  |
 * |  +- child5(7)    ---> tree2(2) 1:[4, 5]
 * |  |
 * |  +- child6(8)    ---> tree2(2) 0:[2, 3]
 * </pre>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 21.11.12 - 20:54
 */
public class TreeFixture3 {

    private TreeDao treeDao;

    private NodeDao nodeDao;

    @PostConstruct
    public void setUp() {
        Tree tree1 = new Tree();
        tree1.setId(1l);
        tree1 = this.treeDao.save(tree1);

        Node rootNode1 = new Node();
        rootNode1.setId(1l);
        rootNode1 = this.nodeDao.save(rootNode1);
        rootNode1.setNestedSetLeft(1);
        rootNode1.setNestedSetRight(10);
        rootNode1.setOrderInLevel(0);

        Node child1 = new Node();
        child1.setId(2l);
        child1 = this.nodeDao.save(child1);
        child1.setNestedSetLeft(4);
        child1.setNestedSetRight(9);
        child1.setOrderInLevel(1);

        Node child2 = new Node();
        child2.setId(3l);
        child2 = this.nodeDao.save(child2);
        child2.setNestedSetLeft(2);
        child2.setNestedSetRight(3);
        child2.setOrderInLevel(0);

        Node child3 = new Node();
        child3.setId(4l);
        child3 = this.nodeDao.save(child3);
        child3.setNestedSetLeft(7);
        child3.setNestedSetRight(8);
        child3.setOrderInLevel(1);

        Node child4 = new Node();
        child4.setId(5l);
        child4 = this.nodeDao.save(child4);
        child4.setNestedSetLeft(5);
        child4.setNestedSetRight(6);
        child4.setOrderInLevel(0);

        // Wurzel für den Baum setzen
        tree1.setRoot(rootNode1);

        // Die Knoten des Baumes setzen
        tree1.getNodes().add(rootNode1);
        rootNode1.setTree(tree1);

        tree1.getNodes().add(child1);
        child1.setTree(tree1);

        tree1.getNodes().add(child2);
        child2.setTree(tree1);

        tree1.getNodes().add(child3);
        child3.setTree(tree1);

        tree1.getNodes().add(child4);
        child4.setTree(tree1);

        // Vater Kind Beziehungen setzen
        child1.setParent(rootNode1);
        rootNode1.getChildren().add(child1);

        child2.setParent(rootNode1);
        rootNode1.getChildren().add(child2);

        child3.setParent(child2);
        child2.getChildren().add(child3);

        child4.setParent(child2);
        child2.getChildren().add(child4);


        // Baum 2 erstellen
        Tree tree2 = new Tree();
        tree2.setId(2l);
        tree2 = this.treeDao.save(tree2);

        Node rootNode2 = new Node();
        rootNode2.setId(6l);
        rootNode2 = this.nodeDao.save(rootNode2);
        rootNode2.setNestedSetLeft(1);
        rootNode2.setNestedSetRight(6);
        rootNode2.setOrderInLevel(0);

        Node child5 = new Node();
        child5.setId(7l);
        child5 = this.nodeDao.save(child5);
        child5.setNestedSetLeft(4);
        child5.setNestedSetRight(5);
        child5.setOrderInLevel(1);

        Node child6 = new Node();
        child6.setId(8l);
        child6 = this.nodeDao.save(child6);
        child6.setNestedSetLeft(2);
        child6.setNestedSetRight(3);
        child6.setOrderInLevel(0);

        // Wurzel für den Baum setzen
        tree2.setRoot(rootNode2);

        // Die Knoten des Baumes setzen
        tree2.getNodes().add(rootNode2);
        rootNode2.setTree(tree2);

        tree2.getNodes().add(child5);
        child5.setTree(tree2);

        tree2.getNodes().add(child6);
        child6.setTree(tree2);

        // Vater Kind Beziehungen setzen
        child5.setParent(rootNode2);
        rootNode2.getChildren().add(child5);

        child6.setParent(rootNode2);
        rootNode2.getChildren().add(child6);
    }

    public void setTreeDao(TreeDao treeDao) {
        this.treeDao = treeDao;
    }

    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }
}
