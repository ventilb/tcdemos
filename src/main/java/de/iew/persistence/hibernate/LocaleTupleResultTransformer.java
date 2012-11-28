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

package de.iew.persistence.hibernate;

import org.hibernate.transform.ResultTransformer;

import java.util.List;
import java.util.Locale;

/**
 * Implementiert einen Hibernate {@link ResultTransformer}, der aus zwei
 * Elementen des Ergebnis-Tupels ein {@link Locale} Objekt erstellt.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 26.11.12 - 21:30
 */
public class LocaleTupleResultTransformer implements ResultTransformer {

    /**
     * Standard LocaleTupleResultTransformer, der an Index 0 den Sprach-Code
     * und an Index 1 den Länder-Code erwartet.
     */
    public static final LocaleTupleResultTransformer DEFAULT = new LocaleTupleResultTransformer(0, 1);

    private final int languageCodeIndex;

    private final int countryCodeIndex;

    public LocaleTupleResultTransformer(int languageCodeIndex, int countryCodeIndex) {
        this.languageCodeIndex = languageCodeIndex;
        this.countryCodeIndex = countryCodeIndex;
    }

    public Object transformTuple(Object[] tuple, String[] aliases) {
        return new Locale((String) tuple[this.languageCodeIndex], (String) tuple[this.countryCodeIndex]);
    }

    public List transformList(List collection) {
        return collection;
    }
}
