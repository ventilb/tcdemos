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

package de.iew.framework.persistence.hibernate;

import de.iew.framework.domain.Node;
import de.iew.framework.domain.Tree;
import de.iew.framework.persistence.NodeDao;
import de.iew.framework.persistence.TreeDao;
import de.iew.framework.persistence.TreeOperationDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Hibernate Implementierung der {@link de.iew.framework.persistence.TreeOperationDao}-Schnittstelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 24.11.12 - 20:24
 */
@Repository(value = "treeOperationDao")
public class HbmTreeOperationDaoImpl extends AbstractHbmDao implements TreeOperationDao {

    private static final Log log = LogFactory.getLog(HbmNodeDaoImpl.class);

    private NodeDao nodeDao;

    private TreeDao treeDao;

    public Node save(Node domainModel) {
        Tree tree = domainModel.getTree();

        long nestedSetRightToShiftFrom = domainModel.getNestedSetRight() - 2;

        moveNestedSetBorder(tree.getId(), nestedSetRightToShiftFrom);
        return this.nodeDao.save(domainModel);
    }

    public void incNestedSetBorders(long treeId) {
        String hql = "UPDATE Node SET nestedSetLeft = nestedSetLeft + 1, nestedSetRight = nestedSetRight + 1 WHERE tree.id = :treeId";

        Query query = getCurrentSession().createQuery(hql);
        query.setLong("treeId", treeId);

        int updated = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurden " + updated + " Knoten aktualisiert.");
        }
    }

    public void moveNestedSetBorder(long treeId, long fromNestedSetIndex) {
        String hql = "UPDATE Node SET nestedSetRight = nestedSetRight + 2 WHERE tree.id = :treeId AND nestedSetRight > :fromNestedSetIndex";
        Query query = getCurrentSession().createQuery(hql);
        query.setLong("treeId", treeId);
        query.setLong("fromNestedSetIndex", fromNestedSetIndex);

        int update = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurde, für " + update + " Knoten, der nestedSetRight Wert aktualisiert.");
        }

        hql = "UPDATE Node SET nestedSetLeft = nestedSetLeft + 2 WHERE tree.id = :treeId AND nestedSetLeft > :fromNestedSetIndex";
        query = getCurrentSession().createQuery(hql);
        query.setLong("treeId", treeId);
        query.setLong("fromNestedSetIndex", fromNestedSetIndex);

        update = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurde, für " + update + " Knoten, der nestedSetLeft Wert aktualisiert.");
        }
    }

    public void deleteSingleNode(long treeId, long nodeId) {
        Node nodeToDelete = findNodeForTreeAndId(treeId, nodeId);
        if (nodeToDelete == null) {
            return;
        }

        long leftNestedSetIndex = nodeToDelete.getNestedSetLeft();
        long rightNestedSetIndex = nodeToDelete.getNestedSetRight();

        this.nodeDao.remove(nodeToDelete);

        Session session = getCurrentSession();

        String hql;
        Query query;
        int update;

        hql = "UPDATE Node SET nestedSetLeft = nestedSetLeft - 1, nestedSetRight = nestedSetRight - 1 WHERE tree.id = :treeId AND nestedSetLeft BETWEEN :leftNestedSetIndex AND :rightNestedSetIndex";
        query = session.createQuery(hql);
        query.setLong("treeId", treeId);
        query.setLong("leftNestedSetIndex", leftNestedSetIndex);
        query.setLong("rightNestedSetIndex", rightNestedSetIndex);
        update = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurde, für " + update + " Knoten, die nestedSetLeft und nestedSetRight Eigenschaft aktualisiert.");
        }

        long delta = 2;
        updateNestedSetIndexImpl(treeId, delta, rightNestedSetIndex);
    }

    public void deleteNodesBetween(long treeId, long leftNestedSetIndex, long rightNestedSetIndex) {
        Session session = getCurrentSession();

        String hql;
        Query query;
        int update;

        hql = "UPDATE Node SET parent = NULL WHERE tree.id = :treeId AND nestedSetLeft = :leftNestedSetIndex AND nestedSetRight = :rightNestedSetIndex";
        query = session.createQuery(hql);
        query.setLong("treeId", treeId);
        query.setLong("leftNestedSetIndex", leftNestedSetIndex);
        query.setLong("rightNestedSetIndex", rightNestedSetIndex);

        update = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurde, für " + update + " Knoten, die parentId Eigenschaft auf NULL gesetzt.");
        }

        hql = "DELETE FROM Node WHERE tree.id = :treeId AND nestedSetLeft BETWEEN :leftNestedSetIndex AND :rightNestedSetIndex";
        query = session.createQuery(hql);
        query.setLong("treeId", treeId);
        query.setLong("leftNestedSetIndex", leftNestedSetIndex);
        query.setLong("rightNestedSetIndex", rightNestedSetIndex);

        int deleted = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurde " + deleted + " Knoten zwischen " + leftNestedSetIndex + " und " + rightNestedSetIndex + " gelöscht.");
        }

        long delta = Math.round(rightNestedSetIndex - leftNestedSetIndex + 1);
        updateNestedSetIndexImpl(treeId, delta, rightNestedSetIndex);
/*
        hql = "UPDATE Node SET nestedSetLeft = nestedSetLeft - :delta WHERE tree.id = :treeId AND nestedSetLeft > :rightNestedSetIndex";
        query = session.createQuery(hql);
        query.setLong("delta", delta);
        query.setLong("treeId", treeId);
        query.setLong("rightNestedSetIndex", rightNestedSetIndex);

        update = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurde, für " + update + " Knoten, der nestedSetLeft Wert aktualisiert.");
        }


        hql = "UPDATE Node SET nestedSetRight = nestedSetRight - :delta WHERE tree.id = :treeId AND nestedSetRight > :rightNestedSetIndex";
        query = session.createQuery(hql);
        query.setLong("delta", delta);
        query.setLong("treeId", treeId);
        query.setLong("rightNestedSetIndex", rightNestedSetIndex);

        update = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurde, für " + update + " Knoten, der nestedSetRight Wert aktualisiert.");
        }
        */
    }

    protected void updateNestedSetIndexImpl(long treeId, long delta, long nestedSetRight) {
        Session session = getCurrentSession();

        String hql;
        Query query;
        int update;

        hql = "UPDATE Node SET nestedSetLeft = nestedSetLeft - :delta WHERE tree.id = :treeId AND nestedSetLeft > :rightNestedSetIndex";
        query = session.createQuery(hql);
        query.setLong("delta", delta);
        query.setLong("treeId", treeId);
        query.setLong("rightNestedSetIndex", nestedSetRight);

        update = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurde, für " + update + " Knoten, der nestedSetLeft Wert aktualisiert.");
        }


        hql = "UPDATE Node SET nestedSetRight = nestedSetRight - :delta WHERE tree.id = :treeId AND nestedSetRight > :rightNestedSetIndex";
        query = session.createQuery(hql);
        query.setLong("delta", delta);
        query.setLong("treeId", treeId);
        query.setLong("rightNestedSetIndex", nestedSetRight);

        update = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurde, für " + update + " Knoten, der nestedSetRight Wert aktualisiert.");
        }
    }

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

    @Autowired
    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }

    @Autowired
    public void setTreeDao(TreeDao treeDao) {
        this.treeDao = treeDao;
    }
}
