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

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Testet die {@link Order}-Schnittstelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 08.12.12 - 12:43
 */
public class OrderTest {

    @Test
    public void testDefaultComparator() {
        // Testfix erstellen
        MockOrder mo1 = new MockOrder();
        mo1.setOrdinalNumber(0);

        MockOrder mo2 = new MockOrder();
        mo2.setOrdinalNumber(1);

        // Das Testobjekt erstellen
        Order.DefaultComparator comparator = new Order.DefaultComparator();

        // Test und Auswertung
        assertTrue(comparator.compare(mo1, mo2) < 0);
        assertTrue(comparator.compare(mo2, mo1) > 0);

        // Testfix erstellen
        mo1 = new MockOrder();
        mo1.setOrdinalNumber(2);

        mo2 = new MockOrder();
        mo2.setOrdinalNumber(2);

        // Das Testobjekt erstellen
        comparator = new Order.DefaultComparator();

        // Test und Auswertung
        assertTrue(comparator.compare(mo1, mo2) == 0);
        assertTrue(comparator.compare(mo2, mo1) == 0);

        // Testfix erstellen
        mo1 = new MockOrder();
        mo1.setOrdinalNumber(5);

        mo2 = new MockOrder();
        mo2.setOrdinalNumber(2);

        // Das Testobjekt erstellen
        comparator = new Order.DefaultComparator();

        // Test und Auswertung
        assertTrue(comparator.compare(mo1, mo2) > 0);
        assertTrue(comparator.compare(mo2, mo1) < 0);

        // Testfix erstellen
        mo1 = new MockOrder();
        mo1.setOrdinalNumber(-1);

        mo2 = new MockOrder();
        mo2.setOrdinalNumber(0);

        // Das Testobjekt erstellen
        comparator = new Order.DefaultComparator();

        // Test und Auswertung
        assertTrue(comparator.compare(mo1, mo2) < 0);
        assertTrue(comparator.compare(mo2, mo1) > 0);

        // Testfix erstellen
        mo1 = new MockOrder();
        mo1.setOrdinalNumber(-2);

        mo2 = new MockOrder();
        mo2.setOrdinalNumber(-1);

        // Das Testobjekt erstellen
        comparator = new Order.DefaultComparator();

        // Test und Auswertung
        assertTrue(comparator.compare(mo1, mo2) < 0);
        assertTrue(comparator.compare(mo2, mo1) > 0);

        // Testfix erstellen
        mo1 = new MockOrder();
        mo1.setOrdinalNumber(-2);

        mo2 = new MockOrder();
        mo2.setOrdinalNumber(-3);

        // Das Testobjekt erstellen
        comparator = new Order.DefaultComparator();

        // Test und Auswertung
        assertTrue(comparator.compare(mo1, mo2) > 0);
        assertTrue(comparator.compare(mo2, mo1) < 0);
    }

    private static class MockOrder implements Order {

        private int ordinalNumber;

        public int getOrdinalNumber() {
            return this.ordinalNumber;
        }

        public void setOrdinalNumber(int ordinalNumber) {
            this.ordinalNumber = ordinalNumber;
        }
    }
}
