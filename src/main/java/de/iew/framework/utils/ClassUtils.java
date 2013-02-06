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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Provides helper methods to deal with Java classes.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 06.02.13 - 22:07
 */
public class ClassUtils {

    /**
     * Determines the class of the first generic parameter of the given object instance's super class.
     * <p>
     * The algorithm starts with the super class of the given object and iterates up until it finds a generic super class
     * or {@link Object}. If a generic super class was found, the first generic parameter class will be returned
     * otherwise null.
     * </p>
     * <p>
     * Background: We want to automatically determine the domain model class in our hierarchy. Since we als want to play
     * with MBeans we had to switch Springs AOP proxy semantics from JDK-proxies to target proxy class. This means the
     * spring managed beans are not hidden behind a proxy anymore. They are subclassed by cglib now. But with cglib we
     * have an extra level of inheritance and the old algorithm to determine the type class stopped working. See below
     * the old code.
     * </p>
     * <code>
     * this.domainModelClass = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
     * </code>
     * <p>
     * The problem with Spring MBeans and JDK-proxies is, that spring announces the proxy bean in the MBeanServer and
     * not the real instance. The proposed solution is to change the proxy-target-class to TRUE for <code><tx:annotation-drive/></code>
     * and <code><aop:config proxy-target-class="true">...</aop:config></code>.
     * </p>
     *
     * @param instance the object instance to check
     * @return the first generic parameter class or NULL
     * @see <a href="http://blog.xebia.com/2009/02/07/acessing-generic-types-at-runtime-in-java/">http://blog.xebia.com/2009/02/07/acessing-generic-types-at-runtime-in-java/</a>
     * @see <a href="http://stackoverflow.com/questions/8474814/spring-cglib-why-cant-a-generic-class-be-proxied">http://stackoverflow.com/questions/8474814/spring-cglib-why-cant-a-generic-class-be-proxied</a>
     */
    public static Class determineFirstGenericSuperType(Object instance) {
        Type[] typeArguments;
        Class instanceClass = instance.getClass();

        Type clazz = instanceClass.getGenericSuperclass();
        while (!(clazz instanceof ParameterizedType)) {
            instanceClass = instanceClass.getSuperclass();

            // Irgendwann sind wir bei java.lang.Object. Dann gehts nicht weiter und getSuperClass() liefert NULL
            if (instanceClass != null) {
                clazz = instanceClass.getGenericSuperclass();
            } else {
                return null;
            }

        }

        typeArguments = ((ParameterizedType) clazz).getActualTypeArguments();
        return (Class) typeArguments[0];
    }
}
