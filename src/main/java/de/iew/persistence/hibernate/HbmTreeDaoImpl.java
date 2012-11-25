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

package de.iew.persistence.hibernate;

import de.iew.domain.Node;
import de.iew.domain.Tree;
import de.iew.persistence.TreeDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

/**
 * Hibernate Implementierung der {@link TreeDao}-Schnittstelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 24.11.12 - 20:24
 */
@Repository(value = "treeDao")
public class HbmTreeDaoImpl extends AbstractHbmDomainModelDaoImpl<Tree> implements TreeDao {
    public Node findRootNodeForTree(long treeId) {
        Criteria crit = getCurrentSession().createCriteria(Node.class);
        crit.add(Restrictions.eq("tree.id", treeId))
                .add(Restrictions.isNull("parent"));

        return (Node) crit.uniqueResult();
    }

    public Node findNodeForTreeAndId(long treeId, long nodeId) {
        Criteria crit = getCurrentSession().createCriteria(Node.class);
        crit.createAlias("tree", "tree", JoinType.INNER_JOIN)
                .add(Restrictions.eq("id", nodeId))
                .add(Restrictions.eq("tree.id", treeId));
        return (Node) crit.uniqueResult();
    }
}
