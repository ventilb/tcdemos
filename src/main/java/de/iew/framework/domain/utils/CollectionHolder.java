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

package de.iew.framework.domain.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Implements a utility class to hold a view portion of a collection
 * (e.g. a collection of domain models) and the total count available for this
 * collection.
 * <p>
 * This class can be used to implement paginating.
 * </p>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.02.13 - 12:06
 */
public class CollectionHolder<T> implements Collection<T> {

    private final long firstItem;

    private final Collection<T> collection;

    private final long totalCount;

    /**
     * Instantiates a new Collection holder.
     *
     * @param collection the collection
     * @param firstItem  the first item
     * @param totalCount the total count
     */
    public CollectionHolder(Collection<T> collection, long firstItem, long totalCount) {
        this.firstItem = firstItem;
        this.collection = collection;
        this.totalCount = totalCount;
    }

    /**
     * Instantiates a new Collection holder.
     *
     * @param collection the collection
     */
    public CollectionHolder(Collection<T> collection) {
        this(collection, 0, collection.size());
    }

    /**
     * Gets collection.
     *
     * @return the collection
     */
    public Collection<T> getCollection() {
        return collection;
    }

    /**
     * Gets first item.
     *
     * @return the first item
     */
    public long getFirstItem() {
        return firstItem;
    }

    /**
     * Gets total count.
     *
     * @return the total count
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * Gets collection size.
     *
     * @return the collection size
     */
    public long getCollectionSize() {
        return this.collection.size();
    }

    public int size() {
        return this.collection.size();
    }

    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    public boolean contains(Object o) {
        return this.collection.contains(o);
    }

    public Iterator<T> iterator() {
        return this.collection.iterator();
    }

    public Object[] toArray() {
        return this.collection.toArray();
    }

    public <T1 extends Object> T1[] toArray(T1[] a) {
        return this.collection.toArray(a);
    }

    public boolean add(T t) {
        return this.collection.add(t);
    }

    public boolean remove(Object o) {
        return this.collection.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return this.collection.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        return this.collection.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return this.collection.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return this.collection.retainAll(c);
    }

    public void clear() {
        this.collection.clear();
    }
}
