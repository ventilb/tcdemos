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

package de.iew.framework.mbean;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Component;

import javax.management.Notification;

/**
 * Test MBean.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 06.02.13 - 20:12
 */
@ManagedResource(objectName = "bean:name=testBean4", description = "My Managed Bean", log = true,
        logFile = "jmx.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200,
        persistLocation = "foo", persistName = "bar")
@Component
public class TestMB implements TestMBean, NotificationPublisherAware {

    private String testValue;

    private NotificationPublisher notificationPublisher;

    @ManagedAttribute(description = "The Name Attribute",
            currencyTimeLimit = 20,
            defaultValue = "bar",
            persistPolicy = "OnUpdate")
    public void setTestValue(String testValue) {
        this.testValue = testValue;
        this.notificationPublisher.sendNotification(new Notification("TestValueMessage", this, 0, "test Value changed"));
    }

    @ManagedAttribute(defaultValue = "foo", persistPeriod = 300)
    public String getTestValue() {
        return this.testValue;
    }

    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }
}
