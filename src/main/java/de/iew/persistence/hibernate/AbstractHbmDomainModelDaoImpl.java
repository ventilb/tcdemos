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

import de.iew.domain.AbstractModel;
import de.iew.domain.DomainModel;
import de.iew.domain.Order;
import de.iew.framework.utils.ClassUtils;
import de.iew.persistence.DomainModelDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Base class to implement concrete Hibernate {@link DomainModelDao}s.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 24.11.12 - 20:25
 */
public abstract class AbstractHbmDomainModelDaoImpl<M extends AbstractModel> extends AbstractHbmDao implements DomainModelDao<M> {

    private final Class<M> domainModelClass;

    /**
     * Default constructor to implement {@link DomainModelDao}s.
     * <p>
     * Determines the domain model class by Java reflection.
     * </p>
     *
     * @see ClassUtils#determineFirstGenericSuperType(Object) Method description for more information
     */
    protected AbstractHbmDomainModelDaoImpl() {
        this.domainModelClass = ClassUtils.determineFirstGenericSuperType(this);
        Assert.notNull(this.domainModelClass);
    }

    protected AbstractHbmDomainModelDaoImpl(Class<M> domainModelClass) {
        this.domainModelClass = domainModelClass;
        Assert.notNull(this.domainModelClass);
    }

    public M save(M domainModel) {
        return (M) getCurrentSession().merge(domainModel);
    }

    public void remove(M domainModel) {
        getCurrentSession().delete(domainModel);
    }

    public M findById(long id) {
        return (M) getCurrentSession().get(this.domainModelClass, id);
    }

    public Collection<M> findAll() {
        Criteria crit = createCriteria();
        return (Collection<M>) crit.list();
    }

    public Collection<M> findAll(long firstResult, long maxResults) {
        Assert.isTrue(firstResult >= 0);
        Assert.isTrue(maxResults >= 0);

        /*
        Komisch wir können zwar long-Zeilen zählen aber dürfen nur int-Zeilen
        abfragen.
         */
        Criteria crit = createCriteria();
        crit.setFirstResult((int) firstResult);
        crit.setMaxResults((int) maxResults);

        return (Collection<M>) crit.list();
    }

    public Collection<M> findAllOrderedAscending() {
        Criteria crit = createCriteria();
        if (this.domainModelClass.isAssignableFrom(Order.class)) {
            crit.addOrder(org.hibernate.criterion.Order.asc(Order.ORDER_PROPERTY_NAME));
        } else {
            crit.addOrder(org.hibernate.criterion.Order.asc(DomainModel.ID_PROPERTY_NAME));
        }
        return (Collection<M>) crit.list();
    }

    public long count() {
        Projection rowCountProjection = Projections.rowCount();

        Criteria crit = createCriteria();
        crit.setProjection(rowCountProjection);

        return (Long) crit.uniqueResult();
    }

    public void refresh(M domainModel) {
        getCurrentSession().refresh(domainModel);
    }

    public Criteria createCriteria() {
        return getCurrentSession().createCriteria(this.domainModelClass);
    }

}
