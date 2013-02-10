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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;

/**
 * Implementiert den Controller für die Funktionen des Sketchpad.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 10.11.2012 - 16:47
 */
@Controller
@RequestMapping(value = "/sketchpad")
public class SketchPadController {
    private static final Log log = LogFactory.getLog(SketchPadController.class);

    private SketchPadService sketchPadService;

    @RequestMapping
    public ModelAndView indexAction() throws Exception {
        SketchPad sketchPad = this.sketchPadService.getActiveSketchPad();

        ModelAndView modelAndView = new ModelAndView("sketchpad");
        modelAndView.addObject("sketchPad", sketchPad);

        return modelAndView;
    }

    @RequestMapping(value = "/listpolygons")
    @ResponseBody
    public PolygonForms listPolygonsAction(
            @RequestParam long sketchPadId,
            @RequestParam Long lastInsertedShapeId
    ) throws Exception {
        PolygonToPolygonFormTransformer polygonModelTransformer = getPolygonToPolygonModelTransformer();

        List<Polygon> polygons;
        if (lastInsertedShapeId == null) {
            polygons = this.sketchPadService.listAllPolygons(sketchPadId);
        } else {
            polygons = this.sketchPadService.listAllPolygons(sketchPadId, lastInsertedShapeId);
        }

        Collection<PolygonForm> polygonModels = polygonModelTransformer.visitCollection(polygons);
        return new PolygonForms(polygonModels);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/newpolygon")
    @ResponseBody
    public Long createPolygonAction(
            Authentication sketchPadUser,
            @RequestBody PolygonForm polygonForm) throws Exception {
        SegmentForm[] segmentForms = polygonForm.getSegments();
        double[] x = new double[segmentForms.length];
        double[] y = new double[segmentForms.length];
        for (int segmentNum = 0; segmentNum < segmentForms.length; segmentNum++) {
            x[segmentNum] = segmentForms[segmentNum].getX();
            y[segmentNum] = segmentForms[segmentNum].getY();
        }

        Polygon polygon = this.sketchPadService.createPolygon(sketchPadUser, polygonForm.getSketchPadId(), x, y, polygonForm.getLineColorId(), polygonForm.getStrokeId());
        return polygon.getId();
    }

    @RequestMapping(value = "/listcolors")
    @ResponseBody
    public ColorModels listColorsAction(
            @RequestParam long sketchPadId
    ) throws Exception {
        ColorToColorModelTransformer colorModelTransformer = getColorModelTransformer();

        return new ColorModels(colorModelTransformer.visitCollection(this.sketchPadService.listAllColors(sketchPadId)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/choosecolor")
    @ResponseBody
    public RgbColorModel chooseColorAction(
            Authentication sketchPadUser,
            @RequestParam long sketchPadId,
            @RequestParam long colorId) throws Exception {
        ColorToColorModelTransformer colorModelTransformer = getColorModelTransformer();

        return colorModelTransformer.visit(this.sketchPadService.chooseColor(sketchPadUser, sketchPadId, colorId));
    }

    @RequestMapping(value = "/liststrokes")
    @ResponseBody
    public StrokeModels listStrokesAction(
            @RequestParam long sketchPadId
    ) throws Exception {
        StrokeToStrokeModelTransformer strokeModelTransformer = getStrokeModelTransformer();

        return new StrokeModels(strokeModelTransformer.visitCollection(this.sketchPadService.listAllStrokes(sketchPadId)));
    }

    @RequestMapping(value = "/choosestroke")
    @ResponseBody
    public StrokeModel chooseStrokeAction(
            Authentication sketchPadUser,
            @RequestParam long sketchPadId,
            @RequestParam long strokeId) throws Exception {
        StrokeToStrokeModelTransformer strokeModelTransformer = getStrokeModelTransformer();

        return strokeModelTransformer.visit(this.sketchPadService.chooseStroke(sketchPadUser, sketchPadId, strokeId));
    }

    @ExceptionHandler(Exception.class)
    public Model onException(Exception e) {
        if (log.isErrorEnabled()) {
            log.error("Fehler während der Webservice Verarbeitung", e);
        }
        return null;
    }

    public PolygonToPolygonFormTransformer getPolygonToPolygonModelTransformer() {
        return new PolygonToPolygonFormTransformer();
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
