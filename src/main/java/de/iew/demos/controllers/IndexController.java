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

package de.iew.demos.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Der Controller zur Darstellung der Startseite.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 08.11.12 - 19:37
 */
@Controller
public class IndexController {
    private static final Log log = LogFactory.getLog(IndexController.class);

    @RequestMapping(value = "/")
    public String indexAction() {
        if (log.isDebugEnabled()) {
            log.debug("indexAction() wurde aufgerufen.");
        }
        return "index";
    }
}
