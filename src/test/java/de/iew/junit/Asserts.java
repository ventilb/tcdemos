/*
 * Copyright 2012 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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

package de.iew.junit;

import de.iew.web.isc.DSResponseObject;
import org.junit.Assert;
import org.springframework.ui.Model;

/**
 * Stellt zusätzliche Assertions bereit um zusammenhängende Objekt einfacher
 * Testen zu können.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 11:27
 */
public class Asserts {
    public static void assertDSResponse(int expectedStatus, Model dsResponse) {
        junit.framework.Assert.assertTrue(dsResponse instanceof DSResponseObject);

        DSResponseObject responseObject = (DSResponseObject) dsResponse;
        Assert.assertEquals(expectedStatus, responseObject.getStatus());
    }
}
