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
 * Beschreibt die Knotenoperationen.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 24.11.12 - 14:54
 */

isc.defineClass('IewNodeOperation', 'Class');

isc.IewNodeOperation.addClassProperties({
    Add: {
        INSERT_BEFORE: 'INSERT_BEFORE',
        INSERT_AFTER: 'INSERT_AFTER',
        APPEND_CHILD: 'APPEND_CHILD'
    }
});

/**
 * Erweitert die SmartClient RestDataSource für die Kommunikation dem dem
 * TreeController.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 23.11.12 - 00:19
 */

isc.defineClass('IewTreeGridRestDataSource', 'RestDataSource');

isc.IewTreeGridRestDataSource.registerStringMethods({});

isc.IewTreeGridRestDataSource.addProperties({
    requestProperties: {
        httpHeaders: {
            'Accept': 'application/json'
        }
    },
    /**
     * Austauschformat zwischen Browser und Server. Ohne klappt JSON nicht.
     */
    dataFormat: 'json',
    titleField: 'title',
    noNullUpdates: true,
    nullIntegerValue: '',

    /**
     * Die Id des Baums auf dem diese DataSource operiert.
     */
    treeId: null,

    fields: [
        {name: 'id', type: 'integer', primaryKey: true},
        {name: 'treeId', type: 'integer', primaryKey: true},
        {name: 'title', type: 'string'},
        {name: 'parentId', type: 'integer', foreignKey: 'id', rootValue: ''},
        {name: 'orderInLevel', type: 'integer'},
        {name: 'nestedSetLeft', type: 'integer'},
        {name: 'nestedSetRight', type: 'integer'}
    ],

    addOperation: isc.IewNodeOperation.Add.APPEND_CHILD,

//    operationBindings: [
//        {operationType: 'add', dataProtocol: 'postParams', defaultParams: {operation: 'add'}}
//    ],

    pk: function (/* Integer */ nodeId) {
        return {
            id: nodeId,
            treeId: this.treeId
        };
    },

    fetchTreeNodeByPrimaryKey: function (/* Integer */ nodeId, /* Callback|Function */ callback) {
        this.fetchRecord(this.pk(nodeId), callback);
    },

    transformRequest: function (/* DSRequest */ dsRequest) {
        switch (dsRequest.operationType) {
            case 'add':
                dsRequest.data.operation = this.addOperation;
                break;
        }

        return this.Super('transformRequest', arguments);
    },

    setAddOperation: function (/* IewNodeOperation.Add */ addOperation) {
        this.addOperation = addOperation;
    }

});

isc.IewTreeGridRestDataSource.addClassProperties({
    isATreeNode: function (/* Object */ nodeToTest) {
        return isc.isA.Object(nodeToTest)
            && isc.propertyDefined(nodeToTest, 'id')
            && isc.propertyDefined(nodeToTest, 'treeId')
            && isc.propertyDefined(nodeToTest, 'parentId');
    }
});


