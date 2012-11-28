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

package de.iew.domain;

import javax.persistence.Column;

/**
 * Beschreibt die Schnittstelle für die Implementierung von Baumknoten nach
 * dem NestedSets Verfahren.
 * <p>
 * Das NestedSets Verfahren betrachtet jeden Teilbaum im Baum als Menge. Jede
 * Menge eines Teilbaums ist vollständig in der Menge des Vater-Knotens
 * enthalten. Jede Menge wird durch eine linke und rechte Grenze beschrieben.
 * </p>
 *
 * @author Manuel Schulze <mschulze@geneon.de>
 * @see <a href="http://www.klempert.de/nested_sets/">http://www.klempert.de/nested_sets/</a>
 * @since 29.11.12 - 00:09
 */
public interface NestedSetNode {
    public long getNestedSetLeft();

    public void setNestedSetLeft(long nestedSetLeft);

    public long getNestedSetRight();

    public void setNestedSetRight(long nestedSetRight);

}
