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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementiert ein JSP-Tag für die Konfiguration der requireJS Umgebung.
 * Zusätzliche Konfigurations-Einstellungen können als Body angegeben werden.
 * <pre>
 * <iew:requireJS baseUrl="static/js" src="static/js/require.js">
 *    <iew:requireJS_Package name="nls" location="nls" main="nls"/>
 *    <iew:requireJS_I18nConfig autodetect="true"/>
 * </iew:requireJS>
 * </pre>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://requirejs.org/docs/api.html#config">http://requirejs.org/docs/api.html#config</a>
 * @since 27.11.12 - 19:31
 */
public class RequireJS extends BodyTagSupport {

    private String baseUrl = "";

    private String src = "";

    private final List<String> renderedPackages = new ArrayList<String>();

    private final List<String> renderedConfigs = new ArrayList<String>();

    @Override
    public int doStartTag() throws JspException {
        this.renderedPackages.clear();
        this.renderedConfigs.clear();
        return super.doStartTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        if (this.src == null || "".equals(this.src.trim())) {
            return EVAL_PAGE;
        }

        try {
            JspWriter out = getPreviousOut();

            HeadScript headScript = new HeadScript(pageContext);
            headScript.setSrc(this.src);
            out.println(headScript.headScript());

            out.println("<script type=\"text/javascript\">");
            out.println("requirejs.config({");
            out.print("baseUrl: '");
            out.print(JspUtils.getContextPath(pageContext, this.baseUrl));
            out.println("',packages: [");

            doPackages();

            out.println("],config: {");

            doConfigs();

            out.println("}});");
            out.println("</script>");
            return EVAL_PAGE;
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    protected void doPackages() throws IOException {
        JspWriter out = getPreviousOut();

        int packagesLeft = this.renderedPackages.size();
        for (String aPackage : this.renderedPackages) {
            out.print(aPackage);
            packagesLeft--;
            if (packagesLeft > 0) {
                out.println(",");
            }
        }
    }

    protected void doConfigs() throws IOException {
        JspWriter out = getPreviousOut();

        int configsLeft = this.renderedConfigs.size();
        for (String config : this.renderedConfigs) {
            out.print(config);
            configsLeft--;
            if (configsLeft > 0) {
                out.print(",");
            }
        }
    }

    public void addRenderedConfig(String config) {
        this.renderedConfigs.add(config);
    }

    public void addRenderedPackage(String aPackage) {
        this.renderedPackages.add(aPackage);
    }

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
