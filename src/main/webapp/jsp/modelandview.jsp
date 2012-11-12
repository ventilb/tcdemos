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
<head>
    <title>i-entwicklung Tomcat Demos</title>
    <script src="${pageContext.request.contextPath}/js/jquery-1.8.2.js"></script>
</head>
<body>
<h1>Model And View</h1>
<table>
    <tr>
        <td>Id:</td>
        <td>${requestAsModelAndView.id}</td>
    </tr>
    <tr>
        <td>Titel:</td>
        <td>${requestAsModelAndView.title}</td>
    </tr>
    <tr>
        <td>Alter:</td>
        <td>${requestAsModelAndView.age}</td>
    </tr>
</table>

</body>
</html>