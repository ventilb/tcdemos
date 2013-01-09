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

package de.iew.demos.controllers;

import de.iew.demos.model.*;
import de.iew.domain.sketchpad.*;
import de.iew.services.SketchPadService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;

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
    public PolygonModels listPolygonsAction() throws Exception {
        PolygonToPolygonModelTransformer polygonModelTransformer = getPolygonToPolygonModelTransformer();

        SketchPad sketchPad = this.sketchPadService.getActiveSketchPad();
        List<Polygon> polygons = this.sketchPadService.listAllPolygons(sketchPad.getId());

        Collection<PolygonModel> polygonModels = polygonModelTransformer.visitCollection(polygons);
        return new PolygonModels(polygonModels);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/newpolygon")
    @ResponseBody
    public Long createPolygonAction(
            Authentication sketchPadUser,
            @RequestParam double x,
            @RequestParam double y,
            @RequestParam long lineColorId,
            @RequestParam long strokeId) throws Exception {
        SketchPad sketchPad = this.sketchPadService.getActiveSketchPad();

        Polygon polygon = this.sketchPadService.createPolygon(sketchPadUser, sketchPad.getId(), x, y, lineColorId, strokeId);
        return polygon.getId();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addsegment")
    @ResponseBody
    public Boolean extendPolygonAction(
            Authentication sketchPadUser,
            @RequestParam long polygonId,
            @RequestParam double x,
            @RequestParam double y) throws Exception {
        boolean webServiceResult = false;

        webServiceResult = this.sketchPadService.extendPolygon(sketchPadUser, polygonId, x, y);

        return webServiceResult;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/closepolygon")
    @ResponseBody
    public Boolean closePolygonAction(
            Authentication sketchPadUser,
            @RequestParam long polygonId,
            @RequestParam double x,
            @RequestParam double y) throws Exception {
        boolean webServiceResult = false;

        webServiceResult = this.sketchPadService.closePolygon(sketchPadUser, polygonId, x, y);

        return webServiceResult;
    }

    @RequestMapping(value = "/listcolors")
    @ResponseBody
    public ColorModels listColorsAction() throws Exception {
        ColorToColorModelTransformer colorModelTransformer = getColorModelTransformer();

        SketchPad sketchPad = this.sketchPadService.getActiveSketchPad();
        return new ColorModels(colorModelTransformer.visitCollection(this.sketchPadService.listAllColors(sketchPad.getId())));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/choosecolor")
    @ResponseBody
    public RgbColorModel chooseColorAction(
            Authentication sketchPadUser,
            @RequestParam long colorId) throws Exception {
        ColorToColorModelTransformer colorModelTransformer = getColorModelTransformer();

        SketchPad sketchPad = this.sketchPadService.getActiveSketchPad();
        return colorModelTransformer.visit(this.sketchPadService.chooseColor(sketchPadUser, sketchPad.getId(), colorId));
    }

    @RequestMapping(value = "/liststrokes")
    @ResponseBody
    public StrokeModels listStrokesAction() throws Exception {
        StrokeToStrokeModelTransformer strokeModelTransformer = getStrokeModelTransformer();

        SketchPad sketchPad = this.sketchPadService.getActiveSketchPad();
        return new StrokeModels(strokeModelTransformer.visitCollection(this.sketchPadService.listAllStrokes(sketchPad.getId())));
    }

    @RequestMapping(value = "/choosestroke")
    @ResponseBody
    public StrokeModel chooseStrokeAction(
            Authentication sketchPadUser,
            @RequestParam long strokeId) throws Exception {
        StrokeToStrokeModelTransformer strokeModelTransformer = getStrokeModelTransformer();

        SketchPad sketchPad = this.sketchPadService.getActiveSketchPad();
        return strokeModelTransformer.visit(this.sketchPadService.chooseStroke(sketchPadUser, sketchPad.getId(), strokeId));
    }

    @ExceptionHandler(Exception.class)
    public Model onException(Exception e) {
        if (log.isErrorEnabled()) {
            log.error("Fehler während der Webservice Verarbeitung", e);
        }
        return null;
    }

    public PolygonToPolygonModelTransformer getPolygonToPolygonModelTransformer() {
        return new PolygonToPolygonModelTransformer();
    }

    public StrokeToStrokeModelTransformer getStrokeModelTransformer() {
        return new StrokeToStrokeModelTransformer();
    }

    public ColorToColorModelTransformer getColorModelTransformer() {
        return new ColorToColorModelTransformer();
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
