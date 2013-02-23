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

package de.iew.framework.domain;

import de.iew.framework.persistence.NodeDao;
import de.iew.framework.persistence.SimpleTextDataDao;
import de.iew.framework.persistence.TreeDao;
import de.iew.framework.persistence.TreeOperationDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.*;

/**
 * Integrationstests für das {@link SimpleTextData}-Domainmodell.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 11:11
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application.xml", "classpath:spring-db-config.xml", "classpath:spring-security-config.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional // Wichtig, sonst haben wir hier keine Transaktion und die Testdaten werden nicht zurück gerollt.
public class SimpleTextDataIntegrationTest {

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private TreeDao treeDao;

    @Autowired
    private TreeOperationDao treeOperationDao;

    @Autowired
    private SimpleTextDataDao simpleTextDataDao;

    @Test
    public void testCreateWithoutNode() {
        TextItemCollection textItemCollection = new TextItemCollection();

        TextItem textItem = new TextItem();
        textItem.setContent("Hallo Welt");
        textItem.setLanguageCode("de");
        textItem.setCountryCode("DE");

        SimpleTextData simpleTextData = new SimpleTextData();

        textItemCollection.getTextItems().add(textItem);

        simpleTextData.setTextItemCollection(textItemCollection);

        simpleTextData = this.simpleTextDataDao.save(simpleTextData);

        assertNotNull(simpleTextData.getId());
        assertNotNull(simpleTextData.getTextItemCollection());
        assertNotNull(simpleTextData.getTextItemCollection().getId());
        assertEquals(1, simpleTextData.getTextItemCollection().getTextItems().size());

        for (TextItem textItem1 : simpleTextData.getTextItemCollection()) {
            assertNotNull(textItem1.getId());
        }
    }

    @Test
    public void testCreateWithNode() {
        Node node = new Node();

        Tree tree = new Tree();
        tree.setRoot(node);
        tree = this.treeDao.save(tree);

        TextItemCollection textItemCollection = new TextItemCollection();

        TextItem textItem = new TextItem();
        textItem.setContent("Hallo Welt");
        textItem.setLanguageCode("de");
        textItem.setCountryCode("DE");

        SimpleTextData simpleTextData = new SimpleTextData();

        textItemCollection.getTextItems().add(textItem);

        simpleTextData.setTextItemCollection(textItemCollection);

        simpleTextData = this.simpleTextDataDao.save(simpleTextData);

        //simpleTextData.setNode(tree.getRoot());
        tree.getRoot().setDataSource(simpleTextData);

        //assertEquals(tree.getRoot(), simpleTextData.getNode());
        assertEquals(simpleTextData, tree.getRoot().getDataSource());
    }

}
