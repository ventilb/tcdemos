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

package de.iew.services;

import de.iew.domain.ModelNotFoundException;
import de.iew.domain.principals.Account;

/**
 * Beschreibt die Schnittstelle für einen Dienst zur Nutzerverwaltung.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 15:57
 */
public interface UserDetailsService {

    /**
     * Liefert die Accountdaten zu dem angegebenen Loginnamen.
     * <p>
     * Der Loginname muss eindeutig auflösbar sein.
     * </p>
     *
     * @param loginName Der Loginname.
     * @return Die Accountdaten.
     * @throws ModelNotFoundException Wenn kein Account zum Loginnamen
     *                                existiert.
     */
    public Account loadUserByLoginName(String loginName) throws ModelNotFoundException;
}
