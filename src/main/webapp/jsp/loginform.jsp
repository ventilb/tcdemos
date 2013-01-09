<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<spring:message var="pageTitle" code="page.login.title"/>
<%@ include file="meta/head.jsp" %>
<body>
<c:set var="baseUrl" value="${pageContext.request.contextPath}"/>
<div id="content_top"></div>
<%@ include file="meta/content_head.jsp" %>
<div id="content" class="login_form">
    <form:form action="${baseUrl}/identity/login.html" commandName="loginForm" cssClass="zend_dojo_form login_form">
        <div class="form_inner">
            <fieldset id="fieldset-authelements" class="zend_dojo_form_displaygroup with_legend">
                <legend>
                    <span>
                        <spring:message code="forms.login_form.fieldset.legend.title"/>
                    </span>
                </legend>
                <div class="display_group_inner">
                    <c:if test="${error}">
                        <spring:message code="${loginErrorMessage}"/>
                    </c:if>
                    <div class="form_element">
                        <div class="input">
                            <div id="loginname-label" class="label required">
                                <label for="username" class="label required">
                                    <spring:message code="forms.login_form.username.element.label"/>
                                </label>
                            </div>
                            <div class="element required" id="loginname-element">
                                <form:input id="username" path="username"/>
                            </div>
                            <!--
                            http://stackoverflow.com/questions/1653438/spring-forms-how-to-check-for-error-on-specific-path
                            -->
                            <c:set var="usernameErrors"><form:errors path="username"/></c:set>
                            <c:if test="${not empty usernameErrors}">
                                <ul class="errors">
                                    <form:errors path="username" element="li"/>
                                </ul>
                            </c:if>
                        </div>
                    </div>
                    <div class="form_element">
                        <div class="input">
                            <div id="password-label" class="label required">
                                <label for="password" class="label required">
                                    <spring:message code="forms.login_form.password.element.label"/>
                                </label>
                            </div>
                            <div class="element required" id="password-element">
                                <form:password id="password" path="password"/>
                            </div>
                            <!--
                            http://stackoverflow.com/questions/1653438/spring-forms-how-to-check-for-error-on-specific-path
                            -->
                            <c:set var="passwordErrors"><form:errors path="password"/></c:set>
                            <c:if test="${not empty passwordErrors}">
                                <ul class="errors">
                                    <form:errors path="password" element="li"/>
                                </ul>
                            </c:if>
                            <div class="clearboth"></div>
                        </div>
                    </div>
                </div>
            </fieldset>
            <fieldset id="fieldset-controls" class="iew_form_controls_displaygroup">
                <div class="display_group_inner">
                    <div class="form_element">
                        <div class="input">
                            <div class="element required" id="login-element">
                                <spring:message code="buttons.login.title" var="buttonsLoginTitle"/>
                                <input class="primary_action" id="login" name="login" value="${buttonsLoginTitle}"
                                       type="submit">
                            </div>
                            <div class="clearboth"></div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
    </form:form>
</div>
<%@ include file="meta/content_foot.jsp" %>
</body>
</html>