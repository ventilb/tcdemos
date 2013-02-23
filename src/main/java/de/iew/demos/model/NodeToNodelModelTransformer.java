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

package de.iew.demos.model;

import de.iew.framework.domain.DataSource;
import de.iew.framework.domain.Node;
import de.iew.framework.domain.utils.AbstractDomainModelVisitor;
import de.iew.framework.utils.DSAnnotationsIntrospector;
import de.iew.framework.utils.StringResolver;
import de.iew.framework.persistence.hibernate.HbmUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * Implementiert die {@link de.iew.framework.domain.utils.DomainModelVisitor}-Schnittstelle zum Umwandeln der
 * {@link Node}-Domainmodelle in {@link NodeModel}-DTOs.
 * <p>
 * Ziel dieses Umwandlers ist, bereits während des Ladens von Knoten, die
 * Knoten in die Zieldarstellung umzuwandeln um unnötige Schleifeniterationen
 * zu vermeiden.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 30.11.12 - 15:04
 */
public class NodeToNodelModelTransformer extends AbstractDomainModelVisitor<Node, NodeModel> {

    private static final Log log = LogFactory.getLog(NodeToNodelModelTransformer.class);

    private StringResolver stringResolver;

    /**
     * {@inheritDoc}
     * <p>
     * Wandelt den angegebenen Knoten in ein {@link NodeModel}-DTO um.
     * </p>
     *
     * @param domainModel Der besuchte Knoten.
     * @return Das {@link NodeModel}-DTO.
     */
    public NodeModel visit(Node domainModel) {
        DataSource dataSource = domainModel.getDataSource();

        NodeModel nodeModel = new NodeModel();
        nodeModel.setId(domainModel.getId());
        nodeModel.setTitle(determineTitle(dataSource));
        nodeModel.setTreeId(domainModel.getTree().getId());

        nodeModel.setOrdinalNumber(domainModel.getOrdinalNumber());
        nodeModel.setNestedSetLeft(domainModel.getNestedSetLeft());
        nodeModel.setNestedSetRight(domainModel.getNestedSetRight());

        if (dataSource != null) {
            nodeModel.setDataSourceClassname(HbmUtils.determineRealClassname(dataSource));
        }

        Node parent = domainModel.getParent();
        if (parent != null) {
            nodeModel.setParentId(parent.getId());
        }

        return nodeModel;
    }

    public String determineTitle(DataSource dataSource) {
        String title = "-No data source-";
        try {
            dataSource = (DataSource) HbmUtils.fromProxy(dataSource);

            Method m = DSAnnotationsIntrospector.determineTitlePropertyMethod(dataSource);
            if (m != null) {
                Object titlePropertyValue = DSAnnotationsIntrospector.getTitlePropertyValue(m, dataSource);
                title = this.stringResolver.resolveString(titlePropertyValue);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Fehler beim Abfragen des Knotentitels", e);
            }
        }
        return title;
    }

    public void setStringResolver(StringResolver stringResolver) {
        this.stringResolver = stringResolver;
    }
}
