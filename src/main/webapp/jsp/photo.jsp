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
    <script src="${pageContext.request.contextPath}/static/js/paint.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/static/js/photo.js" type="text/javascript"></script>
    <div id="photo_editor" class="photo_editor">
        <img src="${pageContext.request.contextPath}/static/img/sonnenuntergang.jpg"/>
        <canvas width="480" height="270">

        </canvas>
    </div>
</div>

</body>
</html>