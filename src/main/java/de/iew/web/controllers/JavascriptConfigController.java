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

package de.iew.web.controllers;

import de.iew.web.view.js.JavascriptConfigView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * Implementiert einen Controller zum Ausliefern dynamischer Konfigurationen
 * und Funktionen für die Javascript Umgebung.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 28.11.12 - 21:25
 */
@Controller
@RequestMapping(value = "/jsconfig")
public class JavascriptConfigController {

    private static final Log log = LogFactory.getLog(JavascriptConfigController.class);

    private JavascriptConfigView javascriptConfigView;

    @RequestMapping
    public ModelAndView indexAction() throws Exception {
        ModelAndView mav = new ModelAndView(this.javascriptConfigView);
        // Dynamische Konfiguration hier in das Modell packen.
        return mav;
    }

    @ExceptionHandler(value = Exception.class)
    public void onException(Exception e, HttpServletResponse response) {
        response.setStatus(404);
        if (log.isErrorEnabled()) {
            log.error("Fehler beim Rendern eines der Javascript Konfiguration.", e);
        }
    }

    @Autowired
    public void setJavascriptConfigView(JavascriptConfigView javascriptConfigView) {
        this.javascriptConfigView = javascriptConfigView;
    }
}
