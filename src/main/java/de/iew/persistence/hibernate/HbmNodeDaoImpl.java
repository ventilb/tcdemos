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
import de.iew.persistence.NodeDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * Hibernate Implementierung der {@link NodeDao}-Schnittstelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 24.11.12 - 20:43
 */
@Repository("nodeDao")
public class HbmNodeDaoImpl extends AbstractHbmDomainModelDaoImpl<Node> implements NodeDao {

    private static final Log log = LogFactory.getLog(HbmNodeDaoImpl.class);

    @Override
    public Node save(Node domainModel) {
        Tree tree = domainModel.getTree();

        long nestedSetRightToShiftFrom = domainModel.getNestedSetRight() - 2;

        moveNestedSetBorder(tree.getId(), nestedSetRightToShiftFrom);
        return super.save(domainModel);
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

    public void deleteNodesBetween(long treeId, long leftNestedSetIndex, long rightNestedSetIndex) {
        Session session = getCurrentSession();

        String hql = "DELETE FROM Node WHERE tree.id = :treeId AND nestedSetLeft BETWEEN :leftNestedSetIndex AND :rightNestedSetIndex";
        Query query = session.createQuery(hql);
        query.setLong("treeId", treeId);
        query.setLong("leftNestedSetIndex", leftNestedSetIndex);
        query.setLong("rightNestedSetIndex", rightNestedSetIndex);

        int deleted = query.executeUpdate();
        if (log.isDebugEnabled()) {
            log.debug("Es wurde " + deleted + " Knoten zwischen " + leftNestedSetIndex + " und " + rightNestedSetIndex + " gelöscht.");
        }

        long delta = Math.round(rightNestedSetIndex - leftNestedSetIndex + 1);

        hql = "UPDATE Node SET nestedSetLeft = nestedSetLeft - :delta WHERE tree.id = :treeId AND nestedSetLeft > :rightNestedSetIndex";
        query = session.createQuery(hql);
        query.setLong("delta", delta);
        query.setLong("treeId", treeId);
        query.setLong("rightNestedSetIndex", rightNestedSetIndex);

        int update = query.executeUpdate();
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
    }
}
