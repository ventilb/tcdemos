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
import de.iew.domain.Tree;
import de.iew.persistence.TreeDao;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Klassenkommentar.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 10:08
 */
public class MockTreeDaoImpl extends AbstractMockDomainModelDaoImpl<Tree> implements TreeDao {

    public Node findRootNodeForTree(long treeId) {
        Node treeRoot = null;

        for (Tree tree : findAll()) {
            if (tree.getId() == treeId) {
                treeRoot = tree.getRoot();
            }
        }

        return treeRoot;
    }

    public Node findNodeForTreeAndId(long treeId, long nodeId) {
        for (Tree tree : findAll()) {
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

}
