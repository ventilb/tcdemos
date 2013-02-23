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

import de.iew.framework.domain.ModelNotFoundException;
import de.iew.framework.domain.Node;
import de.iew.framework.domain.Tree;
import de.iew.framework.persistence.DataSourceDao;
import de.iew.framework.persistence.NodeDao;
import de.iew.framework.persistence.TreeDao;
import de.iew.framework.persistence.TreeOperationDao;
import de.iew.framework.persistence.mock.MockDataSourceDaoImpl;
import de.iew.framework.persistence.mock.MockNodeDaoImpl;
import de.iew.framework.persistence.mock.MockTreeDaoImpl;
import de.iew.framework.persistence.mock.MockTreeOperationDaoImpl;
import de.iew.framework.persistence.mock.fixtures.TreeFixture1;
import de.iew.framework.persistence.mock.fixtures.TreeFixture2;
import de.iew.framework.persistence.mock.fixtures.TreeFixture3;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tested die {@link TreeServiceImpl} Implementierung.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 11:00
 */
public class TreeServiceImplTest {

    private TreeOperationDao treeOperationDao;

    private NodeDao nodeDao;

    private TreeDao treeDao;

    private DataSourceDao dataSourceDao;

    @Before
    public void setUp() throws Exception {
        this.treeDao = new MockTreeDaoImpl();
        this.nodeDao = new MockNodeDaoImpl();
        this.dataSourceDao = new MockDataSourceDaoImpl();

        MockTreeOperationDaoImpl treeOperationDao = new MockTreeOperationDaoImpl();
        treeOperationDao.setNodeDao(this.nodeDao);
        treeOperationDao.setTreeDao(this.treeDao);
        this.treeOperationDao = treeOperationDao;
    }

    @Test
    public void testGetTreeByLookupKey1() throws Exception {
        // Testfix erstellen
        setupTestfixture3();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Tree tree = treeService.getTreeByLookupKey("TestTree1");
        assertEquals(this.treeDao.findById(1), tree);

        tree = treeService.getTreeByLookupKey("TESTTREE1");
        assertEquals(this.treeDao.findById(1), tree);
    }

    @Test(expected = ModelNotFoundException.class)
    public void testGetTreeByLookupKey2() throws Exception {
        // Testfix erstellen
        setupTestfixture3();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.getTreeByLookupKey("A_Tree_that_does_not_exist");
    }

    @Test
    public void testGetAllNodes() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung

