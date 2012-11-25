<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright 2012 Manuel Schulze <manuel_schulze@i-entwicklung.de>
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<html>
<%@ include file="meta/head.jsp" %>
<body>
<div id="content_head">
    <div class="inner">
        <spring:message code="treedemo.page.title"/>
    </div>
</div>
<div id="content">
    <script type="text/javascript">
        requirejs(['modules/TreeDemoContextMenu', 'modules/IewTreeGrid', 'modules/IewTreeGridRestDataSource'], function () {
            isc.IewTreeGridRestDataSource.create({
                ID: 'DemoTreeGridDS',
                addDataURL: isc.Page.getAppBaseUrl('/tree/add.json'),
                fetchDataURL: isc.Page.getAppBaseUrl('/tree/fetch.json'),
                removeDataURL: isc.Page.getAppBaseUrl('/tree/delete.json'),
                treeId: ${rootNode.tree.id}
            });

            isc.IewTreeGridRestDataSource.create({
                ID: 'DemoListGridDS',
                dataURL: isc.Page.getAppBaseUrl('/list/fetchNodes.json'),
                treeId: ${rootNode.tree.id}
            });

            var initialCriteria = {
                treeId: ${treeId}
            };

            isc.IewTreeGrid.create({
                ID: 'DemoTreeGrid',
                dataSource: 'DemoTreeGridDS',
                autoFetchData: true,
                treeRootValue: ${rootNode.id},
                showRoot: true,
                initialCriteria: initialCriteria,
                contextMenu: isc.TreeDemoContextMenu.create(),

                addNewNodeSuccess: function (node) {
                    console.log('A node was added: ', this, node);
                    DemoListGrid.invalidateCache();
                },

                deleteNodeSuccess: function (node) {
                    console.log('A node was deleted: ', this, node);
                    DemoListGrid.invalidateCache();
                },

                selectionChanged: function (record, state) {
                    if (state) {
                        DemoListGrid.selectTreeNodeById(record.id);
                    }
                }
            });

            isc.ListGrid.create({
                ID: 'DemoListGrid',
                dataSource: 'DemoListGridDS',
                treeId: ${rootNode.tree.id},
                autoFetchData: true,
                initialCriteria: initialCriteria,
                selectTreeNodeById: function (/* Integer */ nodeId) {
                    var callback = {target: this, methodName: 'selectSingleRecord'};
                    this.findTreeNodeById(nodeId, callback);
                },
                findTreeNodeById: function (/* Integer */ nodeId, /* Callback|Function */ callback) {
                    console.log('Suche nach Knoten mit Id', nodeId);
                    if (isc.isA.Array(this.data)) {
                        if (this.isLogInfoEnabled()) {
                            this.logInfo('Selecting from Array model is not yet supported.');
                        }
                    } else if (isc.isA.Instance(this.data) && this.data.isA('ResultSet')) {
                        var ds = isc.DS.get(this.dataSource);

                        var record = this.data.findByKey(ds.pk(nodeId));
                        if (record) {
                            isc.Class.fireCallback(callback, 'record', [record]);
                        } else {
                            ds.fetchTreeNodeByPrimaryKey(nodeId, function (dsRequest, data, dsResponse) {
                                if (isc.isA.Array(data)) {
                                    isc.Class.fireCallback(callback, 'record', [data[0]]);
                                }
                            });
                        }
                    }
                }
            });

            isc.HLayout.create({
                autoDraw: true,
                position: 'relative',
                htmlElement: $('#content').get(0),
                width: 1000,
                height: 400,
                members: [
                    'DemoTreeGrid',
                    'DemoListGrid'
                ]
            });
        });
    </script>
</div>
<div id="content_foot">
    <div class="inner">
        &copy; 2012 by Manuel Schulze &#8210; <a href="mailto:manuel_schulze@i-entwicklung.de">manuel_schulze@i-entwicklung.de</a>
    </div>
</div>
</body>
</html>