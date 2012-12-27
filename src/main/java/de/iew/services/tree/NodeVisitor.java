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
 * Beschreibt eine Schnittstelle, die beim Iterieren über
 * {@link Node}-Domainmodelle Operationen auf den besuchten
 * Knoten ermöglicht.
 * <p>
 * Zum Beispiel lässt sich mit dieser Schnittstelle eine Rechteprüfung auf
 * Knoten implementieren.
 * </p>
 * <p>
 * Dieses Interface ist inspiriert durch Hibernates {@link org.hibernate.transform.ResultTransformer}
 * Schnittstelle.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 19:39
 */
public interface NodeVisitor<M> {

    /**
     * Wird aufgerufen wenn der angegebene Knoten besucht wird.
     *
     * @param node Der besuchte Knoten.
     * @return Das Ergebnis des Besuchs (Implementierungsabhängig).
     */
    M visitNode(Node node);

    /**
     * Wird aufgerufen wenn die angegebene Knoten-Sammlung als Ganzes besucht
     * werden soll.
     *
     *
     *
     * @param nodes Die besuchte Knoten-Sammlung.
     * @return Das Ergebnis des Besuchs (Implementierungsabhängig).
     */
    Collection<M> visitNodeCollection(Collection<Node> nodes);
}
