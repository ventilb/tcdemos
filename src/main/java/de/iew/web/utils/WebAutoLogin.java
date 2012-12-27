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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Implementiert eine kleine Hilfskomponente um Nutzer automatisch einzuloggen.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://blog.w3villa.com/java/spring-security-auto-login/">http://blog.w3villa.com/java/spring-security-auto-login/</a>
 * @since 01.12.12 - 16:19
 */
@Component(value = "webAutoLogin")
public class WebAutoLogin {

    private static final Log log = LogFactory.getLog(WebAutoLogin.class);

    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    public void autoLogin(String username, HttpServletRequest request) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        autoLogin(userDetails, request);
    }

    public void autoLogin(UserDetails userDetails, HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        try {
            // @TODO Das funktioniert so nicht direkt. Habe es ohne Passwort Angabe nicht hinbekommen.
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), "test", userDetails.getAuthorities());

            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authentication = this.authenticationManager.authenticate(token);

            securityContext.setAuthentication(authentication);
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.info("Fehler während des Einlog-Versuchs.", e);
            }
            securityContext.setAuthentication(null);
        }
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
