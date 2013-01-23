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

package de.iew.framework.security.access;

import org.springframework.security.access.ConfigAttribute;

/**
 * Interface to implement our own hierarchy of {@link ConfigAttribute} classes.
 * <p>
 * Our web resources are configured by domain models. This hierarchy provides
 * a simple interface to create {@link ConfigAttribute} instances from these
 * domain models.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.01.13 - 21:48
 */
public interface WebResourceAccessRuleConfigAttribute extends ConfigAttribute {
}
