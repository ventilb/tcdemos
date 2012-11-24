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

package de.iew.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;

/**
 * Stellt Hilfsmethoden für die Verwendung von JSPs und dessen Technologien
 * bereit.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.11.12 - 21:56
 */
public class JspUtils {
    private static final String URL_SEPARATOR = "/";

    public static String getContextPath(JspContext jspContext, String file) {
        PageContext pageContext = (PageContext) jspContext;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String contextPath = request.getContextPath();

        if (!empty(file)) {
            if (!file.startsWith(URL_SEPARATOR)) {
                file = URL_SEPARATOR + file;
            }
            contextPath += file;
        }

        return contextPath;
    }

    public static String getContextPath(JspContext jspContext) {
        return getContextPath(jspContext, null);
    }

    private static boolean empty(String test) {
        return test == null || "".equals(test.trim());
    }


}
