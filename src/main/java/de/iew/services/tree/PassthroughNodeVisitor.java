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

package de.iew.services.tree;

import de.iew.domain.Node;

import java.util.Collection;

/**
 * Implementiert einen {@link NodeVisitor}, der die Eingabe unverändert
 * zurückgibt.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 19:59
 */
public class PassthroughNodeVisitor implements NodeVisitor<Node> {

    public final static NodeVisitor INSTANCE = new PassthroughNodeVisitor();

    public Node visitNode(Node node) {
        return node;
    }

    public Collection<Node> visitNodeCollection(Collection<Node> nodes) {
        return nodes;
    }
}
