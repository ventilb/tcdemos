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

package de.iew.persistence;

import de.iew.domain.Tree;

/**
 * Beschreibt die Schnittstelle für den Zugriff auf
 * {@link de.iew.domain.Tree}-Domainmodelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 07.12.12 - 22:20
 */
public interface TreeDao extends DomainModelDao<Tree> {
    /**
     * Liefert den Baum mit dem angegebenen LookupKey.
     * <p>
     * Die Prüfung ist Case-Insensitive.
     * </p>
     *
     * @param lookupKey Der LookupKey. Darf nicht NULL sein.
     * @return Der angeforderte Baum oder NULL wenn kein Baum für den
     *         LookupKey existiert.
     */
    public Tree findTreeByLookupKey(String lookupKey);

}
