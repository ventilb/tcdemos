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

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Implementiert eine View um dynamische Einstellungen für die Javascript
 * Umgebung bereitstellen zu können. Stellt Hilfsfunktionen für die
 * Javascript Klassen bereit. Definiert:
 * <ul>
 * <li>function baseUrl(\/* String|NULL *\/ url)</li>
 * </ul>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 28.11.12 - 21:31
 */
@Component(value = "javascriptConfigView")
public class JavascriptConfigView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> stringObjectMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/javascript; charset=utf-8");

        PrintWriter writer = response.getWriter();
        writer.write("define(function(){return{");

        writer.write(renderBaseUrlHelper(request));

        writer.write("}});");
    }

    protected String renderBaseUrlHelper(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer("baseUrl: function(/* String|NULL */ url) {");
        sb.append("var baseUrl = '").append(request.getContextPath()).append("';")
                .append("return baseUrl + (url ? url : '');}");
        return sb.toString();
    }
}
