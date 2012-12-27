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
 * Kontextmenü zur Demonstration der Baumfunktionen.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 20.11.12 - 22:41
 */

define(['i18n!nls/messages'], function (msg) {
    isc.defineClass('TreeDemoContextMenu', 'Menu');

    isc.TreeDemoContextMenu.registerStringMethods({});

    isc.TreeDemoContextMenu.addProperties({
        data: [
            {title: msg['tree.context_menu.item.title.new_child'], click: 'target.appendNewChildNode(target.getSelectedRecord(), "de.iew.domain.SimpleTextData")'},
            {title: msg['tree.context_menu.item.title.sibling_before'], click: 'target.insertNewNodeBefore(target.getSelectedRecord(), "de.iew.domain.SimpleTextData")', enableIf: '!target.isRootNode(target.getSelectedRecord())'},
            {title: msg['tree.context_menu.item.title.sibling_after'], click: 'target.insertNewNodeAfter(target.getSelectedRecord(), "de.iew.domain.SimpleTextData")', enableIf: '!target.isRootNode(target.getSelectedRecord())'},
            {title: msg['tree.context_menu.submenu.title.delete'], submenu: [
                {title: msg['tree.context_menu.item.title.delete_node'], click: 'target.deleteMigrate(target.getSelectedRecord())', enableIf: '!target.isRootNode(target.getSelectedRecord())'},
                {title: msg['tree.context_menu.item.title.delete_subtree'], click: 'target.deleteSubtree(target.getSelectedRecord())', enableIf: '!target.isRootNode(target.getSelectedRecord())'}
            ]}
        ],

        initWidget: function () {
            this.Super('initWidget', arguments);
        }
    });

    isc.TreeDemoContextMenu.addClassProperties({});
});