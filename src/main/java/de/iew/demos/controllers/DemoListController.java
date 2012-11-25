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

import de.iew.demos.model.NodeModel;
import de.iew.demos.model.NodeModels;
import de.iew.domain.Node;
import de.iew.services.TreeService;
import de.iew.web.isc.DSResponseObject;
import de.iew.web.isc.DSResponseCollection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Implementiert einen einfachen Demowebservice zur Verwaltung von Knoten
 * als Liste.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 22.11.12 - 23:15
 */
@Controller
@RequestMapping(value = "/list")
public class DemoListController {

    private static final Log log = LogFactory.getLog(DemoListController.class);

    private TreeService treeService;

    @RequestMapping(value = "/fetchNodes", method = RequestMethod.GET)
    @ResponseBody
    public Model fetchAction(
            @RequestParam(value = "treeId", required = true) Long treeId,
            @RequestParam(value = "id", required = false) Long nodeId
    ) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Lade Knoten für Baum " + treeId + " und Knoten " + nodeId + ".");
        }

        if (nodeId == null) {
            return fetchNodes(treeId);
        } else {
            return fetchNode(treeId, nodeId);
        }
    }

    public Model fetchNode(long treeId, long nodeId) throws Exception {
        Node node = this.treeService.getNodeByTreeAndId(treeId, nodeId);

        return new DSResponseObject(NodeModel.fromNode(node));
    }

    public Model fetchNodes(long treeId) throws Exception {
        Collection<Node> allNodes = this.treeService.getAllNodes(treeId);

        return new DSResponseCollection(NodeModels.fromCollection(allNodes));
    }

    @ExceptionHandler(Exception.class)
    public Model onException(Exception e) {
        if (log.isErrorEnabled()) {
            log.error("Fehler während der Webservice Verarbeitung", e);
        }
        return new DSResponseObject();
    }

    @Autowired
    public void setTreeService(TreeService treeService) {
        this.treeService = treeService;
    }
}
