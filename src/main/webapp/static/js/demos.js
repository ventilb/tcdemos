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
 * Einige Testfunktionen für die Javascript <-> Server Kommunikation mit
 * Ajax.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 10.11.12 - 15:51
 */

define(['jquery', 'core'], function (jQuery, core) {
    return {
        demo1Action: function () {
            jQuery.ajax({
                url: core.baseUrl('/modelandview.html'),
                success: function (data) {
                    var resultContainer = $('#demo_result');

                    if (jQuery.isPlainObject(data)) {
                        resultContainer.val(JSON.stringify(data));
                    } else {
                        resultContainer.val(data);
                    }
                }
            });
        },
        demo2Action: function () {
            jQuery.ajax({
                url: core.baseUrl('/modelandview.json'),
                success: function (data) {
                    var resultContainer = $('#demo_result');

                    if (jQuery.isPlainObject(data)) {
                        resultContainer.val(JSON.stringify(data));
                    } else {
                        resultContainer.val(data);
                    }
                }
            });
        }
    };

});
