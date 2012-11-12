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

import de.iew.demos.model.ImageFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;

/**
 * Testcontroller zum Verschieben einer Fläche in einem HTML-Canvas.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 10.11.12 - 16:47
 */
@Controller
@RequestMapping(value = "/photo")
public class PhotoController {
    private static final Log log = LogFactory.getLog(PhotoController.class);

    private Point p1 = new Point(20, 20);
    private Point p2 = new Point(80, 80);

    @RequestMapping
    public String indexAction() {
        return "photo";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getimageframe")
    @ResponseBody
    public ImageFrame getImageFrame() {
        ImageFrame imageFrame = new ImageFrame();
        imageFrame.setPx1(this.p1.x);
        imageFrame.setPy1(this.p1.y);
        imageFrame.setPx2(this.p2.x);
        imageFrame.setPy2(this.p2.y);

        return imageFrame;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/setimageframe")
    @ResponseBody
    public ImageFrame setImageFrame(@RequestBody ImageFrame imageFrame) {
        this.p1.x = imageFrame.getPx1();
        this.p1.y = imageFrame.getPy1();
        this.p2.x = imageFrame.getPx2();
        this.p2.y = imageFrame.getPy2();

        return imageFrame;
    }

}
