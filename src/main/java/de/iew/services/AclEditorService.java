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

import de.iew.domain.DomainModel;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Permission;

/**
 * Beschreibt die Schnittstelle für einen Dienst zur Verwaltung der ACL
 * Einträge für Domainmodelle und Berechtigungen an diesen Modellen.
 * <p>
 * Die Schnittstelle basiert auf Spring Security ACL. Die Idee ist, für jedes
 * Domainmodell eine ACL zu pflegen. Für jede ACL werden die Berechtigungen an
 * diesem Domainmodell konfiguriert. ACLs können aber von anderen ACLs erben.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://static.springsource.org/spring-security/site/docs/3.1.x/reference/domain-acls.html">http://static.springsource.org/spring-security/site/docs/3.1.x/reference/domain-acls.html</a>
 * @since 28.12.12 - 13:20
 */
public interface AclEditorService {

    /**
     * Erstellt eine neue ACL für das angegebene Domainmodell.
     *
     * @param domainModelClass Die Klasse des Domainmodells.
     * @param domainModelId    Die Id des Domainmodells.
     * @return Die erstellte ACL.
     * @throws AlreadyExistsException Wird gemeldet wenn die ACL zu dem Domainmodell bereits existiert.
     */
    public MutableAcl createAcl(Class<? extends DomainModel> domainModelClass, long domainModelId) throws AlreadyExistsException;

    /**
     * Konfiguriert das angegebene Zugriffsrecht für die angegebene Identität
     * in der angegebenen ACL. Das Zugriffsrecht wird an das Ende vorhandener
     * Zurgiffsrechte angehängt.
     *
     * @param acl              Die ACL.
     * @param permission       Das Zugriffsrecht.
     * @param securityIdentity Die Identität.
     */
    public void grantPermission(MutableAcl acl, Permission permission, Object securityIdentity);
}
