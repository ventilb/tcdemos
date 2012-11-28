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
import de.iew.web.utils.JspUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Rendert die Deklaration für ein requireJS-Paket.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://requirejs.org/docs/api.html#packages">http://requirejs.org/docs/api.html#packages</a>
 * @since 27.11.12 - 19:55
 */
public class Package extends SimpleTagSupport {

    private String name;

    private String location = "";

    private String main = "";

    @Override
    public void doTag() throws JspException, IOException {
        RequireJS requireJS = (RequireJS) getParent();

        requireJS.addRenderedPackage(doPackage());
        super.doTag();
    }

    public String doPackage() throws IOException {
        StringBuffer sb = new StringBuffer("{name:'");
        sb.append(getName())
                .append("',location:'")
                .append(JspUtils.getContextPath(getJspContext(), getLocation()))
                .append("',main:'")
                .append(getMain())
                .append("'}");
        return sb.toString();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
