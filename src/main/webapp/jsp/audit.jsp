<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  ~ Copyright 2012-2013 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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
<spring:message var="pageTitle" code="page.audit.title"/>
<%@ include file="meta/head.jsp" %>
<body>
<%@ include file="meta/content_top.jsp" %>
<%@ include file="meta/content_head.jsp" %>
<div id="content">
    <script type="text/javascript">
        requirejs(['i18n!nls/messages', 'core'], function (msg, core) {
            isc.RestDataSource.create({
                ID: 'AuditDataSource',
                /**
                 * Austauschformat zwischen Browser und Server. Ohne klappt JSON nicht.
                 */
                dataFormat: 'json',
                fetchDataURL: core.baseUrl('/audit/fetch.json'),
                fields: [
                    {name: 'id', type: 'integer', primaryKey: true},
                    {name: 'timestamp', type: 'datetime'},
                    {name: 'principal', type: 'string'},
                    {name: 'severity', type: 'string'},
                    {name: 'message', type: 'string'}
                ]

            });

            isc.ListGrid.create({
                ID: 'AuditListGrid',
                dataSource: 'AuditDataSource',
                autoFetchData: true,
                dataFetchMode: 'paged'
            });

            isc.HLayout.create({
                autoDraw: true,
                position: 'relative',
                htmlElement: $('#content').get(0),
                width: 1000,
                height: 400,
                members: [
                    'AuditListGrid'
                ]
            });
        });
    </script>

</div>
<%@ include file="meta/content_foot.jsp" %>
</body>
</html>