<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iew" uri="/WEB-INF/taglibs/iew.tld" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<div id="content_top">
    <div class="inner">
        <sec:authorize access="isAnonymous()">
            <a href="${baseUrl}/identity/login.html">Login</a>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <sec:authentication var="authenticatedUsername" property="principal.username"/>
            <spring:message code="welcome.username.message" arguments="${authenticatedUsername}"/>
            <a href="${baseUrl}/identity/logout.html">Logout</a>
        </sec:authorize>
    </div>
</div>
