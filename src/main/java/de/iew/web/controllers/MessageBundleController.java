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

import de.iew.domain.MessageBundleStore;
import de.iew.services.MessageBundleService;
import de.iew.web.view.js.RequireJSMessageBundleView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

/**
 * Implementiert einen Controller zum Rendern eines {@link MessageBundleStore}
 * in Javascript.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://requirejs.org/docs/api.html#i18n">http://requirejs.org/docs/api.html#i18n</a>
 * @since 25.11.12 - 18:50
 */
@Controller
@RequestMapping(value = "/nls")
public class MessageBundleController {

    private static final Log log = LogFactory.getLog(MessageBundleController.class);

    private MessageBundleService messageBundleService;

    private RequireJSMessageBundleView requireJSMessageBundleView;

    @RequestMapping(value = "/{basename}")
    public ModelAndView fetchDefaultBundleAction(
            @PathVariable(value = "basename") String basename
    ) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Lade Standard MessageBundle für basename " + basename + ".");
        }

        MessageBundleStore bundle = this.messageBundleService.getDefaultMessageBundle(basename);

        List<Locale> locales = this.messageBundleService.getSupportedLocales();

        ModelAndView mav = new ModelAndView(this.requireJSMessageBundleView);
        mav.addObject(RequireJSMessageBundleView.MB_ROOT_BUNDLE, true);
        mav.addObject(RequireJSMessageBundleView.MB_BUNDLE, bundle);
        mav.addObject(RequireJSMessageBundleView.MB_AVAILABLE_LOCALES, locales);
        return mav;
    }

    @RequestMapping(value = "/{languageCode}-{countryCode}/{basename}")
    public ModelAndView fetchBundleAction(
            @PathVariable(value = "languageCode") String languageCode,
            @PathVariable(value = "countryCode") String countryCode,
            @PathVariable(value = "basename") String basename
    ) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Lade Sprachpaket für " + languageCode + " und " + countryCode + " für " + basename + ".");
        }

        MessageBundleStore bundle = this.messageBundleService.getMessageBundle(new Locale(languageCode, countryCode), basename);

        ModelAndView mav = new ModelAndView(this.requireJSMessageBundleView);
        mav.addObject(RequireJSMessageBundleView.MB_BUNDLE, bundle);
        return mav;
    }

    @Autowired
    public void setMessageBundleService(MessageBundleService messageBundleService) {
        this.messageBundleService = messageBundleService;
    }

    @Autowired
    public void setRequireJSMessageBundleView(RequireJSMessageBundleView requireJSMessageBundleView) {
        this.requireJSMessageBundleView = requireJSMessageBundleView;
    }

    @ExceptionHandler(value = Exception.class)
    public void onException(Exception e, HttpServletResponse response) {
        response.setStatus(404);
        if (log.isErrorEnabled()) {
            log.error("Fehler beim Rendern eines MessageBundle.", e);
        }
    }
}
