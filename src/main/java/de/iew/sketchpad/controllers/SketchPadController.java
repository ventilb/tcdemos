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

package de.iew.sketchpad.controllers;

import de.iew.sketchpad.services.SketchPadService;
import de.iew.sketchpad.services.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Implementiert den Controller für die Funktionen des Sketchpad.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 10.11.12 - 16:47
 */
@Controller
@RequestMapping(value = "/sketchpad")
public class SketchPadController {
    private static final Log log = LogFactory.getLog(SketchPadController.class);

    private SketchPadService sketchPadService;

    @RequestMapping
    public String indexAction() {
        return "sketchpad";
    }

    @RequestMapping(value = "/listpolygons")
    @ResponseBody
    public Polygons listPolygonsAction() {
        return this.sketchPadService.listAllPolygons();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/newpolygon")
    @ResponseBody
    public Long createPolygonAction(
            HttpServletRequest request,
            @RequestParam double x,
            @RequestParam double y,
            @RequestParam long lineColorId,
            @RequestParam long strokeId) {
        String sessionId = request.getSession(true).getId();

        if (log.isDebugEnabled()) {
            log.debug("Session Id " + sessionId);
        }

        Polygon polygon = this.sketchPadService.createPolygon(sessionId, x, y, lineColorId, strokeId);
        return polygon.getId();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addsegment")
    @ResponseBody
    public Boolean extendPolygonAction(
            HttpServletRequest request,
            @RequestParam long polygonId,
            @RequestParam double x,
            @RequestParam double y) {
        boolean webServiceResult = false;

        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionId = session.getId();
            if (log.isDebugEnabled()) {
                log.debug("Session Id " + session.getId());
            }

            webServiceResult = this.sketchPadService.extendPolygon(sessionId, polygonId, x, y);
        }

        return webServiceResult;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/closepolygon")
    @ResponseBody
    public Boolean closePolygonAction(
            HttpServletRequest request,
            @RequestParam long polygonId,
            @RequestParam double x,
            @RequestParam double y) {
        boolean webServiceResult = false;

        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionId = session.getId();
            if (log.isDebugEnabled()) {
                log.debug("Session Id " + session.getId());
            }

            webServiceResult = this.sketchPadService.closePolygon(sessionId, polygonId, x, y);
        }

        return webServiceResult;
    }

    @RequestMapping(value = "/listcolors")
    @ResponseBody
    public Colors listColorsAction() {
        return this.sketchPadService.listAllColors();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/choosecolor")
    @ResponseBody
    public RgbColor chooseColorAction(
            HttpServletRequest request,
            @RequestParam long colorId) {
        if (log.isDebugEnabled()) {
            log.debug("Setze Farbe " + colorId);
        }
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();
        return this.sketchPadService.chooseColor(sessionId, colorId);
    }

    @RequestMapping(value = "/liststrokes")
    @ResponseBody
    public Strokes listStrokesAction() {
        return this.sketchPadService.listAllStrokes();
    }

    @RequestMapping(value = "/choosestroke")
    @ResponseBody
    public Stroke chooseStrokeAction(
            HttpServletRequest request,
            @RequestParam long strokeId) {
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();
        return this.sketchPadService.chooseStroke(sessionId, strokeId);
    }

    @Autowired
    public void setSketchPadService(SketchPadService sketchPadService) {
        this.sketchPadService = sketchPadService;
    }

    // TODO Testcode //////////////////////////////////////////////////////////

    @RequestMapping(method = RequestMethod.POST, value = "/setrgbcolor")
    @ResponseBody
    public Boolean setColorAction(
            HttpServletRequest request,
            @RequestBody RgbColor color) {
        return setColorImpl(request, color);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/setrgbacolor")
    @ResponseBody
    public Boolean setColorAction(
            HttpServletRequest request,
            @RequestBody RgbaColor color) {
        if (log.isDebugEnabled()) {
            log.debug("Setze Farbe " + color);
        }
        return setColorImpl(request, color);
    }

    protected boolean setColorImpl(HttpServletRequest request, RgbColor color) {
        boolean webServiceResult = false;

        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionId = session.getId();
            if (log.isDebugEnabled()) {
                log.debug("Session Id " + session.getId());
            }

            // @TODO addColor
            //this.sketchPadService.setColor(sessionId, color);
        }

        return webServiceResult;
    }
}
