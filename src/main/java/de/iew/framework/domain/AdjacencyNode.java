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

import java.util.List;

/**
 * Beschreibt eine Schnittstelle für die Implementierung von Baumknoten nach
 * dem Adjazenzlisten Verfahren.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 29.11.12 - 00:02
 */
public interface AdjacencyNode<N> {
    /**
     * Liefert den Vater dieses Knotens. Liefert NULL wenn dieser Knoten
     * die Wurzel ist und keinen Vater hat.
     *
     * @return Den Vater dieses Knotens.
     */
    public N getParent();

    /**
     * Konfiguriert den Vater dieses Knotens. Ersetzt einen evtl. vorhandenen
     * Vater.
     *
     * @param parent Der Vater dieses Knotens. Darf NULL sein.
     */
    public void setParent(N parent);

    /**
     * Liefert die Kinder dieses Knotens. Liefert eine leere Liste wenn dieser
     * Knoten keine Kinder hat.
     *
     * @return Die Kinder dieses Knotens.
     */
    public List<N> getChildren();

    /**
     * Konfiguriert die Kinder dieses Knotens auf children.
     *
     * @param children Die Liste der Kinder dieses Knotens. Darf nicht NULL
     *                 sein.
     */
    public void setChildren(List<N> children);

}
