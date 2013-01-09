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

package de.iew.persistence.hibernate;

import de.iew.domain.sketchpad.Stroke;
import de.iew.persistence.StrokeDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Hibernate Implementierung der {@link StrokeDao}-Schnittstelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 17:06
 */
@Repository(value = "sketchPadStrokeDao")
public class HbmStrokeDaoImpl extends AbstractHbmDomainModelDaoImpl<Stroke> implements StrokeDao {

    public List<Stroke> listStrokesBySketchPadId(long sketchPadId) {
        Criteria criteria = createCriteria();
        criteria.createAlias("sketchPads", "sp")
                .add(Restrictions.eq("sp.id", sketchPadId));
        return criteria.list();
    }

    public Stroke getStrokeBySketchPadIdAndId(long sketchPadId, long strokeId) {
        Criteria criteria = createCriteria();
        criteria.createAlias("sketchPads", "sp")
                .add(Restrictions.eq("sp.id", sketchPadId))
                .add(Restrictions.eq("id", strokeId));

        return (Stroke) criteria.uniqueResult();
    }
}
