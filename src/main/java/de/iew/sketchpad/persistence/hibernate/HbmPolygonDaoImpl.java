/*
 * Copyright 2012-2013 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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

package de.iew.sketchpad.persistence.hibernate;

import de.iew.framework.persistence.hibernate.AbstractHbmDomainModelDaoImpl;
import de.iew.sketchpad.domain.Polygon;
import de.iew.sketchpad.persistence.PolygonDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Hibernate Implementierung der {@link PolygonDao}-Schnittstelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.2013 - 17:46
 */
@Repository(value = "polygonDao")
public class HbmPolygonDaoImpl extends AbstractHbmDomainModelDaoImpl<Polygon> implements PolygonDao {

    public long getSegmentCount(long polygonId) {
        Criteria criteria = createCriteria();

        criteria.setProjection(Projections.count("segs.id"))
                .createAlias("segments", "segs")
                .add(Restrictions.eq("id", polygonId));

        return (Long) criteria.uniqueResult();
    }

    public List<Polygon> listPolygons(long sketchPadId) {
        Criteria criteria = createCriteria();
        criteria.createAlias("sketchPad", "sp")
                .add(Restrictions.eq("sp.id", sketchPadId))
                .createAlias("segments", "segs")
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    public List<Polygon> listPolygonsFrom(long sketchPadId, long fromPolygonId) {
        Criteria criteria = createCriteria();
        criteria.createAlias("sketchPad", "sp")
                .add(Restrictions.eq("sp.id", sketchPadId))
                .add(Restrictions.gt("id", fromPolygonId))
                .createAlias("segments", "segs")
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List list = criteria.list();

        return list;
    }
}
