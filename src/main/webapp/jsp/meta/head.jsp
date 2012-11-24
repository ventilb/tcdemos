<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iew" uri="/WEB-INF/taglibs/iew.tld" %>
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

<head>
    <title>i-entwicklung Tomcat Demos</title>
    <link href="${pageContext.request.contextPath}/static/css/page.css" rel="stylesheet" type="text/css"/>
    <script data-main="${pageContext.request.contextPath}/static/js/main.js" src="${pageContext.request.contextPath}/static/js/require.js"></script>
    <script type="text/javascript">
        function baseUrl(/* String|NULL */ url) {
            var baseUrl = '${pageContext.request.contextPath}';
            return baseUrl + '' + url;
        }
    </script>
    <script src="${pageContext.request.contextPath}/static/js/jquery-1.8.2.js" type="text/javascript"></script>
    <iew:loadIsc/>
</head>
