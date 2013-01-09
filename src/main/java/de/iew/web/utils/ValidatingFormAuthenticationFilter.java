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

package de.iew.web.utils;

import de.iew.web.forms.LoginForm;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Erweitert Springs {@link UsernamePasswordAuthenticationFilter} um die
 * Möglichkeit die Logindaten zu validieren. Außerdem führt dieser Filter bei
 * einem HTTP-POST den Login Vorgang durch.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://stackoverflow.com/questions/7025614/how-to-make-extra-validation-in-spring-security-login-form">http://stackoverflow.com/questions/7025614/how-to-make-extra-validation-in-spring-security-login-form</a>
 * @see <a href="https://gist.github.com/3137040">https://gist.github.com/3137040</a>
 * @see <a href="http://mark.koli.ch/2010/07/spring-3-and-spring-security-setting-your-own-custom-j-spring-security-check-filter-processes-url.html">http://mark.koli.ch/2010/07/spring-3-and-spring-security-setting-your-own-custom-j-spring-security-check-filter-processes-url.html</a>
 * @since 03.01.13 - 15:55
 */
public class ValidatingFormAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private Validator validator;

    public ValidatingFormAuthenticationFilter() {
        setUsernameParameter("username");
        setPasswordParameter("password");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        if (request.getMethod().equals(RequestMethod.POST.name())) {
            // If the incoming request is a POST, then we send it up
            // to the AbstractAuthenticationProcessingFilter.
            super.doFilter(request, response, chain);
        } else {
            // If it's a GET, we ignore this request and send it
            // to the next filter in the chain.  In this case, that
            // pretty much means the request will hit the /login
            // controller which will process the request to show the
            // login page.
            chain.doFilter(request, response);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginForm loginForm = new LoginForm();

        /*
        Validiere zuerst die Logindaten. Da wir hier in einem Filter sind,
        müssen wir die Validierung per Hand vornehmen. Das Validierungsergebnis
        wird dann im Fehlerfall durch eine Exception mitgeteilt.

        @see <a href="http://static.springsource.org/spring/docs/3.0.x/reference/validation.html">http://static.springsource.org/spring/docs/3.0.x/reference/validation.html</a>
        */
        DataBinder binder = new DataBinder(loginForm);
        binder.setValidator(this.validator);

        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues(request.getParameterMap());
        binder.bind(mutablePropertyValues);
        binder.validate();

        BindingResult results = binder.getBindingResult();

        if (results.hasErrors()) {
            throw new LoginFormValidationException("validation failed", results);
        }

        return super.attemptAuthentication(request, response);
    }

    public static class LoginFormValidationException extends AuthenticationException {
        private BindingResult validationResult;

        public LoginFormValidationException(String msg, BindingResult validationResult) {
            super(msg);
            this.validationResult = validationResult;
        }

        public BindingResult getErrors() {
            return this.validationResult;
        }
    }
}
