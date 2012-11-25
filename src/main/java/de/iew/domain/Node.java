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
public class Node extends AbstractModel {

    private String title;

    private Node parent;

    private List<Node> children = new ArrayList<Node>();

    private Tree tree;

    /**
     * Die Reihenfolgenummer dieses Knotens innerhalb seiner Baumebene.
     */
    private int orderInLevel;

    private long nestedSetLeft;

    private long nestedSetRight;

    @Column
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderInLevel")
    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    @Column
    public int getOrderInLevel() {
        return orderInLevel;
    }

    public void setOrderInLevel(int orderInLevel) {
        this.orderInLevel = orderInLevel;
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
