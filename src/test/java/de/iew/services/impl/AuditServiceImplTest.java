/*
 * Copyright 2012-2013 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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

import de.iew.framework.domain.audit.Severity;
import de.iew.services.AuditService;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implements a test case for the {@link AuditServiceImpl} class.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.03.13 - 00:18
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application.xml", "classpath:spring-db-config.xml", "classpath:spring-security-config.xml", "classpath:spring-acl-config.xml", "classpath:spring-integration-config.xml", "classpath:spring-mbean-config.xml", "classpath:spring-services-config.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
public class AuditServiceImplTest {
    @Test
    public void testPersistMessageInDatabase() throws Exception {
        int count;

        // Testfix aufbauen
        String source = new String("Message Source");
        Date timestamp = new Date();
        Authentication authentication = newAnonymousAuthentication();
        Severity severity = Severity.INFO;
        String message = "If you can read this, the testPersistMessage() test was successful.";
        Throwable throwable = null;

        // Test durchführen
        /// Der Cast funktioniert, solange wir cglib verwenden, da dort nicht mit Proxies gearbeitet wird sondern mit
        /// Vererbung.
        AuditServiceImpl auditServiceImpl = (AuditServiceImpl) auditService;
        auditServiceImpl.persistMessage(source, timestamp, authentication, severity, message, throwable);

        // Test auswerten
        count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM audit_event_message");
        Assert.assertEquals(1, count);
    }

    private Authentication newAnonymousAuthentication() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
        return new AnonymousAuthenticationToken("anonymous", "anonymous", grantedAuthorities);
    }

    @Before
    public void setup() throws Exception {
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);

        IDatabaseConnection dbConn = new DatabaseDataSourceConnection(dataSource);

        ITable audit_event_message = new DefaultTable("audit_event_message");

        IDataSet routerTestDataSet = new DefaultDataSet(audit_event_message);

        DatabaseOperation.TRUNCATE_TABLE.execute(dbConn, routerTestDataSet);
    }

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuditService auditService;
}
