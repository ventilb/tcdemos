package de.iew.web.taglibs;

import de.iew.web.IscConfigurationServletContextInjector;
import de.iew.web.utils.JspUtils;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Properties;

/**
 * Implementiert das <code>loadIsc</code>-Tag für JSPs um die Smartclient
 * Umgebung zu konfigurieren und zu laden.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.11.12 - 19:54
 */
public class LoadIsc extends SimpleTagSupport {

    public static final String ISC_ISOMORPHIC_DIR_CONFIG = "de.iew.web.taglibs.LoadIsc.ISC_ISOMORPHIC_DIR_CONFIG";

    public static final String ISC_MODULES_BASE_CONFIG = "de.iew.web.taglibs.LoadIsc.ISC_MODULES_BASE_CONFIG";

    public static final String ISC_SKINS_BASE_CONFIG = "de.iew.web.taglibs.LoadIsc.ISC_SKINS_BASE_CONFIG";

    public static final String ISC_SKIN_CONFIG = "de.iew.web.taglibs.LoadIsc.ISC_SKIN_CONFIG";

    public static final String ISC_APP_IMG_DIR = "de.iew.web.taglibs.LoadIsc.ISC_APP_IMG_DIR";

    public static final String ISC_AUTO_DRAW_CONFIG = "de.iew.web.taglibs.LoadIsc.ISC_AUTO_DRAW_CONFIG";

    private static final String[] ISC_MODULES = {
            "ISC_Core.js",
            "ISC_Foundation.js",
            "ISC_Containers.js",
            "ISC_Grids.js",
            "ISC_Forms.js",
            "ISC_DataBinding.js"
    };

    private String isomorphicDir = "";

    private String modulesBase = "";

    private String skinsBase = "";

    private String skin = "standard";

    private String appImgDir = "";

    private boolean autoDraw = true;

    @Override
    public void doTag() throws JspException, IOException {
        JspContext jspContext = getJspContext();

        String isomorphicDir = this.isomorphicDir;
        String appImgDir = this.appImgDir;
        String autoDraw = String.valueOf(this.autoDraw);

        Properties iscConfiguration = (Properties) getJspContext().findAttribute(IscConfigurationServletContextInjector.CONTEXT_KEY);
        if (iscConfiguration != null) {
            isomorphicDir = iscConfiguration.getProperty(ISC_ISOMORPHIC_DIR_CONFIG, isomorphicDir);
            appImgDir = iscConfiguration.getProperty(ISC_APP_IMG_DIR, appImgDir);
            autoDraw = iscConfiguration.getProperty(ISC_AUTO_DRAW_CONFIG, autoDraw);
        }

        loadIsc();

        JspWriter out = jspContext.getOut();
        out.println("<script type=\"text/javascript\">");
        includeBaseUrlHook();

        if (!empty(appImgDir)) {
            out.print("isc.Page.setAppImgDir('");
            out.print(JspUtils.getContextPath(jspContext, appImgDir));
            out.println("');");
        }
        if (!empty(isomorphicDir)) {
            out.print("isc.Page.setIsomorphicDir('");
            out.print(JspUtils.getContextPath(jspContext, isomorphicDir));
            out.println("');");
        }
        out.println("isc.setAutoDraw(" + autoDraw + ");");
        out.println("</script>");

        loadSkin();
    }

    public void headScript(String javascript) throws IOException {
        JspContext jspContext = getJspContext();

        JspWriter out = jspContext.getOut();
        out.print("<script type=\"text/javascript\"");

        if (!empty(javascript)) {
            out.print(" src=\"");
            out.print(JspUtils.getContextPath(jspContext, javascript));
        }

        out.println("\"></script>");
    }

    public void loadIsc() throws IOException {
        String modulesBase = this.modulesBase;

        Properties iscConfiguration = (Properties) getJspContext().findAttribute(IscConfigurationServletContextInjector.CONTEXT_KEY);
        if (iscConfiguration != null) {
            modulesBase = iscConfiguration.getProperty(ISC_MODULES_BASE_CONFIG, modulesBase);
        }

        for (String iscModule : ISC_MODULES) {
            headScript(modulesBase + "/" + iscModule);
        }
    }

    public void loadSkin() throws IOException {
        JspContext jspContext = getJspContext();

        String skinsBase = this.skinsBase;
        String skin = this.skin;

        Properties iscConfiguration = (Properties) jspContext.findAttribute(IscConfigurationServletContextInjector.CONTEXT_KEY);
        if (iscConfiguration != null) {
            skinsBase = iscConfiguration.getProperty(ISC_SKINS_BASE_CONFIG, skinsBase);
            skin = iscConfiguration.getProperty(ISC_SKIN_CONFIG, skin);
        }

        headScript(skinsBase + "/" + skin + "/load_skin.js");
    }

    public void includeBaseUrlHook() throws IOException {
        JspWriter out = getJspContext().getOut();

        out.println("isc.Page.addClassProperties({");
        out.println("getAppBaseUrl: function(/* String? */ file){");
        out.print("return '");
        out.print(JspUtils.getContextPath(getJspContext()));
        out.println("' + file;");
        out.println("}");
        out.println("});");
    }

    public boolean empty(String test) {
        return test == null || "".equals(test.trim());
    }

    // Tag Attributes /////////////////////////////////////////////////////////

    public String getIsomorphicDir() {
        return isomorphicDir;
    }

    public void setIsomorphicDir(String isomorphicDir) {
        this.isomorphicDir = isomorphicDir;
    }

    public String getSkinsBase() {
        return skinsBase;
    }

    public void setSkinsBase(String skinsBase) {
        this.skinsBase = skinsBase;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getModulesBase() {
        return modulesBase;
    }

    public void setModulesBase(String modulesBase) {
        this.modulesBase = modulesBase;
    }

    public String getAppImgDir() {
        return appImgDir;
    }

    public void setAppImgDir(String appImgDir) {
        this.appImgDir = appImgDir;
    }

    public boolean isAutoDraw() {
        return autoDraw;
    }

    public void setAutoDraw(boolean autoDraw) {
        this.autoDraw = autoDraw;
    }
}
