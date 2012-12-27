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

import java.util.Comparator;

/**
 * Beschreibt eine Schnittstelle mit der eine Ordnungszahl für ein Domainmodell
 * definiert werden kann.
 * <p>
 * Mit diesem Interface kann eine mathematische Ordnungsrelation für das
 * Domainmodell definiert werden. Durch dieses Interface können verallgemeinerte
 * Implementierungen für {@link Comparable} und {@link java.util.Comparator}
 * bereitgestellt werden.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 08.12.12 - 12:13
 */
public interface Order {

    /**
     * Wiederverwendbares Singleton-Exemplar für Vergleiche auf Basis dieser
     * {@link Order}-Schnittstelle.
     */
    public static final Comparator<Order> ASCENDING = new DefaultComparator();

    public int getOrdinalNumber();

    public void setOrdinalNumber(int ordinalNumber);

    /**
     * Standard Implementierung der {@link Comparator}-Schnittstelle.
     */
    public static class DefaultComparator implements Comparator<Order> {

        public int compare(Order o1, Order o2) {
            int ord1 = o1.getOrdinalNumber();
            int ord2 = o2.getOrdinalNumber();

            return ord1 - ord2;
        }
    }
}
