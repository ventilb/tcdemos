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

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Beschreibt ein Domainmodell für die Spezifikation von Datenquellen.
 * <p>
 * Datenquellen sind die zentrale Schnittstelle für die Daten der Anwendung.
 * Mit dieser abstrakten Beschreibung soll erreicht werden, dass die Daten
 * unabhängig von der Strukturierung (Liste, Baum) sind. Die Struktur kann
 * unabhängig von den Daten und umgekehrt verändert werden.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 01:07
 */
@Entity
@Table(name = "data_source")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DataSource extends AbstractModel {

}
