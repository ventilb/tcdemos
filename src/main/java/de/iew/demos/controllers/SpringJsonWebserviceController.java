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

import de.iew.demos.model.SimpleJsonModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * Implementiert einen Controller zur Demonstration der verschiedenen
 * JSON-(De)Serialierungs-Fähigkeiten.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 10.11.12 - 10:43
 */
@Controller
public class SpringJsonWebserviceController {

    private static final Log log = LogFactory.getLog(SpringJsonWebserviceController.class);

    /**
     * Aufruf mit /modelandview.html liefert eine HTML-Seite zu dieser
     * Ressource aus (rendert modelandview.jsp). Aufruf mit /modelandview.json
     * liefert das Modell als JSON-String aus.
     * <p>
     * Ohne die {@link org.springframework.web.servlet.view.ContentNegotiatingViewResolver}
     * Konfiguration muss in jedem Fall auch die JSP <code>modelandview.jsp</code>
     * implementiert werden, da wir ein {@link ModelAndView}-Modell zurückgeben.
     * </p>
     * <p>
     * Mit der {@link org.springframework.web.servlet.view.ContentNegotiatingViewResolver}
     * Konfiguration und entsprechenden Content-Type und Accept Header kann auf die JSP
     * verzichtet werden, wenn JSON angefordert werden soll.
     * </p>
     * <pre>
     * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X GET http://localhost:8080/modelandview.json
     * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X GET http://localhost:8080/modelandview.html
     * </pre>
     *
     * @return Das Model und die Sicht.
     */
    @RequestMapping("/modelandview")
    public ModelAndView requestAsModelAndView() {

        if (log.isDebugEnabled()) {
            log.debug("requestAsModelAndView wurde aufgerufen");
        }

        ModelAndView mav = new ModelAndView("modelandview");
        SimpleJsonModel model = new SimpleJsonModel();
        model.setId(1l);
        model.setTitle("Age: ");
        model.setAge(1);

        mav.addObject("requestAsModelAndView", model);

        return mav;
    }

    /**
     * Liefert immer einen JSON-String aus. Der Unterschied ist die
     * {@link ResponseBody}-Annotation.
     * <p>
     * Funktioniert auch ohne die {@link org.springframework.web.servlet.view.ContentNegotiatingViewResolver}
     * Konfiguration.
     * </p>
     * <pre>
     * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X GET http://localhost:8080/getmodel.json
     * </pre>
     *
     * @return Das Modell.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getmodel")
    @ResponseBody
    public SimpleJsonModel getAsSimpleModel() {
        if (log.isDebugEnabled()) {
            log.debug("getAsSimpleModel wurde aufgerufen");
        }

        SimpleJsonModel model = new SimpleJsonModel();
        model.setTitle("Age: ");
        model.setAge(1);

        return model;
    }

    /**
     * Werte die per HTTP-Post übermittelten JSON-Werte aus und konfiguriert
     * daraus das <code>model</code> Objekt. Liefert das Modell zurück. Spring
     * liefert dann einen JSON-String zum Browser zurück.
     * <pre>
     * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"title":"Foo", "age":"2"}' http://localhost:8080/postmodel.json
     * </pre>
     *
     * @param model Das Modell.
     * @return Das Modell.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/postmodel")
    @ResponseBody
    public SimpleJsonModel postAsSimpleModel(@RequestBody SimpleJsonModel model) {
        if (log.isDebugEnabled()) {
            log.debug("postAsSimpleModel wurde aufgerufen: " + model.getTitle());
        }

        model.setTitle("Bar");
        model.setAge(1);

        return model;
    }

    /**
     * Erhält ein JSON-Array und liefert ein JSON-Array zurück.
     * <p>
     * Spring wertet jedes Array-Element aus und veröffentlicht die Werte
     * in ein {@link SimpleJsonModel} Objekt.
     * </p>
     * <p>
     * Man kann genauso auch Objekt-Listen automatisch in JSON rendern lassen.
     * </p>
     * <pre>
     * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '[{"id":"1","title":"Foo", "age":"2"},{"id":"2","title":"Bar", "age":"1"}]' http://localhost:8080/postmodellist.json
     * </pre>
     *
     * @param models Liste der Modelle.
     * @return Liste der Modelle.
     * @see <a href="http://stackoverflow.com/questions/10864049/map-json-array-of-objects-to-requestbody-listt-using-jackson">JSON-Array in Liste serialisieren</a>
     */
    @RequestMapping(method = RequestMethod.POST, value = "/postmodellist")
    @ResponseBody
    public SimpleJsonModelList postAsSimpleModelList(@RequestBody SimpleJsonModelList models) {
        if (log.isDebugEnabled()) {
            log.debug("postAsSimpleModel wurde aufgerufen: " + models);
        }

        for (SimpleJsonModel model : models) {
            if (log.isDebugEnabled()) {
                log.debug("Iteriere über Modell " + model + ".");
            }
        }

        return models;
    }

    /**
     * Funktioniert ohne die {@link org.springframework.web.servlet.view.ContentNegotiatingViewResolver}
     * Konfiguration.
     * <p/>
     * <pre>
     * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '[{"id":"1","title":"Foo", "age":"2"},{"id":"2","title":"Bar", "age":"1"}]' http://localhost:8080/postmodellistparam.json?modelId=1
     * </pre>
     *
     * @param modelId Die Id des angeforderten Modells.
     * @param models  Liste der Modelle.
     * @return Das angeforderte Modell.
     * @see <a href="http://stackoverflow.com/questions/10864049/map-json-array-of-objects-to-requestbody-listt-using-jackson">JSON-Array in Liste serialisieren</a>
     */
    @RequestMapping(method = RequestMethod.POST, value = "/postmodellistparam")
    @ResponseBody
    public SimpleJsonModel postAsSimpleModelListAndParameter(
            @RequestParam long modelId,
            @RequestBody SimpleJsonModelList models
    ) {
        if (log.isDebugEnabled()) {
            log.debug("postAsSimpleModel wurde aufgerufen: " + models);
        }

        for (SimpleJsonModel model : models) {
            if (modelId == model.getId()) {
                return model;
            }
        }

        return null;
    }


    public static class SimpleJsonModelList extends ArrayList<SimpleJsonModel> {
    }
}
