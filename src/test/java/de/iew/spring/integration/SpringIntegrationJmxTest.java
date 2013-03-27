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

import de.iew.framework.domain.audit.AuditEventMessage;
import de.iew.framework.domain.audit.Severity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.message.GenericMessage;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.*;

/**
 * JUnit test case to test JMX use-cases.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.03.13 - 08:54
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-beans-config.xml"})
public class SpringIntegrationJmxTest {

    @Test
    public void testJmxClientClassic() throws Exception {
        JMXServiceURL clientURL = new JMXServiceURL(this.serviceUrl);
        Map clientEnv = new HashMap();

        JMXConnector connector = JMXConnectorFactory.connect(clientURL, clientEnv);
        MBeanServerConnection connection = connector.getMBeanServerConnection();

        ObjectName mbeanName = new ObjectName(this.jmxTestServiceObjectName);
        JmxTestService mbeanProxy = JMX.newMBeanProxy(connection, mbeanName,
                JmxTestService.class, true);

        mbeanProxy.setName("Foo Bar");
        mbeanProxy.sayHello();

        String[] hellos = mbeanProxy.getStoredHellos();
        Assert.assertEquals(1, hellos.length);

        mbeanProxy.clearHelloStore();

        hellos = mbeanProxy.getStoredHellos();
        Assert.assertEquals(0, hellos.length);
    }

    @Test
    public void testJmxClientSpring() throws Exception {
        this.jmxTestService.setName("Manuel");
        this.jmxTestService.sayHello();

        String[] hellos = this.jmxTestService.getStoredHellos();
        Assert.assertEquals(1, hellos.length);

        this.jmxTestService.clearHelloStore();

        hellos = this.jmxTestService.getStoredHellos();
        Assert.assertEquals(0, hellos.length);

        // Etwas warten, bis die Notifications verschickt wurden
        Thread.sleep(3000);
        // Wir feuern zwar nur einmal in sayHello() aber auch der setName() (Attribut Änderung) wurde gemeldet
        Assert.assertEquals(2, this.jmxTestServiceNotificationListener.getNotifications().size());
    }

    @Test
    public void testJmxSpringIntegration() throws Exception {
        // Testfix erstellen

        // Test durchführen
        Map<String, Object> headers = new Hashtable<String, Object>();
        GenericMessage<String> message = new GenericMessage<String>("Foo Bar", headers);
        this.messageChannel.send(message);

        // Test auswerten
        /// Etwas warten, bis die Notifications verschickt wurden
        Thread.sleep(3000);

        Assert.assertEquals(1, this.springIntegrationTestNotificationListener.getNotifications().size());

        Notification notification = this.springIntegrationTestNotificationListener.getNotifications().get(0);

        Assert.assertEquals(String.class.getName(), notification.getType());
        Assert.assertEquals("Foo Bar", notification.getMessage());
    }

    @Test
    public void testJmxSpringIntegrationWithAuditEventMessage() throws Exception {
        // Testfix erstellen
        long timestamp = System.currentTimeMillis();

        Authentication authentication = newAnonymousAuthentication();

        AuditEventMessage auditEventMessage = new AuditEventMessage();
        auditEventMessage.setTimestamp(new Date(timestamp));
        auditEventMessage.setPrincipal(authentication.getName());
        auditEventMessage.setSeverity(Severity.INFO);
        auditEventMessage.setMessage("Foo Bar");

        // Test durchführen
        Map<String, Object> headers = new Hashtable<String, Object>();
        GenericMessage<AuditEventMessage> message = new GenericMessage<AuditEventMessage>(auditEventMessage, headers);
        this.messageChannel.send(message);

        // Test auswerten
        /// Etwas warten, bis die Notifications verschickt wurden
        Thread.sleep(3000);

        Assert.assertEquals(1, this.springIntegrationTestNotificationListener.getNotifications().size());

        Notification notification = this.springIntegrationTestNotificationListener.getNotifications().get(0);

        Assert.assertEquals(AuditEventMessage.class.getName(), notification.getType());
        Assert.assertNull(notification.getMessage());

        AuditEventMessage userData = (AuditEventMessage) notification.getUserData();
        Assert.assertEquals("Foo Bar", userData.getMessage());
        Assert.assertEquals(Severity.INFO, userData.getSeverity());
        Assert.assertEquals(new Date(timestamp), userData.getTimestamp());
    }

    private Authentication newAnonymousAuthentication() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
        return new AnonymousAuthenticationToken("anonymous", "anonymous", grantedAuthorities);
    }

    // Allgemeine Eigenschaften ///////////////////////////////////////////////

    @Value(value = "#{configuration.serviceUrl}")
    private String serviceUrl;

    @Autowired
    private MBeanServerConnection clientConnector;

    // Eigenschaften für den einfachen JMX Service Test 7//////////////////////

    @Autowired
    @Qualifier("jmxTestServiceProxy")
    private JmxTestService jmxTestService;

    @Autowired
    private JmxTestServiceNotificationListener jmxTestServiceNotificationListener;

    @Value(value = "#{configuration.jmxTestServiceObjectName}")
    private String jmxTestServiceObjectName;

    // Eigenschaften für den JMX und Spring Integration Test //////////////////

    @Autowired
    @Qualifier(value = "auditEventMessageJmxChannel")
    private MessageChannel messageChannel;

    @Value(value = "#{configuration.springIntegrationTestObjectName}")
    private String springIntegrationTestObjectName;

    @Autowired
    private JmxTestServiceNotificationListener springIntegrationTestNotificationListener;

    @Before
    public void setUp() throws Exception {
        ObjectName objectName = ObjectName.getInstance(this.jmxTestServiceObjectName);
        this.clientConnector.addNotificationListener(objectName, this.jmxTestServiceNotificationListener, null, null);

        objectName = ObjectName.getInstance(this.springIntegrationTestObjectName);
        this.clientConnector.addNotificationListener(objectName, this.springIntegrationTestNotificationListener, null, null);
    }

    @After
    public void tearDown() throws Exception {
        ObjectName objectName = ObjectName.getInstance(this.jmxTestServiceObjectName);
        this.clientConnector.removeNotificationListener(objectName, this.jmxTestServiceNotificationListener);

        objectName = ObjectName.getInstance(this.springIntegrationTestObjectName);
        this.clientConnector.removeNotificationListener(objectName, this.springIntegrationTestNotificationListener);
    }
}