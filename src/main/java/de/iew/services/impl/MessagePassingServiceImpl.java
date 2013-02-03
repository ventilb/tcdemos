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

import de.iew.services.MessagePassingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.message.ErrorMessage;
import org.springframework.integration.message.GenericMessage;
import org.springframework.integration.xmpp.XmppHeaders;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of the {@link MessagePassingService}. Sends systems messages to XMPP services. Uses the spring
 * integration framework.
 *
 * @author Manuel Schulze <mschulze@geneon.de>
 * @since 03.02.13 - 09:51
 */
@Service(value = "messagePassingService")
public class MessagePassingServiceImpl implements MessagePassingService, InitializingBean {

    private List<String> xmppSystemMessageReceivers;

    public void afterPropertiesSet() throws Exception {
        if (this.xmppSystemMessageReceivers != null) {
            List<String> processed = new ArrayList<String>();
            for (String receiver : this.xmppSystemMessageReceivers) {
                if (StringUtils.isNotBlank(receiver)) {
                    // TODO Validate, Append URL
                    processed.add(receiver.trim());
                }
            }
            this.xmppSystemMessageReceivers = Collections.unmodifiableList(processed);
        }
    }

    public void passSystemMessage(String message) {
        if (isXmppSystemMessageFeatureAvailable() && StringUtils.isNotBlank(message)) {
            for (String xmppSystemMessageReceiver : this.xmppSystemMessageReceivers) {
                Map<String, Object> headers = new Hashtable<String, Object>();
                headers.put(XmppHeaders.TO, xmppSystemMessageReceiver);

                GenericMessage<String> xmppMessage = new GenericMessage<String>(message, headers);
                this.xmppOutboundEventChannel.send(xmppMessage);
            }
        }
    }

    public void passThrowableSystemMessage(Throwable throwable) {
        if (isXmppSystemMessageFeatureAvailable() && throwable != null) {
            for (String xmppSystemMessageReceiver : this.xmppSystemMessageReceivers) {
                Map<String, Object> headers = new Hashtable<String, Object>();
                headers.put(XmppHeaders.TO, xmppSystemMessageReceiver);

                ErrorMessage errorMessage = new ErrorMessage(throwable, headers);
                this.xmppOutboundEventChannel.send(errorMessage);
            }
        }
    }

    public boolean isXmppSystemMessageFeatureAvailable() {
        return (this.xmppSystemMessageReceivers != null && !this.xmppSystemMessageReceivers.isEmpty());
    }

    // Setters/Getters ////////////////////////////////////////////////////////

    public void setXmppSystemMessageReceivers(List<String> xmppSystemMessageReceivers) {
        this.xmppSystemMessageReceivers = xmppSystemMessageReceivers;
    }

    @Value(value = "#{config['mps.xmpp_system_message_receivers']}")
    public void setXmppSystemMessageReceivers(String xmppSystemMessageReceivers) {
        this.xmppSystemMessageReceivers = null;
        if (StringUtils.isNotBlank(xmppSystemMessageReceivers)) {
            String[] receivers = StringUtils.split(xmppSystemMessageReceivers, ',');
            setXmppSystemMessageReceivers(receivers);
        }
    }

    public void setXmppSystemMessageReceivers(String[] xmppSystemMessageReceivers) {
        this.xmppSystemMessageReceivers = null;
        if (ArrayUtils.isNotEmpty(xmppSystemMessageReceivers)) {
            List<String> receivers = new ArrayList<String>();
            CollectionUtils.addAll(receivers, xmppSystemMessageReceivers);
            setXmppSystemMessageReceivers(receivers);
        }
    }

    // Service und Dao Abhängigkeiten /////////////////////////////////////////

    private MessageChannel xmppOutboundEventChannel;

    @Autowired(required = false)
    public void setXmppOutboundEventChannel(MessageChannel xmppOutboundEventChannel) {
        this.xmppOutboundEventChannel = xmppOutboundEventChannel;
    }

}
