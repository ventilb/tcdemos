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

package de.iew.framework.domain.security;

/**
 * Aufzählung der unterstützung pattern matcher für die {@link WebResource#pattern}
 * URIs. Die pattern matcher können die URIs auswerten und gegen einen HTTP-Request
 * matchen. Mit anderen Worten entspricht diese Aufzählung Springs {@link org.springframework.security.web.util.RequestMatcher}
 * Hierarchie.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.01.13 - 20:48
 */
public enum WebResourcePatternMatcher {
    /**
     * Pattern matcher, der Ant-like URI-Ausdrücke versteht.
     */
    ANT;
}
