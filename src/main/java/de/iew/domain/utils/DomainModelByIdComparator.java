/*
 * Copyright 2012-2013 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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

package de.iew.domain.utils;

import de.iew.domain.DomainModel;

import java.util.Comparator;

/**
 * Provides {@link Comparator} implementations to compare domain models by
 * their id.
 *
 * @author Manuel Schulze <mschulze@geneon.de>
 * @since 02.02.13 - 11:46
 */
public interface DomainModelByIdComparator {

    /**
     * The ASCENDING comparator.
     */
    public static final ByIdComparator ASCENDING = new ByIdComparator();

    /**
     * The comparator implementation.
     */
    public static class ByIdComparator implements Comparator<DomainModel> {
        public int compare(DomainModel o1, DomainModel o2) {
            Long id1 = o1.getId();
            Long id2 = o2.getId();

            if (id1 == null) {
                return 1;
            } else if (id2 == 0) {
                return -1;
            } else {
                return id1.compareTo(id2);
            }
        }
    }
}
