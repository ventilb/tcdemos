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

package de.iew.framework.spring.integration.transformer;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;
import org.springframework.integration.transformer.Transformer;

/**
 * Implements a spring integration {@link Transformer} to transform Throwable
 * payload into string stack trace.
 * <p>
 * The underlying XMPP-framework can not handle {@link org.springframework.integration.message.ErrorMessage}
 * and {@link Throwable} payload directly. Spring integration provides the
 * transformer pattern to deal with these situations.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 03.02.13 - 00:38
 */
public class SimpleThrowableTransformer implements Transformer {

    /**
     * If <code>message</code> contains a {@link Throwable} as payload this
     * method simply transform the stack trace into a string using {@link ExceptionUtils#getFullStackTrace(Throwable)}.
     * Otherwise this method does not have any effect.
     *
     * @param message the message to transform
     * @return the transformed message
     */
    public Message<?> transform(Message<?> message) {
        Object payload = message.getPayload();
        if (payload instanceof Throwable) {
            payload = ExceptionUtils.getFullStackTrace((Throwable) payload);
            message = new GenericMessage<Object>(payload, message.getHeaders());
        }

        return message;
    }
}
