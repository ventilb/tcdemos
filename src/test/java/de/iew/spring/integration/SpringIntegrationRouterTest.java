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

package de.iew.spring.integration;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.message.GenericMessage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Implements a JUnit test case to test spring integrations routing feature.
 * <pre>
 *                                                         +-----------------------+
 *                                        +----(Normal)----| normalOutboundChannel |--[testing_spring_integration_router_normal_test]
 *                                        |                +-----------------------+
 *        +----------------------+    +---+----+           +----------------------+
 * O -----| routerTestOutChannel |----| router |--(Error)--| errorOutboundChannel |--[testing_spring_integration_router_error_test]
 *        +----------------------+    +--------+           +----------------------+
 * </pre>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 15.03.13 - 22:27
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-datasource-config.xml", "classpath:spring-integration-jdbc-config.xml"})
public class SpringIntegrationRouterTest {

    @Test
    public void testRouteMessageToDatabase() {
        Message m;
        Map<String, Integer> payload;
        int count;

        // Testfix erstellen
        RouterTestMessage normalMessage = new RouterTestMessage();
        normalMessage.setMessage("If you can read this, a normal message was send.");
        normalMessage.setSeverity(RouterTestMessage.Severity.NORMAL);

        Map<String, Object> headers = new Hashtable<String, Object>();

        // Test durchführen
        GenericMessage<RouterTestMessage> normalTestMessage = new GenericMessage<RouterTestMessage>(normalMessage, headers);
        this.outgoing.send(normalTestMessage);

        // Test auswerten
        count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM testing_spring_integration_router_normal_test");
        assertEquals(1, count);

        // Eine Nachricht eingefügt. Per default im int-jdbc:outbound-gateway wird die Anzahl der aktualisierten Werte
        // geschickt.
        m = incoming.receive();
        payload = (Map<String, Integer>) m.getPayload();
        assertEquals(new Integer(1), payload.get("UPDATED"));

        // Testfix erstellen
        RouterTestMessage errorMessage = new RouterTestMessage();
        errorMessage.setMessage("If you can read this, an error message was send.");
        errorMessage.setSeverity(RouterTestMessage.Severity.ERROR);

        // Test durchführen
        GenericMessage<RouterTestMessage> errorTestMessage = new GenericMessage<RouterTestMessage>(errorMessage, headers);
        this.outgoing.send(errorTestMessage);

        // Test auswerten
        count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM testing_spring_integration_router_error_test");
        assertEquals(1, count);

        // Eine Nachricht eingefügt. Per default im int-jdbc:outbound-gateway wird die Anzahl der aktualisierten Werte
        // geschickt.
        m = incoming.receive();
        payload = (Map<String, Integer>) m.getPayload();
        assertEquals(new Integer(1), payload.get("UPDATED"));
    }

    @Before
    public void setup() throws Exception {
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);

        IDatabaseConnection dbConn = new DatabaseDataSourceConnection(dataSource);

        ITable testing_spring_integration_router_normal_test = new DefaultTable("testing_spring_integration_router_normal_test");
        ITable testing_spring_integration_router_error_test = new DefaultTable("testing_spring_integration_router_error_test");

        IDataSet routerTestDataSet = new DefaultDataSet(new ITable[]{testing_spring_integration_router_normal_test, testing_spring_integration_router_error_test});

        DatabaseOperation.TRUNCATE_TABLE.execute(dbConn, routerTestDataSet);
    }

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("routerTestOutChannel")
    private MessageChannel outgoing;

    @Autowired
    @Qualifier("jdbcInboundchannel")
    private QueueChannel incoming;

}
