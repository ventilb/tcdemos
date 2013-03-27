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

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;

import javax.management.Notification;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a JMX test service for JUnit.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.03.13 - 09:22
 */
@ManagedResource(objectName = "modelmbean:name=JmxTestService", description = "JUnit test service")
public class JmxTestServiceImpl implements JmxTestService, NotificationPublisherAware {
    private String name;

    private List<String> helloStore = new ArrayList<String>();

    @ManagedOperation(description = "Says Hello to me")
    public void sayHello() {
        String message = "Hello " + this.name;
        this.helloStore.add(message);

        Notification notification = buildNotification(message);
        this.notificationPublisher.sendNotification(notification);
    }

    @ManagedOperation(description = "Clears all hellos")
    public void clearHelloStore() {
        this.helloStore.clear();
    }

    @ManagedAttribute(description = "Delivers the stored hellos")
    public String[] getStoredHellos() {
        return this.helloStore.toArray(new String[this.helloStore.size()]);
    }

    @ManagedAttribute(description = "The name property.",
            defaultValue = "")
    public void setName(String name) {
        this.name = name;
    }

    @ManagedAttribute
    public String getName() {
        return this.name;
    }

    protected Notification buildNotification(String message) {
        String source = getClass().getName();
        long timestamp = System.currentTimeMillis();

        Notification notification = new Notification(JmxTestService.class.getName() + ".NOTIFICATION", source, notificationSequence++, timestamp, message);
        return notification;
    }

    private long notificationSequence = 0;

    private NotificationPublisher notificationPublisher;

    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

}
