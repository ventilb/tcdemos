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

/**
 * Erweitert die SmartClient TreeGrid Komponente um weitere Methoden für die
 * Kommunikation mit dem TreeController.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 22.11.12 - 22:25
 */
define(['i18n!nls/messages'], function (msg) {
    isc.defineClass('IewTreeGrid', 'TreeGrid');

    isc.IewTreeGrid.registerStringMethods({
        dataSourceRequested: 'dataSourceClassname, callback',
        /**
         * Wird gemeldet, wenn der angegebene Knoten erfolgreich hinzugefügt wurde.
         */
        addNewNodeSuccess: 'node',
        /**
         * Wird gemeldet, wenn während des Hinzufügens ein Fehler aufgetreten ist.
         */
        addNewNodeFailed: '',
        deleteNodeSuccess: 'node',
        deleteNodeFailed: ''
    });

    isc.IewTreeGrid.addProperties({
        sortField: 'ordinalNumber',

        isRootNode: function (/* TreeNode */ node) {
            return this.data.isRoot(node);
        },

        insertNewNodeBefore: function (/* TreeNode */ sibling, /* String */ dataSourceClassname) {
            this.newNodeImpl(sibling, dataSourceClassname, isc.IewNodeOperation.Add.INSERT_BEFORE);
        },

        insertNewNodeAfter: function (/* TreeNode */ sibling, /* String */ dataSourceClassname) {
            this.newNodeImpl(sibling, dataSourceClassname, isc.IewNodeOperation.Add.INSERT_AFTER);
        },

        appendNewChildNode: function (/* TreeNode */ parent, /* String */ dataSourceClassname) {
            this.newNodeImpl(parent, dataSourceClassname, isc.IewNodeOperation.Add.APPEND_CHILD);
        },

        /**
         * Implementierung zum Hinzufügen neuer Knoten mit verschiedenen
         * Strategien.
         *
         * @param relatedNode Knotenbezug für das Hinzufügen.
         * @param dataSourceClassname Klassenname der DataSource.
         * @param addOperation Add-Strategie.
         */
        newNodeImpl: function (/* TreeNode */ relatedNode, /* String */ dataSourceClassname, /* IewNodeOperation.Add */ addOperation) {
            console.log('Adding new node', relatedNode);

            this.setAddOperation(addOperation);

            var callback = function (dataSourceClassname, dataSourceData) {
                this._addDataImpl(relatedNode, dataSourceClassname, dataSourceData);
            };
            this.fireCallback(this.dataSourceRequested, 'dataSourceClassname, callback', [dataSourceClassname, callback]);
        },

        _addDataImpl: function (/* TreeNode */ relatedNode, /* String */ dataSourceClassname, /* Object */ dataSourceData) {
            var tree = this.data;

            dataSourceData.treeId = this.getTreeId();
            dataSourceData.dataSourceClassname = dataSourceClassname;

            var callback = {target: this, methodName: 'onAddNewNodeFinished'};
            this.addData(dataSourceData, callback, {
                params: {
                    relatedNodeId: relatedNode[tree.idField]
                }
            });
        },

        onAddNewNodeFinished: function (dsResponse, data, dsRequest) {
            if (dsResponse.status == isc.RPCResponse.STATUS_SUCCESS) {
                var record = null;
                if (isc.isA.Array(data) && data.length > 0) {
                    record = data[0];

                    this.reloadChildren(record[this.data.parentIdField]);
                } else {
                    if (this.logIsErrorEnabled()) {
                        this.logError('Add-Operation succeeded but server didn\'t supplied any data.');
                    }
                }
                this.fireCallback(this.addNewNodeSuccess, 'node', [record]);
            } else {
                this.fireCallback(this.addNewNodeFailed, '', []);
            }
        },

        deleteMigrate: function (/* TreeNode */ nodeToDelete) {
            this.deleteNodeImpl(nodeToDelete, isc.IewNodeOperation.Delete.DELETE_MIGRATE);
        },

        deleteSubtree: function (/* TreeNode */ nodeToDelete) {
            this.deleteNodeImpl(nodeToDelete, isc.IewNodeOperation.Delete.DELETE_SUBTREE);
        },

        /**
         * Implementierung einer Lösch-Methode für Knoten mit
         * unterschiedlichen Strategien.
         *
         * @TODO Das Löschen sollte vor der Durchführung durch ein Event angemeldet werden.
         *
         * @param nodeToDelete Der zu löschende Knoten.
         * @param deleteOperation Die Lösch-Operation.
         */
        deleteNodeImpl: function (/* TreeNode */ nodeToDelete, /* IewNodeOperation.Delete */ deleteOperation) {
            console.log('Deleting selected node');

            this.setDeleteOperation(deleteOperation);

            var callback = {target: this, methodName: 'onNodeDeleted'};
            this.removeData(nodeToDelete, callback);
        },

        onNodeDeleted: function (dsResponse, data, dsRequest) {
            if (dsResponse.status == isc.RPCResponse.STATUS_SUCCESS) {
                var record = null;
                if (isc.isA.Array(data) && data.length > 0) {
                    record = data[0];

                    this.reloadChildren(record[this.data.parentIdField]);
                } else {
                    if (this.logIsErrorEnabled()) {
                        this.logError('Delete-Operation succeeded but server didn\'t supplied any data.');
                    }
                }
                this.fireCallback(this.deleteNodeSuccess, 'node', [record]);
            } else {
                this.fireCallback(this.deleteNodeFailed, '', []);
            }
        },

        reloadChildren: function (/* TreeNode|Integer */ node) {
            try {
                node = this.resolveTreeNodeInstance(node);

                if (node) {
                    var ds = isc.DS.get(this.dataSource);
                    var iscTree = this.data;

                    var openList = [];

                    this.bfsVisit(node, function (visit) {
                        if (iscTree.isOpen(visit)) {
                            openList.add(ds.copyRecord(visit));
                        }
                    });

                    iscTree.unloadChildren(node);

                    var scope = this;
                    var callback = function () {
                        if (!openList.isEmpty()) {
                            var node = openList.removeAt(0);
                            node = scope.resolveTreeNodeInstance(node);
                            if (node) {
                                iscTree.openFolder(node, callback);
                            }
                        }
                    };

                    this.data.loadChildren(openList.removeAt(0), callback);
                }
            } catch (e) {
                if (this.logIsErrorEnabled()) {
                    this.logError('Error reloading children' + e);
                }
            }
        },

        bfsVisit: function (/* TreeNode|Integer */ node, /* Callback|Function*/ visitor) {
            if (node) {
                var queue = [node];

                while (!queue.isEmpty()) {
                    var first = queue.removeAt(0);
                    first = this.resolveTreeNodeInstance(first);
                    var children = this.data.getChildren(first);

                    if (children) {
                        queue.addList(children);
                    }

                    isc.Class.fireCallback(visitor, 'node', [first]);
                }
            }

        },

        resolveTreeNodeInstance: function (/* TreeNode|Integer */ identifier) {
            var idField = this.data.idField;

            if (isc.isA.Object(identifier) && isc.propertyDefined(identifier, idField)) {
                identifier = identifier[idField];
            }

            if (isc.isA.Number(identifier)) {
                identifier = this.data.findById(identifier);
            }

            return identifier;
        },

        /**
         * Liefert die Baum-Id zu diesem TreeGrid..
         *
         * @return {Integer}
         */
        getTreeId: function () {
            var ds = isc.DS.get(this.dataSource);
            return ds.treeId;
        },

        setAddOperation: function (/* IewNodeOperation.Add */ addOperation) {
            var ds = isc.DS.get(this.dataSource);
            if (ds) {
                ds.setAddOperation(addOperation);
            }
        },

        setDeleteOperation: function (/* IewNodeOperation.Delete */ deleteOperation) {
            var ds = isc.DS.get(this.dataSource);
            if (ds) {
                ds.setDeleteOperation(deleteOperation);
            }
        },

        initWidget: function () {
            this.Super('initWidget', arguments);
        }
    });

    isc.IewTreeGrid.addClassProperties({});
});

