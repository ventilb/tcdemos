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

package de.iew.demos.model;

import de.iew.web.dto.WebDTO;

/**
 * Ein DTO für die Übermittlung von Knoten-Daten zwischen Service- und
 * Web-Layer.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 17.11.12 - 13:05
 */
public class NodeModel extends WebDTO {

    private Long id;

    private String title = "";

    private long treeId;

    private Long parentId;

    private int ordinalNumber;

    private long nestedSetLeft;

    private long nestedSetRight;

    private String dataSourceClassname;

    public NodeModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null) {
            title = "";
        }
        this.title = title;
    }

    public long getTreeId() {
        return treeId;
    }

    public void setTreeId(long treeId) {
        this.treeId = treeId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    public long getNestedSetLeft() {
        return nestedSetLeft;
    }

    public void setNestedSetLeft(long nestedSetLeft) {
        this.nestedSetLeft = nestedSetLeft;
    }

    public long getNestedSetRight() {
        return nestedSetRight;
    }

    public void setNestedSetRight(long nestedSetRight) {
        this.nestedSetRight = nestedSetRight;
    }

    public String getDataSourceClassname() {
        return dataSourceClassname;
    }

    public void setDataSourceClassname(String dataSourceClassname) {
        this.dataSourceClassname = dataSourceClassname;
    }
}
