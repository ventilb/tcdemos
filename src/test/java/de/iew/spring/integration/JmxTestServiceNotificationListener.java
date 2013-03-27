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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import javax.management.Notification;
import javax.management.NotificationListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a simple {@link NotificationListener} to test JMX notifications with JUnit.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.03.13 - 10:32
 */
public class JmxTestServiceNotificationListener implements NotificationListener {

    private static final Log log = LogFactory.getLog(JmxTestServiceNotificationListener.class);

    private List<Notification> notifications = new ArrayList<Notification>();

    public void handleNotification(Notification notification, Object handback) {
        try {
            if (log.isDebugEnabled()) {
                log.debug(formatNotification(notification));
            }
            this.notifications.add(notification);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error handling notification", e);
            }
        }
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void clearNotifications() {
        this.notifications.clear();
    }

    private String formatNotification(Notification notification) {
        StringBuilder sb = new StringBuilder();
        sb.append("recieved notification class: ").append(notification.getType());
        sb.append(", method: ").append(notification.getSource());
        sb.append(", sequence: ").append(notification.getSequenceNumber());
        sb.append(",  timestamp: ").append(notification.getTimeStamp());
        sb.append(",  data: ");

        Object userData = notification.getUserData();
        String formattedUserData;
        if (userData == null) {
            formattedUserData = "";
        } else if (userData instanceof String) {
            formattedUserData = StringUtils.arrayToCommaDelimitedString((Object[]) notification.getUserData());
        } else {
            formattedUserData = userData.toString();
        }
        sb.append(formattedUserData);

        return sb.toString();
    }

}
