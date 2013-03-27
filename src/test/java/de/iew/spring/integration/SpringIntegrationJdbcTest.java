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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.message.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;

/**
 * Implements test cases to test the JDBC capabilities of spring integration.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 08.03.13 - 07:25
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-datasource-config.xml", "classpath:spring-integration-jdbc-config.xml"})
public class SpringIntegrationJdbcTest {

    @Test
    public void testPersistStringMessage() throws Exception {
        Map<String, Object> headers = new Hashtable<String, Object>();

        GenericMessage<String> jdbcTestMessage = new GenericMessage<String>("If you can read this, the junit test was successful.", headers);
        this.simpleOutgoing.send(jdbcTestMessage);

        Statement stmt = this.dataSource.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(id) FROM testing_spring_integration_jdbc_test");
        Assert.assertTrue(rs.next());
        Assert.assertEquals(1, rs.getInt(1));

        // Eine Nachricht eingefügt. Per default im int-jdbc:outbound-gateway wird die Anzahl der aktualisierten Werte
        // geschickt.
        Message m = incoming.receive();
        Map<String, Integer> payload = (Map<String, Integer>) m.getPayload();
        Assert.assertEquals(new Integer(1), payload.get("UPDATED"));
    }

    @Test
    public void testPersistComplexMessage() {
        JdbcTestMessage message = new JdbcTestMessage();
        message.setMessage("If you can read this, the complex junit test was successful.");

        Map<String, Object> headers = new Hashtable<String, Object>();

        GenericMessage<JdbcTestMessage> jdbcTestMessage = new GenericMessage<JdbcTestMessage>(message, headers);
        this.complexOutgoing.send(jdbcTestMessage);

        // Eine Nachricht eingefügt. Per default im int-jdbc:outbound-gateway wird die Anzahl der aktualisierten Werte
        // geschickt.
        Message m = incoming.receive();
        Map<String, Integer> payload = (Map<String, Integer>) m.getPayload();
        Assert.assertEquals(new Integer(1), payload.get("UPDATED"));
    }

    @Before
    public void setup() throws Exception {
        IDatabaseConnection dbConn = new DatabaseDataSourceConnection(dataSource);

        ITable testing_spring_integration_jdbc_test = new DefaultTable("testing_spring_integration_jdbc_test");

        IDataSet routerTestDataSet = new DefaultDataSet(testing_spring_integration_jdbc_test);

        DatabaseOperation.TRUNCATE_TABLE.execute(dbConn, routerTestDataSet);
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("simpleJdbcOutboundChannel")
    private MessageChannel simpleOutgoing;

    @Autowired
    @Qualifier("complexJdbcOutboundChannel")
    private MessageChannel complexOutgoing;

    @Autowired
    @Qualifier("jdbcInboundchannel")
    private QueueChannel incoming;
}
