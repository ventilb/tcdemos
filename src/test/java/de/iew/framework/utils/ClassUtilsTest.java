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

package de.iew.framework.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit-Tests for the {@link ClassUtils} methods.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 06.02.13 - 22:12
 */
public class ClassUtilsTest {
    @Test
    public void testDetermineFirstGenericSuperType() throws Exception {
        Extends1 stringGeneric = new Extends1();
        Assert.assertEquals(String.class, ClassUtils.determineFirstGenericSuperType(stringGeneric));

        Extends2 stringGeneric2 = new Extends2();
        Assert.assertEquals(String.class, ClassUtils.determineFirstGenericSuperType(stringGeneric2));

        Extends3 stringGeneric3 = new Extends3();
        Assert.assertEquals(String.class, ClassUtils.determineFirstGenericSuperType(stringGeneric3));

        Hierarchie2ExtendsExtendsBase test4 = new Hierarchie2ExtendsExtendsBase();
        Assert.assertNull(ClassUtils.determineFirstGenericSuperType(test4));
    }

    // Test Hierarchie 1
    public class BaseTestObjectClass<T> {
    }

    public class Extends1 extends BaseTestObjectClass<String> {

    }

    public class Extends2 extends Extends1 {

    }

    public class Extends3 extends Extends2 {

    }

    // Test Hierarchie 2
    public class Hierarchie2Base {

    }

    public class Hierarchie2ExtendsBase extends Hierarchie2Base {

    }

    public class Hierarchie2ExtendsExtendsBase extends Hierarchie2ExtendsBase {

    }

}
