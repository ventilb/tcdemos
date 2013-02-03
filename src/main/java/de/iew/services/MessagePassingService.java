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

package de.iew.services;

/**
 * Specifies an interface to implement message passing services (short: mps).
 * <p>
 * The intention of these kind of services is to send messages, data or pieces of information out of the system to
 * third party systems such as e-mail or jabber servers.
 * </p>
 * <ul>
 * <li>system messages are meant to inform about system events such as exceptions or maloperation</li>
 * </ul>
 *
 * @author Manuel Schulze <mschulze@geneon.de>
 * @since 03.02.13 - 09:50
 */
public interface MessagePassingService {

    /**
     * Pass a string system message.
     *
     * @param message the message
     */
    public void passSystemMessage(String message);

    /**
     * Pass throwable system message.
     *
     * @param throwable the throwable
     */
    public void passThrowableSystemMessage(Throwable throwable);
}
