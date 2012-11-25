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

import de.iew.demos.model.NodeModel;
import de.iew.demos.model.NodeModels;
import de.iew.domain.ModelNotFoundException;
import de.iew.domain.Node;
import de.iew.services.TreeService;
import de.iew.web.isc.DSResponseCollection;
import de.iew.web.isc.DSResponseObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;

/**
 * Implementiert einen Webservice für den Zugriff auf Bäume.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.11.12 - 19:27
 */
@Controller
@RequestMapping(value = "/tree")
public class TreeController {
    private static final Log log = LogFactory.getLog(TreeController.class);

    private TreeService treeService;

    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    @ResponseBody
    public Model fetchAction(
            @RequestParam(value = "treeId", required = true) Long treeId,
            @RequestParam(value = "parentId", required = false) Long parentId
    ) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("Lade Knoten mit Vater " + parentId + " aus Baum " + treeId + ".");
        }
        Collection<Node> nodes;

        if (parentId == null) {
            Node rootNode = this.treeService.getTreeRootNode(treeId);
            nodes = new HashSet<Node>();
            nodes.add(rootNode);
        } else {
            nodes = this.treeService.getDirectChildNodes(treeId, parentId);
        }

        return new DSResponseCollection(NodeModels.fromCollection(nodes), nodes.size());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Model addAction(
            @ModelAttribute NodeModel nodeModel,
            @RequestParam(value = "relatedNodeId", required = false) Long relatedNodeId,
            @RequestParam(value = "operation", defaultValue = "APPEND_CHILD") AddNodeOperation addOperation
    ) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Füge neuen Knoten " + nodeModel + " mit Operation " + addOperation + " hinzu.");
        }

        Node newNode;

        if (relatedNodeId == null) {
            newNode = this.treeService.prependNewTreeRootNode(nodeModel.getTitle(), nodeModel.getTreeId());
        } else {
            switch (addOperation) {
                case APPEND_CHILD:
                    newNode = this.treeService.appendNewNode(nodeModel.getTitle(), nodeModel.getTreeId(), relatedNodeId);
                    break;
                case INSERT_BEFORE:
                    newNode = this.treeService.insertNewNodeBefore(nodeModel.getTitle(), nodeModel.getTreeId(), relatedNodeId);
                    break;
                case INSERT_AFTER:
                    newNode = this.treeService.insertNewNodeAfter(nodeModel.getTitle(), nodeModel.getTreeId(), relatedNodeId);
                    break;
                default:
                    throw new UnsupportedOperationException("The requested Add-Operation " + addOperation + " is not supported.");
            }
        }

        return new DSResponseObject(NodeModel.fromNode(newNode));
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public Model deleteAction(
            @ModelAttribute NodeModel nodeModel
    ) throws Exception {
        this.treeService.deleteNodeAndSubtree(nodeModel.getTreeId(), nodeModel.getId());

        return new DSResponseObject(nodeModel);
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public Model onModelNotFoundException(ModelNotFoundException e) {
        return new DSResponseObject();
    }

    @ExceptionHandler(Exception.class)
    public Model onException(Exception e) {
        if (log.isErrorEnabled()) {
            log.error("Fehler während der Webservice Verarbeitung", e);
        }
        return null;
    }

    @Autowired
    public void setTreeService(TreeService treeService) {
        this.treeService = treeService;
    }

    public static enum AddNodeOperation {
        INSERT_BEFORE,
        INSERT_AFTER,
        APPEND_CHILD
    }

}
