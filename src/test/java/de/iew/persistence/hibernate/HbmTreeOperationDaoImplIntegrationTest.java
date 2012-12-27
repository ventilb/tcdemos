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

package de.iew.persistence.hibernate;

import de.iew.domain.Node;
import de.iew.domain.Tree;
import de.iew.persistence.NodeDao;
import de.iew.persistence.TreeDao;
import de.iew.persistence.TreeOperationDao;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.*;

/**
 * Integrationstest zum Testen der Hibernate-Baumfunktionen.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 15:22
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application.xml", "classpath:spring-db-config.xml", "classpath:spring-security-config.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional // Wichtig, sonst haben wir hier keine Transaktion und die Testdaten werden nicht zurück gerollt.
public class HbmTreeOperationDaoImplIntegrationTest {

    @Autowired
    private TreeOperationDao treeOperationDao;

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private TreeDao treeDao;

    @Test
    @Ignore
    public void testFixNodeLevelOrders() {
        // Testfix erstellen
        Tree tree = new Tree();
        tree = this.treeDao.save(tree);

        Node parent = new Node();
        parent.setOrdinalNumber(0);
        parent = this.nodeDao.save(parent);

        Node child1 = new Node();
        child1.setTree(tree);
        child1.setOrdinalNumber(1);
        child1 = this.nodeDao.save(child1);

        Node child2 = new Node();
        child2.setTree(tree);
        child2.setOrdinalNumber(3);
        child2 = this.nodeDao.save(child2);

        tree.setRoot(parent);

        parent.setTree(tree);
        tree.getNodes().add(parent);

        child1.setTree(tree);
        tree.getNodes().add(child1);

        child2.setTree(tree);
        tree.getNodes().add(child2);

        child1.setParent(parent);
        parent.getChildren().add(child1);

        child2.setParent(parent);
        parent.getChildren().add(child2);

        // Das Testobjekt erstellen
        //this.treeOperationDao.fixNodeLevelOrders(tree.getId(), parent.getId());

        // Test und Auswertung
        assertEquals(0, this.nodeDao.findById(child1.getId()).getOrdinalNumber());
        assertEquals(1, this.nodeDao.findById(child2.getId()).getOrdinalNumber());

    }

    @Test
    public void testFindRootNodeForTree() throws Exception {
        Node node = this.treeOperationDao.findRootNodeForTree(1);
        assertNotNull(node.getTree());
    }
}