        Collection<Node> allNodes = (Collection<Node>) treeService.getAllNodes(tree.getId());
        assertEquals(3, allNodes.size());
    }

    /**
     * Testet das Löschen von Knoten im Baum.
     * <p>Ausgangsszenario:</p>
     * <pre>
     * +- rootNode(1)    ----> tree(1) 0:[1, 6]
     * |  |
     * |  +- child1(2)   ----> tree(1) 1:[4, 5]
     * |  |
     * |  +- child2(3)   ----> tree(1) 0:[2, 3]
     * </pre>
     * <p>Szenario 1:</p>
     * <pre>
     * +- rootNode(1)    ----> tree(1) [1, 4]
     * |  |
     * |  +- child2(3)   ----> tree(1) [2, 3]
     * </pre>
     *
     * @throws Exception
     */
    @Test
    public void testDeleteNodeAndSubtree1() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node rootNode = tree.getRoot();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung 1
        Node deletedNode1 = treeService.deleteNodeAndSubtree(tree.getId(), 2);
        assertNotNull(deletedNode1);

        assertNull(this.nodeDao.findById(2));

        assertEquals(1, rootNode.getChildren().size());
        assertEquals(2, tree.getNodes().size());

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(4, this.nodeDao.findById(1).getNestedSetRight());

        assertEquals(2, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(3, this.nodeDao.findById(3).getNestedSetRight());

        // Test und Auswertung 2
        Node deletedNode2 = treeService.deleteNodeAndSubtree(tree.getId(), 1);
        assertNotNull(deletedNode2);

        assertNull(this.nodeDao.findById(1));
        assertNull(this.nodeDao.findById(3));

        assertNull(tree.getRoot());
        assertTrue(tree.getNodes().isEmpty());
    }

    @Test
    public void testDeleteNodeAndSubtree2() throws Exception {
        // Testfix erstellen
        setupTestfixture3();
        Tree tree = this.treeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung 1
        Node deletedNode = treeService.deleteNodeAndSubtree(tree.getId(), 2);
        assertNotNull(deletedNode);
        assertNull(this.nodeDao.findById(2));

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrdinalNumber());

        assertEquals(2, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(3).getOrdinalNumber());

        assertEquals(5, this.nodeDao.findById(4).getNestedSetLeft());
        assertEquals(6, this.nodeDao.findById(4).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(4).getOrdinalNumber());

        assertEquals(3, this.nodeDao.findById(5).getNestedSetLeft());
        assertEquals(4, this.nodeDao.findById(5).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(5).getOrdinalNumber());
    }

    @Test
    public void testDeleteNode1() throws Exception {
        // Testfix erstellen
        setupTestfixture3();
        Tree tree = this.treeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Node deletedNode = treeService.deleteNodeAndMigrateChildren(tree.getId(), 3);
        assertNotNull(deletedNode);
        assertNull(deletedNode.getParent());
        assertNull(this.nodeDao.findById(3));
        assertEquals(new Long(3), deletedNode.getId());

        assertEquals(4, tree.getNodes().size());

        // Die Kinder des gelöschten Knotens existieren noch
        assertNotNull(this.nodeDao.findById(4));
        assertNotNull(this.nodeDao.findById(5));

        // Die Kinder des gelöschten Knotens hängen in diesem Szenario an der
        // Wurzel
        assertEquals(this.nodeDao.findById(1), this.nodeDao.findById(4).getParent());
        assertEquals(this.nodeDao.findById(1), this.nodeDao.findById(5).getParent());

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrdinalNumber());

        assertEquals(6, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrdinalNumber());

        assertEquals(4, this.nodeDao.findById(4).getNestedSetLeft());
        assertEquals(5, this.nodeDao.findById(4).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(4).getOrdinalNumber());

        assertEquals(2, this.nodeDao.findById(5).getNestedSetLeft());
        assertEquals(3, this.nodeDao.findById(5).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(5).getOrdinalNumber());
    }

    @Test
    public void testMigrateMyChildrenToParent() {
        // Testfix erstellen
        Node parent = new Node();
        parent.setId(1l);
        parent.setOrdinalNumber(0);

        Node child1 = new Node();
        child1.setId(2l);
        child1.setOrdinalNumber(0);

        Node child2 = new Node();
        child2.setId(3l);
        child2.setOrdinalNumber(1);

        Node child3 = new Node();
        child3.setId(4l);
        child3.setOrdinalNumber(2);

        Node child4 = new Node();
        child4.setId(5l);
        child4.setOrdinalNumber(0);

        Node child5 = new Node();
        child5.setId(6l);
        child5.setOrdinalNumber(1);

        child1.setParent(parent);
        parent.getChildren().add(child1);

        child2.setParent(parent);
        parent.getChildren().add(child2);

        child3.setParent(parent);
        parent.getChildren().add(child3);

        child4.setParent(child2);
        child2.getChildren().add(child4);

        child5.setParent(child2);
        child2.getChildren().add(child5);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.migrateMyChildrenToParent(child2);

        assertEquals(5, parent.getChildren().size());
        assertTrue(child2.getChildren().isEmpty());

        assertEquals(parent, child4.getParent());
        assertTrue(parent.getChildren().contains(child4));

        assertEquals(parent, child5.getParent());
        assertTrue(parent.getChildren().contains(child5));

        assertEquals(0, child1.getOrdinalNumber());
        assertEquals(1, child2.getOrdinalNumber());
        assertEquals(2, child4.getOrdinalNumber());
        assertEquals(3, child5.getOrdinalNumber());
        assertEquals(4, child3.getOrdinalNumber());
    }

    @Test
    public void testReorderNodeList() {
        // Testfix erstellen
        List<Node> nodes = new ArrayList<Node>();
        Node n1 = new Node();
        n1.setOrdinalNumber(0);

        Node n2 = new Node();
        n2.setOrdinalNumber(1);

        Node n3 = new Node();
        n3.setOrdinalNumber(3);

        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.reorderNodeList(nodes);

        assertEquals(0, n1.getOrdinalNumber());
        assertEquals(1, n2.getOrdinalNumber());
        assertEquals(2, n3.getOrdinalNumber());

        // Testfix erstellen
        Node n4 = new Node();
        n4.setOrdinalNumber(7);

        nodes.add(n4);

        // Das Testobjekt erstellen
        treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.reorderNodeList(nodes);

        assertEquals(0, n1.getOrdinalNumber());
        assertEquals(1, n2.getOrdinalNumber());
        assertEquals(2, n3.getOrdinalNumber());
        assertEquals(3, n4.getOrdinalNumber());

        // Testfix erstellen
        nodes = new ArrayList<Node>();
        n1 = new Node();
        n1.setOrdinalNumber(1);

        n2 = new Node();
        n2.setOrdinalNumber(2);

        n3 = new Node();
        n3.setOrdinalNumber(3);

        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);

        // Das Testobjekt erstellen
        treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.reorderNodeList(nodes);

        assertEquals(0, n1.getOrdinalNumber());
        assertEquals(1, n2.getOrdinalNumber());
        assertEquals(2, n3.getOrdinalNumber());
    }

    @Test
    public void testReorderMyParentChildren() {
        // Testfix erstellen
        Node parent = new Node();
        parent.setId(1l);
        parent.setOrdinalNumber(0);

        Node child1 = new Node();
        child1.setId(2l);
        child1.setOrdinalNumber(2);

        Node child2 = new Node();
        child2.setId(3l);
        child2.setOrdinalNumber(5);

        Node child3 = new Node();
        child3.setId(4l);
        child3.setOrdinalNumber(1);

        child1.setParent(parent);
        parent.getChildren().add(child1);

        child2.setParent(parent);
        parent.getChildren().add(child2);

        child3.setParent(parent);
        parent.getChildren().add(child3);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.reorderMyParentChildren(child2);

        assertEquals(0, child3.getOrdinalNumber());
        assertEquals(1, child1.getOrdinalNumber());
        assertEquals(2, child2.getOrdinalNumber());
    }

    @Test
    public void testRemoveMeFromParent() {
        // Testfix erstellen
        Node parent = new Node();
        parent.setId(1l);

        Node child1 = new Node();
        child1.setId(2l);

        Node child2 = new Node();
        child2.setId(3l);

        Node child3 = new Node();
        child3.setId(4l);

        child1.setParent(parent);
        parent.getChildren().add(child1);

        child2.setParent(parent);
        parent.getChildren().add(child2);

        child3.setParent(parent);
        parent.getChildren().add(child3);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.removeMeFromParent(child3);

        assertNull(child3.getParent());
        assertEquals(2, parent.getChildren().size());

        treeService.removeMeFromParent(child1);

        assertNull(child1.getParent());
        assertEquals(1, parent.getChildren().size());

        treeService.removeMeFromParent(parent);

        assertNull(parent.getParent());
        assertEquals(1, parent.getChildren().size());
    }

    @Test
    public void testAppendNewNodeAt1() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node parentNode = this.nodeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Node createdNode = (Node) treeService.appendNewNodeAt(tree.getId(), parentNode.getId(), 0, 2);

        assertEquals(0, this.nodeDao.findById(1).getOrdinalNumber());
        assertEquals(1, this.nodeDao.findById(2).getOrdinalNumber());
        assertEquals(0, this.nodeDao.findById(3).getOrdinalNumber());
        assertEquals(2, createdNode.getOrdinalNumber()); // Der Knoten wurde an das Ende der Kinderliste angehängt
    }

    @Test
    public void testAppendNewNodeAt2() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node parentNode = this.nodeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Node createdNode = (Node) treeService.appendNewNodeAt(tree.getId(), parentNode.getId(), -1, 10);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrdinalNumber());

        assertEquals(4, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(5, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(2).getOrdinalNumber());

        assertEquals(2, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(3, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(3).getOrdinalNumber());

        assertEquals(6, createdNode.getNestedSetLeft());
        assertEquals(7, createdNode.getNestedSetRight());
        assertEquals(2, createdNode.getOrdinalNumber()); // Der Knoten wurde an das Ende der Kinderliste angehängt
    }

    @Test
    public void testAppendNewNodeAt3() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node parentNode = this.nodeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Node createdNode = (Node) treeService.appendNewNodeAt(tree.getId(), parentNode.getId(), 0, 1);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrdinalNumber());

        assertEquals(6, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrdinalNumber());

        assertEquals(2, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(3, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(3).getOrdinalNumber());

        assertEquals(4, createdNode.getNestedSetLeft());
        assertEquals(5, createdNode.getNestedSetRight());
        assertEquals(1, createdNode.getOrdinalNumber());
    }

    @Test
    public void testAppendNewNodeAt4() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node parentNode = this.nodeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Node createdNode = (Node) treeService.appendNewNodeAt(tree.getId(), parentNode.getId(), 0, 0);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrdinalNumber());

        assertEquals(6, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrdinalNumber());

        assertEquals(4, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(5, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(3).getOrdinalNumber());

        assertEquals(2, createdNode.getNestedSetLeft());
        assertEquals(3, createdNode.getNestedSetRight());
        assertEquals(0, createdNode.getOrdinalNumber());
    }

    /**
     * Test das Erstellen einer neuen Wurzel in einem existierenden Baum.
     * <p>
     * Szenario 1:
     * </p>
     * <pre>
     * +- rootNode     ----> tree1 [1, 8]
     * |  |
     * |  +- child1    ----> tree1 [6, 7]
     * |  |
     * |  +- child2    ----> tree1 [4, 5]
     * |  |
     * |  +- createdNode1 -> tree1 [2, 3]
     * </pre>
     * <p>
     * Szenarion 2:
     * </p>
     * <pre>
     * +- rootNode     ----> tree1 [1, 10]
     * |  |
     * |  +- child1    ----> tree1 [8, 9]
     * |  |
     * |  +- child2    ----> tree1 [6, 7]
     * |  |
     * |  +- createdNode1 -> tree1 [2, 5]
     * |  |  |
     * |  |  +- createdNode2 -> tree1 [3, 4]
     * </pre>
     * <p>
     * Szenario 3:
     * </p>
     * <pre>
     * +- rootNode        ----> tree1 [1, 12]
     * |  |
     * |  +- child1       ----> tree1 [10, 11]
     * |  |
     * |  +- child2       ----> tree1 [8, 9]
     * |  |
     * |  +- createdNode1 ----> tree1 [2, 7]
     * |  |  |
     * |  |  +- createdNode3 -> tree1 [5, 6]
     * |  |  |
     * |  |  +- createdNode2 -> tree1 [3, 4]
     * </pre>
     * <p>
     * Szenario 4:
     * </p>
     * <pre>
     * +- rootNode        ----> tree1 [1, 14]
     * |  |
     * |  +- child1       ----> tree1 [12, 13]
     * |  |
     * |  +- createdNode4 ----> tree1 [10, 11]
     * |  |
     * |  +- child2       ----> tree1 [8, 9]
     * |  |
     * |  +- createdNode1 ----> tree1 [2, 7]
     * |  |  |
     * |  |  +- createdNode3 -> tree1 [5, 6]
     * |  |  |
     * |  |  +- createdNode2 -> tree1 [3, 4]
     * </pre>
     *
     * @throws Exception
     */
    @Test
    public void testAppendNewNodeAt5() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung 1
        Node createdNode1 = (Node) treeService.appendNewNodeAt(tree.getId(), 1, 0, 0);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrdinalNumber());

        assertEquals(6, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrdinalNumber());

        assertEquals(4, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(5, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(3).getOrdinalNumber());

        assertEquals(2, createdNode1.getNestedSetLeft());
        assertEquals(3, createdNode1.getNestedSetRight());
        assertEquals(0, createdNode1.getOrdinalNumber());

        // Test und Auswertung 2
        Node createdNode2 = (Node) treeService.appendNewNodeAt(tree.getId(), createdNode1.getId(), 0, 0);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(10, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrdinalNumber());

        assertEquals(8, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(9, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrdinalNumber());

        assertEquals(6, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(3).getOrdinalNumber());

        assertEquals(2, createdNode1.getNestedSetLeft());
        assertEquals(5, createdNode1.getNestedSetRight());
        assertEquals(0, createdNode1.getOrdinalNumber());

        assertEquals(3, createdNode2.getNestedSetLeft());
        assertEquals(4, createdNode2.getNestedSetRight());
        assertEquals(0, createdNode2.getOrdinalNumber());

        // Test und Auswertung 3
        Node createdNode3 = (Node) treeService.appendNewNodeAt(tree.getId(), createdNode1.getId(), 0, 1);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(12, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrdinalNumber());

        assertEquals(10, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(11, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrdinalNumber());

        assertEquals(8, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(9, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(3).getOrdinalNumber());

        assertEquals(2, createdNode1.getNestedSetLeft());
        assertEquals(7, createdNode1.getNestedSetRight());
        assertEquals(0, createdNode1.getOrdinalNumber());

        assertEquals(3, createdNode2.getNestedSetLeft());
        assertEquals(4, createdNode2.getNestedSetRight());
        assertEquals(0, createdNode2.getOrdinalNumber());

        assertEquals(5, createdNode3.getNestedSetLeft());
        assertEquals(6, createdNode3.getNestedSetRight());
        assertEquals(1, createdNode3.getOrdinalNumber());

        // Test und Auswertung 4
        Node createdNode4 = (Node) treeService.appendNewNodeAt(tree.getId(), 1, 0, 2);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(14, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrdinalNumber());

        assertEquals(12, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(13, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(3, this.nodeDao.findById(2).getOrdinalNumber());

        assertEquals(10, createdNode4.getNestedSetLeft());
        assertEquals(11, createdNode4.getNestedSetRight());
        assertEquals(2, createdNode4.getOrdinalNumber());

        assertEquals(8, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(9, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(3).getOrdinalNumber());

        assertEquals(2, createdNode1.getNestedSetLeft());
        assertEquals(7, createdNode1.getNestedSetRight());
        assertEquals(0, createdNode1.getOrdinalNumber());

        assertEquals(3, createdNode2.getNestedSetLeft());
        assertEquals(4, createdNode2.getNestedSetRight());
        assertEquals(0, createdNode2.getOrdinalNumber());

        assertEquals(5, createdNode3.getNestedSetLeft());
        assertEquals(6, createdNode3.getNestedSetRight());
        assertEquals(1, createdNode3.getOrdinalNumber());
    }

    @Test
    public void testAppendNewNode() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node parentNode = this.nodeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Node createdNode = (Node) treeService.appendNewNode(tree.getId(), parentNode.getId(), -1);

        assertNotNull(createdNode);
        assertNotNull(createdNode.getId());
        assertEquals(parentNode, createdNode.getParent());
        assertTrue(parentNode.getChildren().contains(createdNode));

        assertEquals(tree, createdNode.getTree());
        assertTrue(tree.getNodes().contains(createdNode));

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());

        assertEquals(4, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(5, this.nodeDao.findById(2).getNestedSetRight());

        assertEquals(2, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(3, this.nodeDao.findById(3).getNestedSetRight());

        assertEquals(6, createdNode.getNestedSetLeft());
        assertEquals(7, createdNode.getNestedSetRight());
    }

    @Test
    public void testPrependNewRootNode1() throws Exception {
        // Testfix erstellen
        setupTestfixture2();
        Tree tree = this.treeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Node createdNode = (Node) treeService.migrateRootNode(tree.getId());
        assertNotNull(createdNode);
        assertNotNull(createdNode.getId());

        assertEquals(tree, createdNode.getTree());
        assertTrue(tree.getNodes().contains(createdNode));

        assertEquals(tree.getRoot(), createdNode);
        assertEquals(createdNode, tree.getRoot());

        assertEquals(1, createdNode.getNestedSetLeft());
        assertEquals(2, createdNode.getNestedSetRight());
    }

    /**
     * Test das Erstellen einer neuen Wurzel in einem existierenden Baum.
     * <p/>
     * <pre>
     * +- createdNode    ----> tree1 [1, 8]
     * |  |
     * |  +- rootNode    ----> tree1 [2, 7]
     * |  |  |
     * |  |  +- child1   ----> tree1 [5, 6]
     * |  |  |
     * |  |  +- child2   ----> tree1 [3, 4]
     * </pre>
     *
     * @throws Exception
     */
    @Test
    public void testPrependNewRootNode2() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node oldRoot = tree.getRoot();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Node createdNode = (Node) treeService.migrateRootNode(tree.getId());
        assertNotNull(createdNode);
        assertNotNull(createdNode.getId());

        assertEquals(tree, createdNode.getTree());
        assertTrue(tree.getNodes().contains(createdNode));

        assertEquals(tree.getRoot(), createdNode);
        assertEquals(createdNode, tree.getRoot());

        assertEquals(oldRoot.getParent(), createdNode);
        assertTrue(createdNode.getChildren().contains(oldRoot));

        assertEquals(1, createdNode.getNestedSetLeft());
        assertEquals(8, createdNode.getNestedSetRight());

        assertEquals(2, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(1).getNestedSetRight());

        assertEquals(5, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(6, this.nodeDao.findById(2).getNestedSetRight());

        assertEquals(3, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(4, this.nodeDao.findById(3).getNestedSetRight());
    }

    @Test
    public void testGetTreeById1() throws Exception {
        // Testfix erstellen
        setupTestfixture2();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Tree tree = treeService.getTreeById(1);
        assertNotNull(tree);
    }

    @Test(expected = ModelNotFoundException.class)
    public void testGetTreeById2() throws Exception {
        // Testfix erstellen

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.getTreeById(1);
    }

    @Test(expected = ModelNotFoundException.class)
    public void testGetNodeByTreeAndId1() throws Exception {
        // Testfix erstellen
        setupTestfixture1();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.getNodeByTreeAndId(2, 1);
        treeService.getNodeByTreeAndId(1, 4);
    }

    @Test(expected = ModelNotFoundException.class)
    public void testGetNodeByTreeAndId2() throws Exception {
        // Testfix erstellen
        setupTestfixture1();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.getNodeByTreeAndId(1, 4);
    }

    @Test
    public void testGetNodeByTreeAndId3() throws Exception {
        // Testfix erstellen
        setupTestfixture1();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Assert.assertEquals(this.nodeDao.findById(1), treeService.getNodeByTreeAndId(1, 1));
        Assert.assertEquals(this.nodeDao.findById(2), treeService.getNodeByTreeAndId(1, 2));
        Assert.assertEquals(this.nodeDao.findById(3), treeService.getNodeByTreeAndId(1, 3));
    }

    @Test
    public void testGetTreeNodes1() throws Exception {
        // Testfix erstellen
        setupTestfixture1();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Collection<Node> treeNodes = (Collection<Node>) treeService.getDirectChildNodes(1, 1);
        Assert.assertNotNull(treeNodes);
        Assert.assertEquals(2, treeNodes.size());
        Assert.assertTrue(treeNodes.contains(this.nodeDao.findById(2)));
        Assert.assertTrue(treeNodes.contains(this.nodeDao.findById(3)));
    }

    @Test(expected = ModelNotFoundException.class)
    public void testGetTreeNodes2() throws Exception {
        // Testfix erstellen
        setupTestfixture1();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.getDirectChildNodes(2, 1);
    }

    @Test
    public void testGetTreeRootNode1() throws Exception {
        // Testfix erstellen
        Tree tree = new Tree();
        tree.setId(1l);
        tree = this.treeDao.save(tree);

        Node rootNode = new Node();
        rootNode.setId(1l);
        rootNode = this.nodeDao.save(rootNode);

        tree.setRoot(rootNode);
        rootNode.setTree(tree);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        Node root = (Node) treeService.getTreeRootNode(1l);
        Assert.assertNotNull(root);
    }

    @Test(expected = ModelNotFoundException.class)
    public void testGetTreeRootNode2() throws Exception {
        // Das Testobjekt erstellen
        TreeServiceImpl treeService = newTreeServiceImpl();

        // Test und Auswertung
        treeService.getTreeRootNode(1l);
    }

    private TreeServiceImpl newTreeServiceImpl() {
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeOperationDao(this.treeOperationDao);
        treeService.setNodeDao(this.nodeDao);
        treeService.setTreeDao(this.treeDao);
        treeService.setDataSourceDao(this.dataSourceDao);
        return treeService;
    }

    /**
     * Konfiguriert einen Testaufbau.
     *
     * @see de.iew.framework.persistence.mock.fixtures.TreeFixture1
     */
    private void setupTestfixture1() {
        TreeFixture1 fixture = new TreeFixture1();
        fixture.setNodeDao(this.nodeDao);
        fixture.setTreeDao(this.treeDao);
        fixture.setUp();
    }

    /**
     * Konfiguriert einen Testaufbau.
     *
     * @see de.iew.framework.persistence.mock.fixtures.TreeFixture2
     */
    private void setupTestfixture2() {
        TreeFixture2 fixture = new TreeFixture2();
        fixture.setNodeDao(this.nodeDao);
        fixture.setTreeDao(this.treeDao);
        fixture.setUp();
    }

    /**
     * Konfiguriert einen Testaufbau.
     *
     * @see de.iew.framework.persistence.mock.fixtures.TreeFixture3
     */
    private void setupTestfixture3() {
        TreeFixture3 fixture = new TreeFixture3();
        fixture.setNodeDao(this.nodeDao);
        fixture.setTreeDao(this.treeDao);
        fixture.setUp();
    }
}
