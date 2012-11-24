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
import de.iew.persistence.mock.MockNodeDaoImpl;
import de.iew.persistence.mock.MockTreeDaoImpl;
import de.iew.persistence.mock.fixtures.TreeFixture1;
import de.iew.persistence.mock.fixtures.TreeFixture2;
import de.iew.persistence.mock.fixtures.TreeFixture3;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertNotNull;

import java.util.Collection;

/**
 * Tested die {@link TreeServiceImpl} Implementierung für den
 * {@link de.iew.services.TreeService}.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 11:00
 */
public class TreeServiceImplTest {

    private TreeDao treeDao;

    private NodeDao nodeDao;

    @Before
    public void setUp() throws Exception {
        this.treeDao = new MockTreeDaoImpl();
        this.nodeDao = new MockNodeDaoImpl();
    }

    @Test
    public void testGetAllNodes() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung

        Collection<Node> allNodes = treeService.getAllNodes(tree.getId());
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
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung 1
        Node deletedNode1 = treeService.deleteNodeAndSubtree(tree.getId(), 2);
        assertNotNull(deletedNode1);
        assertNull(deletedNode1.getId());

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
        assertNull(deletedNode2.getId());

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
        Node rootNode = tree.getRoot();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung 1
        Node deletedNode1 = treeService.deleteNodeAndSubtree(tree.getId(), 3);
        assertNotNull(deletedNode1);
        assertNull(deletedNode1.getId());
        assertNull(this.nodeDao.findById(3));

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrderInLevel());

        assertEquals(2, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(2).getOrderInLevel());

        assertEquals(5, this.nodeDao.findById(4).getNestedSetLeft());
        assertEquals(6, this.nodeDao.findById(4).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(4).getOrderInLevel());

        assertEquals(3, this.nodeDao.findById(5).getNestedSetLeft());
        assertEquals(4, this.nodeDao.findById(5).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(5).getOrderInLevel());


    }

    @Test
    public void testAppendNewNodeAt1() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node parentNode = this.nodeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        Node createdNode = treeService.appendNewNodeAt("Ein neuer Knoten", tree.getId(), parentNode.getId(), 2);

        assertEquals(0, this.nodeDao.findById(1).getOrderInLevel());
        assertEquals(1, this.nodeDao.findById(2).getOrderInLevel());
        assertEquals(0, this.nodeDao.findById(3).getOrderInLevel());
        assertEquals(2, createdNode.getOrderInLevel()); // Der Knoten wurde an das Ende der Kinderliste angehängt
    }

    @Test
    public void testAppendNewNodeAt2() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node parentNode = this.nodeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        Node createdNode = treeService.appendNewNodeAt("Ein neuer Knoten", tree.getId(), parentNode.getId(), 10);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrderInLevel());

        assertEquals(4, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(5, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(2).getOrderInLevel());

        assertEquals(2, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(3, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(3).getOrderInLevel());

        assertEquals(6, createdNode.getNestedSetLeft());
        assertEquals(7, createdNode.getNestedSetRight());
        assertEquals(2, createdNode.getOrderInLevel()); // Der Knoten wurde an das Ende der Kinderliste angehängt
    }

    @Test
    public void testAppendNewNodeAt3() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node parentNode = this.nodeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        Node createdNode = treeService.appendNewNodeAt("Ein neuer Knoten", tree.getId(), parentNode.getId(), 1);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrderInLevel());

        assertEquals(6, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrderInLevel());

        assertEquals(2, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(3, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(3).getOrderInLevel());

        assertEquals(4, createdNode.getNestedSetLeft());
        assertEquals(5, createdNode.getNestedSetRight());
        assertEquals(1, createdNode.getOrderInLevel());
    }

    @Test
    public void testAppendNewNodeAt4() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node parentNode = this.nodeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        Node createdNode = treeService.appendNewNodeAt("Ein neuer Knoten", tree.getId(), parentNode.getId(), 0);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrderInLevel());

        assertEquals(6, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrderInLevel());

        assertEquals(4, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(5, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(3).getOrderInLevel());

        assertEquals(2, createdNode.getNestedSetLeft());
        assertEquals(3, createdNode.getNestedSetRight());
        assertEquals(0, createdNode.getOrderInLevel());
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
     * <p/>
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
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung 1
        Node createdNode1 = treeService.appendNewNodeAt("Ein neuer Knoten 1", tree.getId(), 1, 0);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(8, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrderInLevel());

        assertEquals(6, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrderInLevel());

        assertEquals(4, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(5, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(3).getOrderInLevel());

        assertEquals(2, createdNode1.getNestedSetLeft());
        assertEquals(3, createdNode1.getNestedSetRight());
        assertEquals(0, createdNode1.getOrderInLevel());

        // Test und Auswertung 2
        Node createdNode2 = treeService.appendNewNodeAt("Ein neuer Knoten 2", tree.getId(), createdNode1.getId(), 0);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(10, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrderInLevel());

        assertEquals(8, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(9, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrderInLevel());

        assertEquals(6, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(7, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(3).getOrderInLevel());

        assertEquals(2, createdNode1.getNestedSetLeft());
        assertEquals(5, createdNode1.getNestedSetRight());
        assertEquals(0, createdNode1.getOrderInLevel());

        assertEquals(3, createdNode2.getNestedSetLeft());
        assertEquals(4, createdNode2.getNestedSetRight());
        assertEquals(0, createdNode2.getOrderInLevel());

        // Test und Auswertung 3
        Node createdNode3 = treeService.appendNewNodeAt("Ein neuer Knoten 3", tree.getId(), createdNode1.getId(), 1);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(12, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrderInLevel());

        assertEquals(10, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(11, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(2, this.nodeDao.findById(2).getOrderInLevel());

        assertEquals(8, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(9, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(3).getOrderInLevel());

        assertEquals(2, createdNode1.getNestedSetLeft());
        assertEquals(7, createdNode1.getNestedSetRight());
        assertEquals(0, createdNode1.getOrderInLevel());

        assertEquals(3, createdNode2.getNestedSetLeft());
        assertEquals(4, createdNode2.getNestedSetRight());
        assertEquals(0, createdNode2.getOrderInLevel());

        assertEquals(5, createdNode3.getNestedSetLeft());
        assertEquals(6, createdNode3.getNestedSetRight());
        assertEquals(1, createdNode3.getOrderInLevel());

        // Test und Auswertung 4
        Node createdNode4 = treeService.appendNewNodeAt("Ein neuer Knoten 4", tree.getId(), 1, 2);

        assertEquals(1, this.nodeDao.findById(1).getNestedSetLeft());
        assertEquals(14, this.nodeDao.findById(1).getNestedSetRight());
        assertEquals(0, this.nodeDao.findById(1).getOrderInLevel());

        assertEquals(12, this.nodeDao.findById(2).getNestedSetLeft());
        assertEquals(13, this.nodeDao.findById(2).getNestedSetRight());
        assertEquals(3, this.nodeDao.findById(2).getOrderInLevel());

        assertEquals(10, createdNode4.getNestedSetLeft());
        assertEquals(11, createdNode4.getNestedSetRight());
        assertEquals(2, createdNode4.getOrderInLevel());

        assertEquals(8, this.nodeDao.findById(3).getNestedSetLeft());
        assertEquals(9, this.nodeDao.findById(3).getNestedSetRight());
        assertEquals(1, this.nodeDao.findById(3).getOrderInLevel());

        assertEquals(2, createdNode1.getNestedSetLeft());
        assertEquals(7, createdNode1.getNestedSetRight());
        assertEquals(0, createdNode1.getOrderInLevel());

        assertEquals(3, createdNode2.getNestedSetLeft());
        assertEquals(4, createdNode2.getNestedSetRight());
        assertEquals(0, createdNode2.getOrderInLevel());

        assertEquals(5, createdNode3.getNestedSetLeft());
        assertEquals(6, createdNode3.getNestedSetRight());
        assertEquals(1, createdNode3.getOrderInLevel());
    }

    @Test
    public void testAppendNewNode() throws Exception {
        // Testfix erstellen
        setupTestfixture1();
        Tree tree = this.treeDao.findById(1);
        Node parentNode = this.nodeDao.findById(1);

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        Node createdNode = treeService.appendNewNode("Ein neuer Knoten", tree.getId(), parentNode.getId());

        assertNotNull(createdNode);
        assertNotNull(createdNode.getId());
        assertEquals("Ein neuer Knoten", createdNode.getTitle());
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
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        Node createdNode = treeService.prependNewTreeRootNode("Ein neuer Knoten", tree.getId());
        assertNotNull(createdNode);
        assertNotNull(createdNode.getId());
        assertEquals("Ein neuer Knoten", createdNode.getTitle());

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
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        Node createdNode = treeService.prependNewTreeRootNode("Ein neuer Knoten", tree.getId());
        assertNotNull(createdNode);
        assertNotNull(createdNode.getId());
        assertEquals("Ein neuer Knoten", createdNode.getTitle());

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
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        Tree tree = treeService.getTreeById(1);
        assertNotNull(tree);
    }

    @Test(expected = ModelNotFoundException.class)
    public void testGetTreeById2() throws Exception {
        // Testfix erstellen

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        treeService.getTreeById(1);
    }

    @Test(expected = ModelNotFoundException.class)
    public void testGetNodeByTreeAndId1() throws Exception {
        // Testfix erstellen
        setupTestfixture1();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        treeService.getNodeByTreeAndId(2, 1);
        treeService.getNodeByTreeAndId(1, 4);
    }

    @Test(expected = ModelNotFoundException.class)
    public void testGetNodeByTreeAndId2() throws Exception {
        // Testfix erstellen
        setupTestfixture1();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        treeService.getNodeByTreeAndId(1, 4);
    }

    @Test
    public void testGetNodeByTreeAndId3() throws Exception {
        // Testfix erstellen
        setupTestfixture1();

        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

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
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        Collection<Node> treeNodes = treeService.getDirectChildNodes(1, 1);
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
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

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
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        Node root = treeService.getTreeRootNode(1l);
        Assert.assertNotNull(root);
    }

    @Test(expected = ModelNotFoundException.class)
    public void testGetTreeRootNode2() throws Exception {
        // Das Testobjekt erstellen
        TreeServiceImpl treeService = new TreeServiceImpl();
        treeService.setTreeDao(this.treeDao);
        treeService.setNodeDao(this.nodeDao);

        // Test und Auswertung
        treeService.getTreeRootNode(1l);
    }

    /**
     * Konfiguriert einen Testaufbau.
     *
     * @see de.iew.persistence.mock.fixtures.TreeFixture1
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
     * @see de.iew.persistence.mock.fixtures.TreeFixture2
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
     * @see de.iew.persistence.mock.fixtures.TreeFixture3
     */
    private void setupTestfixture3() {
        TreeFixture3 fixture = new TreeFixture3();
        fixture.setNodeDao(this.nodeDao);
        fixture.setTreeDao(this.treeDao);
        fixture.setUp();
    }
}
