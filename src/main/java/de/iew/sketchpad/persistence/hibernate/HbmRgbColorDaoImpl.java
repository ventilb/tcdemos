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
import de.iew.sketchpad.domain.RgbColor;
import de.iew.sketchpad.persistence.RgbColorDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Hibernate Implementierung der {@link RgbColorDao}-Schnittstelle.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 16:03
 */
@Repository(value = "sketchPadColorDao")
public class HbmRgbColorDaoImpl extends AbstractHbmDomainModelDaoImpl<RgbColor> implements RgbColorDao {
    public List<RgbColor> listColorsBySketchPadId(long sketchPadId) {
        Criteria criteria = createCriteria();
        criteria.createAlias("sketchPads", "sp")
                .add(Restrictions.eq("sp.id", sketchPadId));
        return criteria.list();
    }

    public RgbColor getColorBySketchPadIdAndId(long sketchPadId, long colorId) {
        Criteria criteria = createCriteria();
        criteria.createAlias("sketchPads", "sp")
                .add(Restrictions.eq("sp.id", sketchPadId))
                .add(Restrictions.eq("id", colorId));
        return (RgbColor) criteria.uniqueResult();
    }
}
