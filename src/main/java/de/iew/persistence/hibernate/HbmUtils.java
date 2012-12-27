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

package de.iew.persistence.hibernate;

import de.iew.domain.DataSource;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

/**
 * Stellt Hibernate Hilfsmethoden bereit.
 * <p>
 * In einigen Situationen ist Hibernate etwas komisch. Beispielsweise liefert
 * es für {@link de.iew.domain.Node#getDataSource()} ein {@link HibernateProxy}
 * Objekt, das nicht direkt verwendet werden kann. Diese Klasse stellt
 * Hilfsmethoden bereit um damit umzugehen. Leider wird dadurch der
 * Anwendungscode abhängig von Hibernate.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 23:25
 */
public class HbmUtils {

    /**
     * Prüft für die angegebene DataSource ob es ein {@link HibernateProxy}
     * ist und liefert gegebenenfalls das reale DataSource Objekt zurück.
     * <p>
     * Wenn die angegebene DataSource kein {@link HibernateProxy} ist
     * dann wird das Objekt unverändert zurückgegeben.
     * </p>
     * <p>
     * Abhängig von der DataSource Hierarchie wird die Subklasse der
     * DataSource zurückgegeben.
     * </p>
     * <pre>
     * DataSource simpleTextDataProxy = someNode.getDataSource();
     * DataSource realSimpleTextData = HbmUtils.fromProxy(simpleTextDataProxy);
     * assertTrue(realSimpleTextData instanceof SimpleTextData);
     * </pre>
     *
     * @param dataSource Die umzuwandelnde DataSource.
     * @return Die umgewandelte DataSource.
     */
    public static DataSource fromProxy(DataSource dataSource) {
        if (dataSource instanceof HibernateProxy) {
            HibernateProxy hibernateProxy = (HibernateProxy) dataSource;
            dataSource = (DataSource) hibernateProxy.getHibernateLazyInitializer().getImplementation();
        }

        return dataSource;
    }

    public static Class determineRealClass(DataSource dataSource) {
        return Hibernate.getClass(dataSource);
    }

    public static String determineRealClassname(DataSource dataSource) {
        return determineRealClass(dataSource).getName();
    }
}
