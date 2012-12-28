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

package de.iew.domain;

/**
 * Beschreibung für einen Stereotypen um Java-Klassen als Domainmodell zu
 * deklarieren.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 11:37
 */
public interface DomainModel {
    /**
     * Liefert die Id dieses Domainmodells oder NULL wenn dieses Domainmodell
     * keine Id hat.
     *
     * @return Die Id dieses Domainmodells.
     */
    public Long getId();

    /**
     * Konfiguriert die Id dieses Domainmodells.
     *
     * @param id Die Id dieses Domainmodells oder NULL.
     */
    public void setId(Long id);
}
