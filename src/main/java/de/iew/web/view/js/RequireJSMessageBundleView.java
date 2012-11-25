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

package de.iew.web.view.js;

import de.iew.domain.MessageBundleStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Implementiert ein View zum Rendern eines {@link MessageBundleStore} in
 * Javascript.
 * <p>
 * Wir verwenden diese View um requireJS kompatible Sprachpakete zu rendern.
 * Damit können wir den i18n-Mechanismus von requireJS verwenden.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://requirejs.org/docs/api.html#i18n">http://requirejs.org/docs/api.html#i18n</a>
 * @since 25.11.12 - 20:58
 */
@Component(value = "requireJSMessageBundleView")
public class RequireJSMessageBundleView extends AbstractView {

    public static final String MB_ROOT_BUNDLE = "MB_ROOT_BUNDLE";

    public static final String MB_BUNDLE = "MB_BUNDLE";

    public static final String MB_AVAILABLE_LOCALES = "MB_AVAILABLE_BUNDLES";

    private Locale defaultLocale;

    @Override
    protected void renderMergedOutputModel(Map<String, Object> stringObjectMap, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setContentType("text/javascript");

        PrintWriter writer = httpServletResponse.getWriter();

        // Ja, Javascript Hölle :-)
        writer.write("define({");
        if (isRootBundle(stringObjectMap)) {
            writer.write("'root':{");
        }

        Map<String, String> bundle = getBundle(stringObjectMap);
        int itemsLeft = bundle.size();
        for (Map.Entry<String, String> item : bundle.entrySet()) {
            writer.write("'");
            writer.write(item.getKey());
            writer.write("':'");
            writer.write(item.getValue());
            writer.write("'");
            itemsLeft--;

            if (itemsLeft > 0) {
                writer.write(",");
            }
        }

        if (isRootBundle(stringObjectMap)) {
            writer.write("}");

            List<Locale> availableLocales = getAvailableLocales(stringObjectMap);
            for (Locale locale : availableLocales) {
                if (!locale.equals(this.defaultLocale)) {
                    writer.write(",");
                    writer.write("'");
                    writer.write(locale.getLanguage().toLowerCase() + "-" + locale.getCountry().toLowerCase());
                    writer.write("':");
                    writer.write("true");
                }
            }
        }
        writer.write("});");
    }

    @SuppressWarnings(value = "unchecked")
    public List<Locale> getAvailableLocales(Map<String, Object> stringObjectMap) {
        if (stringObjectMap.containsKey(MB_AVAILABLE_LOCALES)) {
            return (List<Locale>) stringObjectMap.get(MB_AVAILABLE_LOCALES);
        } else {
            return Collections.emptyList();
        }
    }

    public Map<String, String> getBundle(Map<String, Object> stringObjectMap) {
        if (stringObjectMap.containsKey(MB_BUNDLE)) {
            MessageBundleStore store = (MessageBundleStore) stringObjectMap.get(MB_BUNDLE);

            return store.getMessages();
        } else {
            return Collections.emptyMap();
        }
    }

    public boolean isRootBundle(Map<String, Object> stringObjectMap) {
        return stringObjectMap.containsKey(MB_ROOT_BUNDLE) && (Boolean) stringObjectMap.get(MB_ROOT_BUNDLE);
    }


    @Value(value = "#{config['locale.default']}")
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

}
