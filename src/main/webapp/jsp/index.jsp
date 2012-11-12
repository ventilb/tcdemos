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
<div id="content">
    <h1>Startseite</h1>

    <script src="${pageContext.request.contextPath}/static/js/demos.js" type="text/javascript"></script>
    <ul>
        <li><a id="demo1_link" href="javascript:void(0)">Demo 1</a>
        <li><a id="demo2_link" href="javascript:void(0)">Demo 2</a>

        </li>
    </ul>

    <textarea id="demo_result" rows=20></textarea>
</div>

</body>
</html>