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
import de.iew.persistence.DomainModelDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

/**
 * Basisklasse für Hibernate Implentierungen der
 * {@link DomainModelDao}-Schnittstellen.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://blog.xebia.com/2009/02/07/acessing-generic-types-at-runtime-in-java/">http://blog.xebia.com/2009/02/07/acessing-generic-types-at-runtime-in-java/</a>
 * @since 24.11.12 - 20:25
 */
public abstract class AbstractHbmDomainModelDaoImpl<M extends AbstractModel> implements DomainModelDao<M> {

    private final Class<M> domainModelClass;

    protected AbstractHbmDomainModelDaoImpl() {
        this.domainModelClass = ((Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    protected AbstractHbmDomainModelDaoImpl(Class<M> domainModelClass) {
        this.domainModelClass = domainModelClass;
    }

    private SessionFactory sessionFactory;

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

    public Criteria createCriteria() {
        return getCurrentSession().createCriteria(this.domainModelClass);
    }

    public Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
