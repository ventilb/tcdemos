<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
<spring:message var="pageTitle" code="page.sketchpad.title"/>
<%@ include file="meta/head.jsp" %>
<body>
<%@ include file="meta/content_top.jsp" %>
<%@ include file="meta/content_head.jsp" %>
<div id="content">
    <script type="text/javascript">
        require(['jquery', 'sketchpad/sketch-pad'], function($, SketchPad) {
            $(document).ready(function() {
                var sketchPad = new SketchPad('#sketch_pad', ${sketchPad.id});
            });
        });
    </script>

    <div id="sketch_pad" class="sketch_pad">
        <div class="sketch_pad_canvas_container">
            <canvas width="${sketchPad.width}" height="${sketchPad.height}"></canvas>
        </div>
        <div id="right_margin_toolbar">
            <ul class="color_chooser">

            </ul>
            <ul class="stroke_chooser">

            </ul>
        </div>
    </div>
</div>
<%@ include file="meta/content_foot.jsp" %>
</body>
</html>