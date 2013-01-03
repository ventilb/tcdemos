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

import de.iew.domain.principals.Account;

/**
 * Beschreibt eine Schnittstelle für den Zugriff auf
 * {@link Account}-Domainmodelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 16:10
 */
public interface AccountDao extends DomainModelDao<Account> {

    /**
     * Liefert den Account mit dem angegebenen Benutzernamen.
     *
     * @param username Der Benutzername.
     * @return Der Account zu dem Benutzernamen oder NULL wenn kein solcher
     *         Account existiert.
     */
    public Account findAccountByUsername(String username);
}
