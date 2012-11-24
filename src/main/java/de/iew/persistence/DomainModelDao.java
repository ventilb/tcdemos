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

import de.iew.domain.AbstractModel;

import java.util.Collection;

/**
 * Beschreibt eine allgemeine Schnittstelle für den Zugriff auf die
 * Domainmodelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 10:09
 */
public interface DomainModelDao<M extends AbstractModel> {

    /**
     * Speichert das angegebene Domainmodell und liefert eine gespeicherte
     * Kopie zurück.
     * <p>
     * Das abgespeicherte Domainmodell (der Rückgabewert) kann, muss aber
     * nicht das selbe Objekt sein, wie <code>domainModel</code>.
     * </p>
     *
     * @param domainModel Das zu speichernde Domainmodell.
     * @return Das abgespeicherte Domainmodell.
     */
    public M save(M domainModel);

    /**
     * Entfernt das angegebene Domainmodell.
     *
     * @param domainModel Das zu entfernende Domainmodell.
     */
    public void remove(M domainModel);

    /**
     * Liefert das Domainmodell mit der angegebenen Id.
     *
     * @param id Die Id des Domainmodells.
     * @return Das Domaonmodell oder NULL wenn kein solches Domainmodell
     *         existiert.
     */
    public M findById(long id);

    /**
     * Liefert alle verwalteten Domainmodelle.
     * <p>
     * Diese Methode macht keine Zusicherung über Sortierung der gelieferten
     * Domainmodelle.
     * </p>
     *
     * @return Auflistung der verwalteten Domainmodelle.
     */
    public Collection<M> findAll();
}
