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

import de.iew.domain.ModelNotFoundException;
import de.iew.domain.principals.Account;
import de.iew.domain.principals.Authority;
import de.iew.persistence.AccountDao;
import de.iew.persistence.AuthorityDao;
import de.iew.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Implementiert zusätzlich auch {@link org.springframework.security.core.userdetails.UserDetailsService}.
 * Damit kann diese Service-Implementierung auch gleichzeitig in Springs
 * Authentifizierungsframework verwendet werden.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 15:58
 */
@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService, org.springframework.security.core.userdetails.UserDetailsService {

    private String administrativeAuthoritySystemName = "ROLE_ADMIN";

    public Account getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // TODO: Prüfen ob wirklich eingeloggt. siehe dafür SecurityContextHolderAwareRequestWrapper Klasse

        Object principal = auth.getPrincipal();
        if (principal instanceof Account) {
            return (Account) principal;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Die Implementierung wurde von Spring übernommen.
     * </p>
     *
     * @see {SecurityContextHolderAwareRequestWrapper#isGranted}
     */
    public boolean isAuthenticatedUserInRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (role.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }

        return false;
    }

    public Account loadUserByLoginName(String loginName) throws ModelNotFoundException {
        Account account = this.accountDao.findAccountByUsername(loginName);

        if (account == null) {
            throw new ModelNotFoundException("The requested account " + loginName + " was not found.");
        }

        return account;
    }

    public Account getAccountById(long id) throws ModelNotFoundException {
        Account account = this.accountDao.findById(1);

        if (account == null) {
            throw new ModelNotFoundException("The requested account " + id + " was not found.");
        }

        return account;
    }

    public Authority getAdministrativeAuthority() throws ModelNotFoundException, UnsupportedOperationException {
        if (this.administrativeAuthoritySystemName == null || "".equals(this.administrativeAuthoritySystemName.trim())) {
            throw new UnsupportedOperationException("getAdministrativeAuthority() is not supported. The administrativeAuthoritySystemName property is not configured.");
        }
        Authority authority = this.authorityDao.findBySystemName(this.administrativeAuthoritySystemName);

        if (authority == null) {
            throw new ModelNotFoundException("The configured administrative authority " + this.administrativeAuthoritySystemName + " was not found.");
        }

        return authority;
    }

    // org.springframework.security.core.userdetails.UserDetailsService Implementierung ////////////////////////////////
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            return loadUserByLoginName(s);
        } catch (ModelNotFoundException e) {
            throw new UsernameNotFoundException("The requested account was not found.");
        }
    }

    // Service und Dao Abhängigkeiten /////////////////////////////////////////

    private AccountDao accountDao;

    private AuthorityDao authorityDao;

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Autowired
    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    public void setAdministrativeAuthoritySystemName(String administrativeAuthoritySystemName) {
        this.administrativeAuthoritySystemName = administrativeAuthoritySystemName;
    }
}
