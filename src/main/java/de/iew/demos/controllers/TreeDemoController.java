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

import de.iew.domain.Node;
import de.iew.services.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller zum Rendern der HTML-Seite für die Tree-Demo.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 25.11.12 - 01:30
 */
@Controller
@RequestMapping(value = "/treedemo")
public class TreeDemoController {

    private TreeService treeService;

    @RequestMapping
    public ModelAndView indexAction() throws Exception {
        Node rootNode = this.treeService.getTreeRootNode(1);

        ModelAndView mav = new ModelAndView("treedemo");

        mav.addObject("rootNode", rootNode);
        mav.addObject("treeId", 1);

        return mav;
    }

    @Autowired
    public void setTreeService(TreeService treeService) {
        this.treeService = treeService;
    }
}
