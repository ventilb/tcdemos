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

package de.iew.domain.principals;

import de.iew.domain.AbstractModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Beschreibt ein Domainmodell für die Verwaltung eines Accounts.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 16:00
 */
@Entity
@Table(name = "account")
public class Account extends AbstractModel implements UserDetails {

    private String username;

    private String password;

    private boolean locked;

    private boolean enabled;

    private List<Authority> authorities = new ArrayList<Authority>();

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column(columnDefinition = "BIT")
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    // UserDetails Methodenimplementierungen //////////////////////////////////
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_authority", joinColumns = {@JoinColumn(name = "account_id")}, inverseJoinColumns = {@JoinColumn(name = "authority_id")})
    public List<Authority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Column(length = 255)
    public String getPassword() {
        return this.password;
    }

    @Column(length = 255)
    public String getUsername() {
        return this.username;
    }

    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Column(columnDefinition = "BIT")
    public boolean isEnabled() {
        return this.enabled;
    }
}
