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

package de.iew.framework.domain.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstrakte Implementierung der {@link DomainModelVisitor}-Schnittstelle.
 * <p>
 * Stellt eine Standardimplementierung für {@link #visitCollection(java.util.Collection)}
 * bereit.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 03.01.13 - 02:31
 */
public abstract class AbstractDomainModelVisitor<IN, OUT> implements DomainModelVisitor<IN, OUT> {

    public Collection<OUT> visitCollection(Collection<IN> domainModels) {
        Collection<OUT> out = new ArrayList<OUT>(domainModels.size());

        for (IN in : domainModels) {
            out.add(visit(in));
        }

        return out;
    }

}
