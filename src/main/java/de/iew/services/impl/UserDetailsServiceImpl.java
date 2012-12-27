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
import de.iew.persistence.AccountDao;
import de.iew.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    private AccountDao accountDao;

    public Account loadUserByLoginName(String loginName) throws ModelNotFoundException {
        Account account = getAccountById(1);

        return account;
    }

    public Account getAccountById(long id) throws ModelNotFoundException {
        Account account = this.accountDao.findById(1);

        if (account == null) {
            throw new ModelNotFoundException("The requested account " + id + " was not found.");
        }

        return account;
    }

    // org.springframework.security.core.userdetails.UserDetailsService Implementierung ////////////////////////////////
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            return loadUserByLoginName(s);
        } catch (ModelNotFoundException e) {
            throw new UsernameNotFoundException("The requested account was not found.");
        }
    }

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

}
