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

package de.iew.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementiert ein Domainmodell für Baumknoten auf Basis des Adjazenzlisten-
 * und NestedSet-Verfahrens.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 16.11.12 - 19:29
 */
@Entity
@Table(name = "node")
public class Node extends AbstractModel implements TreeNode, AdjacencyNode<Node>, NestedSetNode, Order {

    private Node parent;

    private List<Node> children = new ArrayList<Node>();

    private Tree tree;

    private DataSource dataSource;

    /**
     * Die Position dieses Knotens innerhalb der Geschwisterliste.
     */
    private int ordinalNumber;

    private long nestedSetLeft;

    private long nestedSetRight;

    @ManyToOne(fetch = FetchType.LAZY)
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "ordinalNumber")
    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    // CascadeType war mal auf REMOVE gestellt. Das geht aber so nicht, da
    // Hibernate immer den Tree mit löschen möchte wenn ein Knoten gelöscht
    // werden soll.
    @ManyToOne(fetch = FetchType.EAGER)
    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_source_id")
    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Liefert die Position (beginnend bei 0) dieses Knotens innerhalb der
     * Geschwisterliste dieses Knotens.
     * </p>
     */
    @Column
    public int getOrdinalNumber() {
        return ordinalNumber;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Stellt die Position dieses Knotens innerhalb der Geschwisterliste
     * dieses Knotens auf den angegebenen Wert.
     * </p>
     */
    public void setOrdinalNumber(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    @Column
    public long getNestedSetLeft() {
        return nestedSetLeft;
    }

    public void setNestedSetLeft(long nestedSetLeft) {
        this.nestedSetLeft = nestedSetLeft;
    }

    @Column
    public long getNestedSetRight() {
        return nestedSetRight;
    }

    public void setNestedSetRight(long nestedSetRight) {
        this.nestedSetRight = nestedSetRight;
    }
}
