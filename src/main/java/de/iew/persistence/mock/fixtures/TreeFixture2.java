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

import de.iew.domain.Tree;
import de.iew.persistence.NodeDao;
import de.iew.persistence.TreeDao;

import javax.annotation.PostConstruct;

/**
 * Erstellt ein Szenario, das nur einen Baum mit der Id 1 enthält.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 21.11.12 - 19:53
 */
public class TreeFixture2 {

    private TreeDao treeDao;

    private NodeDao nodeDao;

    @PostConstruct
    public void setUp() {
        Tree tree = new Tree();
        tree.setId(1l);
        this.treeDao.save(tree);
    }

    public void setTreeDao(TreeDao treeDao) {
        this.treeDao = treeDao;
    }

    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }
}
