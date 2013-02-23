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

package de.iew.framework.utils;

import de.iew.framework.domain.SimpleTextData;
import de.iew.framework.domain.TextItem;
import de.iew.framework.domain.TextItemCollection;
import org.junit.Test;

import java.lang.reflect.Method;

import static junit.framework.Assert.*;

/**
 * Testfälle für die {@link DSAnnotationsIntrospector} Hilfsklasse.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 12:36
 */
public class DSAnnotationsIntrospectorTest {
    @Test
    public void testDetermineTitlePropertyMethod() throws Exception {
        SimpleTextData model = new SimpleTextData();

        Method m = DSAnnotationsIntrospector.determineTitlePropertyMethod(model);

        assertNotNull(m);
        assertEquals("getTextItemCollection", m.getName());
    }

    @Test
    public void testGetTitlePropertyValue() throws Exception {
        TextItemCollection textItemCollection = new TextItemCollection();
        textItemCollection.setId(1l);

        TextItem textItem = new TextItem();
        textItem.setContent("Hallo Welt");
        textItem.setLanguageCode("de");
        textItem.setCountryCode("DE");

        SimpleTextData simpleTextData = new SimpleTextData();

        textItemCollection.getTextItems().add(textItem);

        simpleTextData.setTextItemCollection(textItemCollection);

        Method m = DSAnnotationsIntrospector.determineTitlePropertyMethod(simpleTextData);
        Object titleObject = DSAnnotationsIntrospector.getTitlePropertyValue(m, simpleTextData);

        assertNotNull(titleObject);
        assertEquals(textItemCollection, titleObject);
    }
}
