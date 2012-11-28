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

package de.iew.web.taglibs.requirejs;

import de.iew.web.taglibs.RequireJS;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import java.util.Locale;

/**
 * Rendert die Konfiguration für das I18N-Paket von requireJS.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://requirejs.org/docs/api.html#config-moduleconfig">http://requirejs.org/docs/api.html#config-moduleconfig</a>
 * @since 27.11.12 - 22:46
 */
public class I18n extends RequestContextAwareTag {

    private Locale locale;

    private boolean autodetect = false;

    protected Locale getRenderLocale() {
        Locale locale = getLocale();
        if (locale == null && isAutodetect()) {
            locale = autodetect();
        }
        return locale;
    }

    protected Locale autodetect() {
        return getRequestContext().getLocale();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean isAutodetect() {
        return autodetect;
    }

    public void setAutodetect(boolean autodetect) {
        this.autodetect = autodetect;
    }

    @Override
    protected int doStartTagInternal() throws Exception {
        Locale renderLocale = getRenderLocale();
        if (renderLocale != null) {
            StringBuffer sb = new StringBuffer("i18n:{");
            sb.append("locale:'")
                    .append(renderLocale.getLanguage().toLowerCase())
                    .append("-")
                    .append(renderLocale.getCountry().toLowerCase())
                    .append("'")
                    .append("}");
            RequireJS requireJS = (RequireJS) getParent();
            requireJS.addRenderedConfig(sb.toString());
        }
        return SKIP_BODY;
    }
}
