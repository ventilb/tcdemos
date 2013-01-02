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
import de.iew.demos.model.NodeToNodelModelTransformer;
import de.iew.domain.DataSource;
import de.iew.domain.Node;
import de.iew.domain.principals.Authority;
import de.iew.framework.utils.LocaleStringResolver;
import de.iew.services.*;
import de.iew.services.tree.NodeVisitor;
import de.iew.domain.ModelNotFoundException;
import de.iew.web.isc.DSResponseCollection;
import de.iew.web.isc.DSResponseObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    private DataSourceServiceFactory dataSourceServiceFactory;

    private AclEditorService aclEditorService;

    private UserDetailsService userDetailsService;

    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    @ResponseBody
    public Model fetchAction(
            HttpServletRequest request,
            @RequestParam(value = "treeId", required = true) Long treeId,
            @RequestParam(value = "parentId", required = false) Long parentId
    ) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Lade Knoten mit Vater " + parentId + " aus Baum " + treeId + ".");
        }
        NodeVisitor<NodeModel> nodeVisitor = getNodeTransformer(request);
        Collection<NodeModel> nodes;

        if (parentId == null) {
            Node rootNode = this.treeService.getTreeRootNode(treeId);
            nodes = new HashSet<NodeModel>();
            nodes.add(nodeVisitor.visitNode(rootNode));
        } else {
            nodes = nodeVisitor.visitNodeCollection(this.treeService.getDirectChildNodes(treeId, parentId));
        }

        return new DSResponseCollection(nodes);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Model addAction(
            HttpServletRequest request,
            @ModelAttribute NodeModel nodeModel,
            @RequestParam(value = "relatedNodeId", required = false) Long relatedNodeId,
            @RequestParam(value = "operation", defaultValue = "APPEND_CHILD") AddNodeOperation addOperation
    ) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Füge neuen Knoten " + nodeModel + " mit Operation " + addOperation + " hinzu.");
        }

        DataSourceService dataSourceService = this.dataSourceServiceFactory.lookupDataSourceService(nodeModel.getDataSourceClassname());
        DataSource dataSource = dataSourceService.createPersistedDefaultDataSource(nodeModel);

        NodeVisitor nodeVisitor = getNodeTransformer(request);

        Node newNode;
        if (relatedNodeId == null) {
            newNode = this.treeService.migrateRootNode(nodeModel.getTreeId());
        } else {
            switch (addOperation) {
                case APPEND_CHILD:
                    newNode = this.treeService.appendNewNode(nodeModel.getTreeId(), relatedNodeId, dataSource.getId());
                    break;
                case INSERT_BEFORE:
                    newNode = this.treeService.insertNewNodeBefore(nodeModel.getTreeId(), relatedNodeId, dataSource.getId());
                    break;
                case INSERT_AFTER:
                    newNode = this.treeService.insertNewNodeAfter(nodeModel.getTreeId(), relatedNodeId, dataSource.getId());
                    break;
                default:
                    throw new UnsupportedOperationException("The requested Add-Operation " + addOperation + " is not supported.");
            }
        }

        // ACL Konfigurieren. Hier setzen wir erstmal für ROLE_ADMIN das ADMINISTRATION Privileg
        MutableAcl acl;
        Authority administration = this.userDetailsService.getAdministrativeAuthority();
        Permission admPermission = BasePermission.ADMINISTRATION;

        acl = this.aclEditorService.createAcl(dataSource.getClass(), dataSource.getId());
        this.aclEditorService.grantPermission(acl, admPermission, administration);

        acl = this.aclEditorService.createAcl(newNode.getClass(), newNode.getId());
        this.aclEditorService.grantPermission(acl, admPermission, administration);

        return new DSResponseObject(nodeVisitor.visitNode(newNode));
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public Model deleteAction(
            @ModelAttribute NodeModel nodeModel,
            @RequestParam(value = "operation", defaultValue = "DELETE_SUBTREE") DeleteNodeOperation deleteOperation
    ) throws Exception {
        switch (deleteOperation) {
            case DELETE_MIGRATE:
                this.treeService.deleteNodeAndMigrateChildren(nodeModel.getTreeId(), nodeModel.getId());
                break;
            case DELETE_SUBTREE:
                this.treeService.deleteNodeAndSubtree(nodeModel.getTreeId(), nodeModel.getId());
                break;
            default:
                throw new UnsupportedOperationException("The requested Delete-Operation " + deleteOperation + " is not supported.");

        }

        return new DSResponseObject(nodeModel);
    }

    public NodeVisitor<NodeModel> getNodeTransformer(HttpServletRequest request) {
        LocaleStringResolver localeStringResolver = new LocaleStringResolver();
        localeStringResolver.setLocale(request.getLocale());

        NodeToNodelModelTransformer nodeToNodelVisitorVisitor = new NodeToNodelModelTransformer();
        nodeToNodelVisitorVisitor.setStringResolver(localeStringResolver);
        return nodeToNodelVisitorVisitor;
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

    @Autowired
    public void setDataSourceServiceFactory(DataSourceServiceFactory dataSourceServiceFactory) {
        this.dataSourceServiceFactory = dataSourceServiceFactory;
    }

    @Autowired
    public void setAclEditorService(AclEditorService aclEditorService) {
        this.aclEditorService = aclEditorService;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public static enum AddNodeOperation {
        INSERT_BEFORE,
        INSERT_AFTER,
        APPEND_CHILD
    }

    public static enum DeleteNodeOperation {
        DELETE_MIGRATE,
        DELETE_SUBTREE
    }
}
