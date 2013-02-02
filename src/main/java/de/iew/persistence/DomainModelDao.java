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
 * <p>
 * Wir modellieren explizit DAOs für Domainmodelle. Diese DAOs bieten Methoden
 * für die Verwaltung eines konkreten Domainmodells. In den meisten Fällen sind
 * es CRUD-Methoden. Zusätzliche Methoden, die sich auf das verwaltete
 * Domainmodell beziehen, gehören ebenfalls in diese DAOs.
 * </p>
 * <p>
 * Oft findet man den Fall, dass eine DAO-Methode in mehreren Domainmodell-DAOs
 * implementiert werden könnte. Das führt zu undurchsichtigem Code, da die
 * Wahl des DAOs dann der Willkür des Entwicklers unterliegt. In solchen Fällen
 * werden keine Domainmodell-DAOs implementiert sondern darauf aufsetzende
 * "Operation"-DAOs (Siehe als Beispiel {@link TreeOperationDao}.
 * </p>
 * <pre>
 * +------------------------------------------------+
 * |                Service-Schicht                 |
 * +-----------------+-----------------------+------+
 *                   |                       |
 * +-----------------+-----------------+     |
 * | Operation-DAO mit tollen Methoden |     |
 * +------+----------+---------+-------+     |
 *        |          |         |             |
 *   +----+---+      |     +---+----+    +---+----+
 *   | DM-Dao |      |     | DM-Dao |    | DM-Dao |
 *   +----+---+      |     +---+----+    +---+----+
 *        |          |         |             |
 * +------+----------+---------+-------------+------+
 * |                     Datenbank                  |
 * +------------------------------------------------+
 * </pre>
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
     * Läd den Zustand des angegebenen Domainmodells neu aus dem Backend.
     * <p>
     * Kann verwendet werden, wenn sich der Zustand innerhalb einer Hibernate
     * Sitzung ändert ohne das Hibernate davon erfährt. Zum Beispiel wenn
     * zu dem Objekt ein Trigger gefeuert hat. Kann auch nützlich sein um
     * bidirektionale Assoziationen zu aktualisieren, nachdem nur eine Seite
     * der Assoziation, aus Performancegründen, konfiguriert wurde.
     * </p>
     *
     * @param domainModel Das zu aktualisierende Domainmodell.
     */
    public void refresh(M domainModel);

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

    /**
     * Returns a view portion of the domain model collection.
     * <p>
     * The view starts at <code>firstResult</code> and contains maximum
     * <code>maxResults</code> models.
     * </p>
     *
     *
     * @param firstResult the model number to start from
     * @param maxResults  the maximum model count
     * @return view portion of the domain models
     */
    public Collection<M> findAll(long firstResult, long maxResults);

    /**
     * Counts the available domain models of type <code>M</code>.
     *
     * @return the domain model count
     */
    public long count();
}
