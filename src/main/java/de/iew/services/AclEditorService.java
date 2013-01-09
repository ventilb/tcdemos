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
import org.springframework.security.acls.model.NotFoundException;
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
     * Erstellt eine ACL zur Demonstration der ACL Fähigkeiten von Spring wenn
     * der Nutzer in der Rolle ROLE_SKETCHPAD_ADMIN ist.
     * <pre>
     *         | SKETCHPAD_ADMIN | SKETCHPAD_USER | SKETCHPAD_VISITOR | Eigentümer
     * --------+-----------------+----------------+-------------------+-----------
     *   READ  |        x        |        x       |         x         |     x
     * --------+-----------------+----------------+-------------------+-----------
     *  WRITE  |        x        |                |                   |     x
     * --------+-----------------+----------------+-------------------+-----------
     *  CREATE |        x        |                |                   |
     * --------+-----------------+----------------+-------------------+-----------
     *  DELETE |        x        |                |                   |
     * --------+-----------------+----------------+-------------------+-----------
     * </pre>
     *
     * @param polygonId Die Id des Polygons.
     */
    public void setupDemoSketchPadPolygonPermissionsIfSketchPadAdmin(long polygonId);

    /**
     * Erstellt eine ACL zur Demonstration der ACL Fähigkeiten von Spring wenn
     * der Nutzer in der Rolle ROLE_SKETCHPAD_USER ist.
     * <pre>
     *         | SKETCHPAD_ADMIN | SKETCHPAD_USER | SKETCHPAD_VISITOR | Eigentümer
     * --------+-----------------+----------------+-------------------+-----------
     *   READ  |        x        |                |                   |     x
     * --------+-----------------+----------------+-------------------+-----------
     *  WRITE  |        x        |                |                   |     x
     * --------+-----------------+----------------+-------------------+-----------
     *  CREATE |        x        |                |                   |
     * --------+-----------------+----------------+-------------------+-----------
     *  DELETE |        x        |                |                   |
     * --------+-----------------+----------------+-------------------+-----------
     * </pre>
     *
     * @param polygonId Die Id des Polygons.
     */
    public void setupDemoSketchPadPolygonPermissionsIfSketchPadUser(long polygonId);

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
     * Liefert die vorhandene ACL für das angegebene Domainmodell.
     *
     * @param domainModelClass Die Klasse des Domainmodells.
     * @param domainModelId    Die Id des Domainmodells.
     * @return Die ACL zum Domainmodell
     * @throws NotFoundException Wenn das Domainmodell keine ACL hat.
     */
    public MutableAcl findAcl(Class<? extends DomainModel> domainModelClass, long domainModelId) throws NotFoundException;

    /**
     * Liefert die vorhandene ACL für das angegebene Domainmodell und erstellt
     * falls nicht vorhanden eine neue ACL für das Domainmodell.
     *
     * @param domainModelClass Die Klasse des Domainmodells.
     * @param domainModelId    Die Id des Domainmodells.
     * @return Die ACL zum Domainmodell.
     */
    public MutableAcl findOrCreateAcl(Class<? extends DomainModel> domainModelClass, long domainModelId);

    /**
     * Erstellt eine Vater-Kind-Beziehung zwischen <code>parentAcl</code> und
     * <code>childAcl</code> und konfiguriert die ACLs so, dass auch die
     * Regeln vom Vater an das Kind vererbt werden.
     *
     * @param childAcl  Die Kind ACL. Erbt die Regeln vom Vater.
     * @param parentAcl Die Vater ACL. Reicht die Regeln an das Kind weiter.
     */
    public void inheritAclPermissionsFrom(MutableAcl childAcl, MutableAcl parentAcl);

    /**
     * Konfiguriert das angegebene Zugriffsrecht für die angegebene
     * Gruppen-Identität in der angegebenen ACL.
     * <p>
     * Das Zugriffsrecht wird an das Ende vorhandener Zurgiffsrechte angehängt.
     * </p>
     *
     * @param acl        Die ACL.
     * @param permission Das Zugriffsrecht.
     * @param authority  Die Gruppen-Identität.
     */
    public void grantAuthorityPermission(MutableAcl acl, Permission permission, Object authority);

    /**
     * Konfiguriert die angegebenen Zugriffsrechte für die angegebene
     * Gruppen-Identität in der angegebenen ACL.
     * <p>
     * Die Zugriffsrechte werden in der angegebenen Reihenfolge an die ACL
     * gehängt.
     * </p>
     *
     * @param acl         Die ACL.
     * @param permissions Liste der Zugriffsrechte.
     * @param authority   Die Gruppen-Identität.
     */
    public void grantAuthorityPermissions(MutableAcl acl, Permission[] permissions, Object authority);

    /**
     * Konfiguriert das angegebene Zugriffsrecht für die angegebene Identität
     * in der angegebenen ACL.
     * <p>
     * Das Zugriffsrecht wird an das Ende vorhandener Zurgiffsrechte angehängt.
     * </p>
     *
     * @param acl              Die ACL.
     * @param permission       Das Zugriffsrecht.
     * @param securityIdentity Die Identität.
     */
    public void grantPrincipalPermission(MutableAcl acl, Permission permission, Object securityIdentity);

    /**
     * Konfiguriert die angegebenen Zugriffsrechte für die angegebene Identität
     * in der angegebenen ACL.
     * <p>
     * Die Zugriffsrechte werden in der angegebenen Reihenfolge an die ACL
     * gehängt.
     * </p>
     *
     * @param acl              Die ACL.
     * @param permissions      Liste der Zugriffsrechte.
     * @param securityIdentity Die Identität.
     */
    public void grantPrincipalPermissions(MutableAcl acl, Permission[] permissions, Object securityIdentity);
}
