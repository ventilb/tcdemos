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

package de.iew.domain.utils;

import java.util.Collection;

/**
 * Beschreibt eine Schnittstelle, die beim Iterieren über Domainmodelle
 * Operationen auf den besuchten Modellen ermöglicht.
 * <p>
 * Zum Beispiel lässt sich mit dieser Schnittstelle eine Rechteprüfung der
 * Modelle implementieren oder das Domainmodell von einer Darstellung in
 * eine Andere überführen.
 * </p>
 * <p>
 * Dieses Interface ist inspiriert durch Hibernates {@link org.hibernate.transform.ResultTransformer}
 * Schnittstelle.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 19:39
 */
public interface DomainModelVisitor<IN, OUT> {

    /**
     * Wird aufgerufen wenn das angegebene Domainmodell besucht wird.
     *
     * @param domainModel Das besuchte Domainmodell.
     * @return Das Ergebnis des Besuchs (Implementierungsabhängig).
     */
    OUT visit(IN domainModel);

    /**
     * Wird aufgerufen wenn die angegebene Domainmodell-Sammlung als Ganzes besucht
     * werden soll.
     *
     * @param domainModels Die besuchte Domainmodell-Sammlung.
     * @return Das Ergebnis des Besuchs (Implementierungsabhängig).
     */
    Collection<OUT> visitCollection(Collection<IN> domainModels);
}
