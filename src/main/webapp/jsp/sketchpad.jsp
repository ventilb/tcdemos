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
<div id="content_head">
    <div class="inner">
        SketchPad Demo
    </div>
</div>
<div id="content">
    <script src="${pageContext.request.contextPath}/static/js/paint.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/static/js/sketchPad.js" type="text/javascript"></script>
    <div id="sketch_pad" class="sketch_pad">
        <canvas width="1000" height="700"></canvas>
        <div id="right_margin_toolbar">
            <ul id="color_chooser">

            </ul>
            <ul id="stroke_chooser">

            </ul>
        </div>
    </div>
</div>
<div id="content_foot">
    <div class="inner">
        &copy; 2012 by Manuel Schulze &#8210; <a href="mailto:manuel_schulze@i-entwicklung.de">manuel_schulze@i-entwicklung.de</a>
    </div>
</div>
</body>
</html>