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

package de.iew.web.controllers;

import de.iew.web.forms.LoginForm;
import de.iew.web.utils.ValidatingFormAuthenticationFilter;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementiert einen Controller für Login- und Logout-Prozesse.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://forum.springsource.org/showthread.php?103116-Custom-Login-Form-Validation">http://forum.springsource.org/showthread.php?103116-Custom-Login-Form-Validation</a>
 * @since 03.01.13 - 13:20
 */
@Controller
@RequestMapping(value = "/identity")
public class IdentityController {

    /**
     * Der Key, unter dem das {@link LoginForm} für die View veröffentlicht
     * wird.
     */
    private String loginFormModelKey = "loginForm";

    /**
     * Name des Viewscripts zum Anzeigen des Login-Formulars.
     */
    private String viewScript = "loginform";

    @RequestMapping(value = "/login.html")
    public ModelAndView loginAction() {
        ModelAndView mav = new ModelAndView(this.viewScript);

        LoginForm loginForm = createLoginForm();
        mav.addObject(this.loginFormModelKey, loginForm);
        return mav;
    }

    @RequestMapping(value = "/loginerror.html")
    public ModelAndView loginError(
            HttpServletRequest request
    ) {
        LoginForm loginForm = createLoginForm();

        DataBinder binder = new DataBinder(loginForm);
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues(request.getParameterMap());
        binder.bind(mutablePropertyValues);

        ModelAndView mav = new ModelAndView(this.viewScript);
        mav.addObject(this.loginFormModelKey, loginForm);
        mav.addObject("error", true);
        mav.addObject("loginErrorMessage", "errors.login.denied.message");

        // Hole den Login Fehler und veröffentliche die Fehlermeldungen
        Exception e = (Exception) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (e instanceof ValidatingFormAuthenticationFilter.LoginFormValidationException) {
            ValidatingFormAuthenticationFilter.LoginFormValidationException ex = (ValidatingFormAuthenticationFilter.LoginFormValidationException) e;
            BindingResult res = ex.getErrors();
            /*
            Oh man :-D

            @see http://stackoverflow.com/questions/6704478/access-spring-mvc-bindingresult-from-within-a-view
             */
            mav.addObject(BindingResult.MODEL_KEY_PREFIX + this.loginFormModelKey, res);
        }

        return mav;
    }

    @RequestMapping(value = "/logout.html")
    public String logoutAction() {
        return null;
    }

    protected LoginForm createLoginForm() {
        return new LoginForm();
    }

    public String getLoginFormModelKey() {
        return loginFormModelKey;
    }

    public void setLoginFormModelKey(String loginFormModelKey) {
        this.loginFormModelKey = loginFormModelKey;
    }

    public String getViewScript() {
        return viewScript;
    }

    public void setViewScript(String viewScript) {
        this.viewScript = viewScript;
    }
}