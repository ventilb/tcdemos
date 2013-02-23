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

package de.iew.framework.domain.principals;

import de.iew.framework.domain.AbstractModel;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Beschreibt das Domainmodell für eine Nutzergruppe.
 * <p>
 * Nutzergruppen stehen konzeptionell höher als Authorities. Als Beispiel
 * lassen sich die Hierarchien in einem Unternehmen als Gruppen beschreiben.
 * In der Regel werden die Authorities den Gruppen zugeordnet.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 14:04
 */
@Entity
@Table(name = "group")
public class Group extends AbstractModel {
}
