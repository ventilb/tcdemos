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

package de.iew.web.taglibs;

import de.iew.web.utils.JspUtils;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Implementiert ein JSP-Tag zum Einbinden von Script-Dateien in eine
 * HTML-Seite. Rendert ein <script></script> -Tag.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 28.11.12 - 00:03
 */
public class HeadScript extends SimpleTagSupport {

    private String type = "text/javascript";

    private String src = "";

    public HeadScript() {
    }

    public HeadScript(JspContext jspContext) {
        setJspContext(jspContext);
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspContext jspContext = getJspContext();
        jspContext.getOut().print(headScript());
    }

    public String headScript() throws IOException {
        StringBuffer headScript = new StringBuffer();
        if (this.src != null && !"".equals(this.src.trim())) {
            JspContext jspContext = getJspContext();

            String[] attrs = new String[2];
            attrs[0] = "type=\"" + this.type + "\"";
            attrs[1] = "src=\"" + JspUtils.getContextPath(jspContext, this.src) + "\"";

            headScript.append(JspUtils.openTag("script", attrs));
            headScript.append(JspUtils.closeTag("script"));
        }
        return headScript.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
