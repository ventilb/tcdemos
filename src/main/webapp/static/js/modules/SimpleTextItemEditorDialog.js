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
 * Implementiert einen einfachen Texteditor.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.12.12 - 01:08
 */
define(['i18n!nls/messages'], function (msg) {
    isc.defineClass('SimpleTextItemEditorDialog', 'Window');

    isc.SimpleTextItemEditorDialog.registerStringMethods({
        editingDone: ''
    });

    isc.SimpleTextItemEditorDialog.addProperties({
        // Meine Eigenschaften hier

        title: msg['simple_title_editor_dialog.title'],

        width: '400',
        height: '300',
        autoCenter: true,

        // Meine Methoden hier: Wichtige zuerst

        setTitleValue: function (/* String */ titleValueToSet) {
            this.editorForm.setValue('title', titleValueToSet);
        },

        getTitleValue: function () {
            return this.editorForm.getValue('title');
        },

        initWidget: function () {
            this.Super('initWidget', arguments);

            this.editorForm = isc.DynamicForm.create({
                width: '100%',
                height: '*',
                colWidths: ['*'],
                numCols: 1,
                fields: [
                    {type: 'textArea', name: 'title', width: '*', height: '*', showTitle: false}
                ]
            });

            var scope = this;
            var submitButton = isc.Button.create({
                title: msg['buttons.submit.title'],
                click: function () {
                    scope.fireCallback(scope.editingDone, '', []);
                }
            });

            var buttonLayout = isc.HLayout.create({
                width: '100%',
                height: '40',
                align: 'center',
                defaultLayoutAlign: 'center',
                members: [
                    submitButton
                ]
            });

            var vlayout = isc.VLayout.create({
                members: [
                    this.editorForm,
                    buttonLayout
                ]
            });

            this.addItem(vlayout);
        }
    });

    isc.SimpleTextItemEditorDialog.addClassProperties({});
});
