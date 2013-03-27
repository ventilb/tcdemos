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
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.message.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Implements test cases to test the jdbc queuing feature of spring integration.
 * <p>
 * According to spring integration documentation it is not recommended to use relation databases for queuing. A better
 * solution would be a dedicated queuing system such as AMQP.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://www.rabbitmq.com/">http://www.rabbitmq.com/</a>
 * @see <a href="http://static.springsource.org/spring-integration/reference/htmlsingle/#jdbc-message-store">http://static.springsource.org/spring-integration/reference/htmlsingle/#jdbc-message-store</a>
 * @since 15.03.13 - 22:27
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-datasource-config.xml", "classpath:spring-integration-jdbc-config.xml"})
public class SpringIntegrationMessageStoreTest {

    @Test
    public void testMessageStore() throws Exception {
        AssertMessageStore assertMessageStore = new AssertMessageStore();
        this.incoming.subscribe(assertMessageStore);

        Map<String, Object> headers = new Hashtable<String, Object>();

        GenericMessage<String> jdbcTestMessage = new GenericMessage<String>("If you can read this, the junit test was successful.", headers);
        this.outgoing.send(jdbcTestMessage);

        Thread.sleep(5000);

        assertEquals(1, assertMessageStore.messageCount);
    }

    @Before
    public void setup() throws Exception {
        IDatabaseConnection dbConn = new DatabaseDataSourceConnection(dataSource);

        ITable testing_spring_integration_jdbc_test = new DefaultTable("testing_spring_integration_message_store_CHANNEL_MESSAGE");

        IDataSet routerTestDataSet = new DefaultDataSet(testing_spring_integration_jdbc_test);

        DatabaseOperation.TRUNCATE_TABLE.execute(dbConn, routerTestDataSet);
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier(value = "messageStoreOutputChannel")
    private SubscribableChannel incoming;

    @Autowired
    @Qualifier("messageStoreInputChannel")
    private QueueChannel outgoing;

    private static class AssertMessageStore implements MessageHandler {

        public int messageCount = 0;

        public void handleMessage(Message<?> message) throws MessagingException {
            this.messageCount++;
        }
    }

}
