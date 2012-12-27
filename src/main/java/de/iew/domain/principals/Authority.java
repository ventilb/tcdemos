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

import de.iew.domain.DataSource;
import de.iew.domain.TextItemCollection;
import de.iew.domain.annotations.DSTitleProperty;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Beschreibt ein Domainmodell für die Verwaltung einer Authority.
 * <p>
 * Authorities bezeichnen Rollen im System und umfassen in der Regel eine
 * Aufgabe, wie zum Beispiel Datenbank Administrator. Über Authorities kann
 * der Zugriff auf Domainmodelle und Funktionen durch die ACL gesteuert werden.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 18:22
 */
@Entity
@Table(name = "authority")
@PrimaryKeyJoinColumn(name = "id")
public class Authority extends DataSource implements GrantedAuthority {
    private String systemName;

    private TextItemCollection displayName;

    @Column(name = "system_name", length = 255)
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    @DSTitleProperty
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "display_name_id")
    public TextItemCollection getDisplayName() {
        return displayName;
    }

    public void setDisplayName(TextItemCollection displayName) {
        this.displayName = displayName;
    }

    // GrantedAuthority Implementierungen /////////////////////////////////////
    @Transient
    public String getAuthority() {
        return this.systemName;
    }
}
