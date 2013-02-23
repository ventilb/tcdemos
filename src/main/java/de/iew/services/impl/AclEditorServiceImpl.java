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

package de.iew.services.impl;

import de.iew.framework.domain.DomainModel;
import de.iew.framework.domain.principals.Account;
import de.iew.services.AclEditorService;
import de.iew.services.UserDetailsService;
import de.iew.sketchpad.domain.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementiert einen einfachen ACL-Dienst.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 28.12.12 - 13:28
 */
@Service("aclEditorService")
public class AclEditorServiceImpl implements AclEditorService {

    // Methoden für die ACL-Regeln ////////////////////////////////////////////

    /**
     * Erstellt die ACL-Regeln für die ROLE_SKETCHPAD_ADMIN für das angegebene
     * Polygon. Diese sind für alle Benutzer die selben und daher als eigene
     * Methode aufgeschrieben.
     *
     * @param polygonId Die Id des Polygons.
     */
    public void setupSketchPadPolygonAdminPermissions(long polygonId) {
        MutableAcl polygonAcl = findOrCreateAcl(Polygon.class, polygonId);

        Permission[] allPermissions = new Permission[]{
                BasePermission.READ,
                BasePermission.WRITE,
                BasePermission.CREATE,
                BasePermission.DELETE
        };
        grantAuthorityPermissions(polygonAcl, allPermissions, "ROLE_SKETCHPAD_ADMIN");
    }

    public void setupDemoSketchPadPolygonPermissionsIfSketchPadAdmin(long polygonId) {
        if (this.userDetailsService.isAuthenticatedUserInRole("ROLE_SKETCHPAD_ADMIN")) {
            MutableAcl polygonAcl = createAcl(Polygon.class, polygonId);

            setupSketchPadPolygonAdminPermissions(polygonId);

            Account authenticated = this.userDetailsService.getAuthenticatedUser();
            grantPrincipalPermission(polygonAcl, BasePermission.WRITE, authenticated);
            grantPrincipalPermission(polygonAcl, BasePermission.READ, authenticated);
            grantAuthorityPermission(polygonAcl, BasePermission.READ, "ROLE_SKETCHPAD_USER");
            grantAuthorityPermission(polygonAcl, BasePermission.READ, "ROLE_SKETCHPAD_VISITOR");
        }
    }

    public void setupDemoSketchPadPolygonPermissionsIfSketchPadUser(long polygonId) {
        if (this.userDetailsService.isAuthenticatedUserInRole("ROLE_SKETCHPAD_USER")
                && !this.userDetailsService.isAuthenticatedUserInRole("ROLE_SKETCHPAD_ADMIN")) {
            MutableAcl polygonAcl = createAcl(Polygon.class, polygonId);

            setupSketchPadPolygonAdminPermissions(polygonId);

            Account authenticated = this.userDetailsService.getAuthenticatedUser();

            grantPrincipalPermission(polygonAcl, BasePermission.WRITE, authenticated);
            grantPrincipalPermission(polygonAcl, BasePermission.READ, authenticated);
        }
    }

    // Methoden für die ACL-Verwaltung ////////////////////////////////////////

    public MutableAcl createAcl(Class<? extends DomainModel> domainModelClass, long domainModelId) throws AlreadyExistsException {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(domainModelClass, domainModelId);

        return this.aclService.createAcl(objectIdentity);
    }

    public MutableAcl findAcl(Class<? extends DomainModel> domainModelClass, long domainModelId) throws NotFoundException {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(domainModelClass, domainModelId);
        return (MutableAcl) this.aclService.readAclById(objectIdentity);
    }

    public MutableAcl findOrCreateAcl(Class<? extends DomainModel> domainModelClass, long domainModelId) {
        try {
            return findAcl(domainModelClass, domainModelId);
        } catch (NotFoundException e) {
            return createAcl(domainModelClass, domainModelId);
        }
    }

    public void inheritAclPermissionsFrom(MutableAcl childAcl, MutableAcl parentAcl) {
        childAcl.setEntriesInheriting(true);
        childAcl.setParent(parentAcl);
        this.aclService.updateAcl(childAcl);
    }

    public void grantAuthorityPermission(MutableAcl acl, Permission permission, Object securityIdentity) {
        grantAuthorityPermissionAt(acl, acl.getEntries().size(), permission, securityIdentity);
    }

    public void grantAuthorityPermissionAt(MutableAcl acl, int index, Permission permission, Object securityIdentity) {
        Sid sid = makeAuthoritySid(securityIdentity);

        acl.insertAce(index, permission, sid, true);
        this.aclService.updateAcl(acl);
    }

    public void grantAuthorityPermissions(MutableAcl acl, Permission[] permissions, Object securityidentity) {
        for (Permission permission : permissions) {
            grantAuthorityPermission(acl, permission, securityidentity);
        }
    }

    public void grantPrincipalPermission(MutableAcl acl, Permission permission, Object securityIdentity) {
        grantPrincipalPermissionAt(acl, acl.getEntries().size(), permission, securityIdentity);
    }

    public void grantPrincipalPermissions(MutableAcl acl, Permission[] permissions, Object securityIdentity) {
        /*
        Der einfache Spring-ACL Mechanismus für die ACL Prüfung prüft die
        Bitmaske auf Gleichheit. Daher müssen wir für jede Permission vorerst
        einen eigenen Eintrag erstellen bis wir das Problem gelöst haben.
         */
        for (Permission permission : permissions) {
            grantPrincipalPermission(acl, permission, securityIdentity);
        }
    }

    public void grantPrincipalPermissionAt(MutableAcl acl, int index, Permission permission, Object securityIdentity) {
        Sid sid = makePrincipalSid(securityIdentity);

        acl.insertAce(index, permission, sid, true);
        this.aclService.updateAcl(acl);
    }

    // Hilfsmethoden //////////////////////////////////////////////////////////

    // Do not use: Not yet implemented
    public void isGrantedOrDeny(Acl acl, Permission permission, Object securityIdentity) {
        Sid sid = makePrincipalSid(securityIdentity);

        List<Sid> sids = new ArrayList<Sid>();
        sids.add(sid);

        List<Permission> permissions = new ArrayList<Permission>();
        permissions.add(permission);
        if (!acl.isGranted(permissions, sids, false)) {

        }
    }

    public Sid makeAuthoritySid(Object securityIdentity) {
        if (securityIdentity instanceof GrantedAuthority) {
            return new GrantedAuthoritySid(((GrantedAuthority) securityIdentity).getAuthority());
        } else if (securityIdentity instanceof String) {
            return new GrantedAuthoritySid((String) securityIdentity);
        } else {
            throw new IllegalArgumentException("Unsupported authority security identity " + securityIdentity + ".");
        }
    }

    public Sid makePrincipalSid(Object securityIdentity) {
        if (securityIdentity instanceof UserDetails) {
            return new PrincipalSid(((UserDetails) securityIdentity).getUsername());
        } else if (securityIdentity instanceof Authentication) {
            return makePrincipalSid(((Authentication) securityIdentity).getPrincipal());
        } else if (securityIdentity instanceof String) {
            return new PrincipalSid((String) securityIdentity);
        } else {
            throw new IllegalArgumentException("Unsupported security identity " + securityIdentity + ".");
        }
    }

    // Service und Dao Abhängigkeiten /////////////////////////////////////////

    private MutableAclService aclService;

    private UserDetailsService userDetailsService;

    @Autowired
    public void setAclService(MutableAclService aclService) {
        this.aclService = aclService;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
