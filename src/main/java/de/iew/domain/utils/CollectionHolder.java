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

package de.iew.domain.utils;

import java.util.Collection;

/**
 * Implements a utility class to hold a view portion of a collection
 * (e.g. a collection of domain models) and the total count available for this
 * collection.
 * <p>
 * This class can be used to implement paginating.
 * </p>
 *
 * @author Manuel Schulze <mschulze@geneon.de>
 * @since 02.02.13 - 12:06
 */
public class CollectionHolder<T> {

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
}