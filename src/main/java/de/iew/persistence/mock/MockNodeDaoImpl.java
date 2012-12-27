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
import de.iew.persistence.NodeDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Mock-Implementierung der {@link NodeDao}-Schnittstelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 10:36
 */
public class MockNodeDaoImpl extends AbstractMockDomainModelDaoImpl<Node> implements NodeDao {
    /**
     * {@inheritDoc}
     * <p>
     * Diese Methode fügt zusätzlich alle Knoten des Baumes des Knotens nach dem
     * save() erneut als Knoten in den Baum ein. Hintergrund, ist, dass das
     * abzuspeichernde Node nach dem save() eine Id erhalten und sich somit der
     * Hashcode geändert hat. Ohne diesen Fix würde man den Knoten nicht mehr im
     * Hashset des Knotens finden.
     * </p>
     */
    @Override
    public Node save(Node domainModel) {
        Node result = super.save(domainModel);

        Tree tree = domainModel.getTree();
        if (tree != null) {
            Node[] nodes = tree.getNodes().toArray(new Node[0]);
            tree.getNodes().clear();
            for (Node node : nodes) {
                tree.getNodes().add(node);
            }

        }

        return result;
    }
}
