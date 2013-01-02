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

import de.iew.domain.DomainModel;
import de.iew.services.AclEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementiert einen einfachen ACL-Dienst.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 28.12.12 - 13:28
 */
@Service("aclEditorService")
public class AclEditorServiceImpl implements AclEditorService {

    private MutableAclService aclService;

    public MutableAcl createAcl(Class<? extends DomainModel> domainModelClass, long domainModelId) throws AlreadyExistsException {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(domainModelClass, domainModelId);

        return this.aclService.createAcl(objectIdentity);
    }

    public void grantPermission(MutableAcl acl, Permission permission, Object securityIdentity) {
        grantPermissionAt(acl, acl.getEntries().size(), permission, securityIdentity);
    }

    public void grantPermissionAt(MutableAcl acl, int index, Permission permission, Object securityIdentity) {
        Sid sid;
        if (securityIdentity instanceof UserDetails) {
            sid = new PrincipalSid(((UserDetails) securityIdentity).getUsername());
        } else if (securityIdentity instanceof GrantedAuthority) {
            sid = new GrantedAuthoritySid(((GrantedAuthority) securityIdentity).getAuthority());
        } else if (securityIdentity instanceof UsernamePasswordAuthenticationToken) {
            grantPermissionAt(acl, index, permission, ((UsernamePasswordAuthenticationToken) securityIdentity).getPrincipal());
            return;
        } else {
            throw new IllegalArgumentException("Unsupported security identity " + securityIdentity + ".");
        }

        acl.insertAce(index, permission, sid, true);
        this.aclService.updateAcl(acl);
    }

    @Autowired
    public void setAclService(MutableAclService aclService) {
        this.aclService = aclService;
    }

}
