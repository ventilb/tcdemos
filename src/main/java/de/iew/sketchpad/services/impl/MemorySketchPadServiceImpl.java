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

package de.iew.sketchpad.services.impl;

import de.iew.sketchpad.services.SketchPadService;
import de.iew.sketchpad.services.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Implementiert einen {@link SketchPadService}, der die Polygone im Speicher
 * verwalter.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 11.11.12 - 10:35
 */
@Service(value = "sketchPadService")
public class MemorySketchPadServiceImpl implements SketchPadService {
    private static final Log log = LogFactory.getLog(MemorySketchPadServiceImpl.class);

    //private final Hashtable<Long, Polygon> polygonIndex = new Hashtable<Long, Polygon>();

    private final SortedMap<Long, Polygon> polygonIndex = new TreeMap<Long, Polygon>();

    private long polygonIds = 0;

    private final Hashtable<Long, RgbColor> colorIndex = new Hashtable<Long, RgbColor>();

    private final Hashtable<String, RgbColor> selectedColors = new Hashtable<String, RgbColor>();

    private final Hashtable<Long, Stroke> strokeIndex = new Hashtable<Long, Stroke>();

    private final Hashtable<String, Stroke> selectedStrokes = new Hashtable<String, Stroke>();

    @PreDestroy
    public void destroy() {
        FileOutputStream fos = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Beende mich " + this + ". Habe " + this.polygonIndex.size() + " Polygone im Speicher.");
            }

            fos = new FileOutputStream("/tmp/sketch.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.polygonIndex);
            oos.writeLong(this.polygonIds);
            oos.writeObject(this.colorIndex);
            oos.writeObject(this.selectedColors);
            oos.writeObject(this.strokeIndex);
            oos.writeObject(this.selectedStrokes);

            oos.flush();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Fehler beim Beenden des Sketch-Service.", e);
            }
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                if (log.isFatalEnabled()) {
                    log.fatal("Fehler beim Schließen des FileOutputStream", e);
                }
            }
        }
    }

    @PostConstruct
    public void construct() {
        FileInputStream fis = null;
        try {
            File sketchFile = new File("/tmp/sketch.dat");
            if (sketchFile.canRead()) {
                fis = new FileInputStream(sketchFile);
                ObjectInputStream ois = new ObjectInputStream(fis);

                this.polygonIndex.putAll((TreeMap<Long, Polygon>) ois.readObject());
                this.polygonIds = ois.readLong();
                this.colorIndex.putAll((Hashtable<Long, RgbColor>) ois.readObject());
                this.selectedColors.putAll((Hashtable<String, RgbColor>) ois.readObject());
                this.strokeIndex.putAll((Hashtable<Long, Stroke>) ois.readObject());
                this.selectedStrokes.putAll((Hashtable<String, Stroke>) ois.readObject());

                ois.close();
            } else {
                throw new IOException("Unable to read sketch file");
            }

        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Fehler beim Starten des Sketch-Service.", e);
            }
            constructNew();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                if (log.isFatalEnabled()) {
                    log.fatal("Fehler beim Schließen des FileInputStream", e);
                }
            }
        }
    }

    public Polygons listAllPolygons() {
        return new Polygons(this.polygonIndex.values());
    }

    public synchronized Polygon createPolygon(String sessionId, double x, double y, long lineColorId, long strokeId) {
        if (log.isDebugEnabled()) {
            log.debug("Erstelle Polygon bei x = " + x + " und y = " + y + ".");
        }

        Polygon polygon = new Polygon();
        if (polygon.addSegment(x, y)) {
            polygon.setId(++this.polygonIds);

            RgbColor color = getColorById(lineColorId);
            polygon.setLineColor(color);

            Stroke stroke = getStrokeById(strokeId);
            polygon.setStroke(stroke);

            this.polygonIndex.put(polygon.getId(), polygon);
            return polygon;
        }

        return null;
    }

    public boolean extendPolygon(String sessionId, long polygonId, double x, double y) {
        if (log.isDebugEnabled()) {
            log.debug("Erstelle neues Segment bei x = " + x + " und y = " + y + ".");
        }

        Polygon polygon = getPolygonById(polygonId);
        return polygon.addSegment(x, y);
    }

    public boolean closePolygon(String sessionId, long polygonId, double x, double y) {
        if (log.isDebugEnabled()) {
            log.debug("Schließe Polygon bei x = " + x + " und y = " + y + ".");
        }

        Polygon polygon = getPolygonById(polygonId);
        return polygon.addSegment(x, y);
    }

    public Colors listAllColors() {
        return new Colors(this.colorIndex.values());
    }

    public RgbColor chooseColor(String sessionId, long colorId) {
        RgbColor color = getColorById(colorId);
        this.selectedColors.put(sessionId, color);
        return color;
    }

    public Strokes listAllStrokes() {
        return new Strokes(this.strokeIndex.values());
    }

    public Stroke chooseStroke(String sessionId, long strokeId) {
        Stroke stroke = getStrokeById(strokeId);
        this.selectedStrokes.put(sessionId, stroke);
        return stroke;
    }

    public Polygon getPolygonById(long polygonId) {
        if (this.polygonIndex.containsKey(polygonId)) {
            return this.polygonIndex.get(polygonId);
        }
        throw new NoSuchElementException("The requested polygon " + polygonId + " was not found.");
    }

    public RgbColor getColorById(long colorId) {
        if (this.colorIndex.containsKey(colorId)) {
            return this.colorIndex.get(colorId);
        }
        throw new NoSuchElementException("The requested color " + colorId + " was not found.");
    }

    public Stroke getStrokeById(long strokeId) {
        if (this.strokeIndex.containsKey(strokeId)) {
            return this.strokeIndex.get(strokeId);
        }
        throw new NoSuchElementException("The requested stroke " + strokeId + " was not found.");
    }

    protected void constructNew() {
        RgbColor red = new RgbColor();
        red.setId(1l);
        red.setR(255);
        red.setG(0);
        red.setB(0);

        RgbColor green = new RgbColor();
        green.setId(2l);
        green.setR(0);
        green.setG(255);
        green.setB(0);

        RgbColor blue = new RgbColor();
        blue.setId(3l);
        blue.setR(0);
        blue.setG(0);
        blue.setB(255);

        this.colorIndex.put(red.getId(), red);
        this.colorIndex.put(green.getId(), green);
        this.colorIndex.put(blue.getId(), blue);

        RgbColor c = new RgbColor();
        c.setId(4l);
        c.setR(0);
        c.setG(100);
        c.setB(0);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(5l);
        c.setR(105);
        c.setG(139);
        c.setB(105);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(6l);
        c.setR(118);
        c.setG(238);
        c.setB(0);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(7l);
        c.setR(240);
        c.setG(230);
        c.setB(140);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(8l);
        c.setR(255);
        c.setG(165);
        c.setB(0);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(9l);
        c.setR(255);
        c.setG(250);
        c.setB(240);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(10l);
        c.setR(255);
        c.setG(215);
        c.setB(240);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(11l);
        c.setR(255);
        c.setG(255);
        c.setB(0);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(12l);
        c.setR(240);
        c.setG(255);
        c.setB(240);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(13l);
        c.setR(198);
        c.setG(226);
        c.setB(255);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(14l);
        c.setR(135);
        c.setG(206);
        c.setB(235);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(15l);
        c.setR(0);
        c.setG(0);
        c.setB(0);
        this.colorIndex.put(c.getId(), c);

        c = new RgbColor();
        c.setId(16l);
        c.setR(255);
        c.setG(255);
        c.setB(255);
        this.colorIndex.put(c.getId(), c);


        Stroke stroke1px = new Stroke();
        stroke1px.setId(1l);
        stroke1px.setStrokeWidth(1);

        Stroke stroke2px = new Stroke();
        stroke2px.setId(2l);
        stroke2px.setStrokeWidth(2);

        Stroke stroke3px = new Stroke();
        stroke3px.setId(3l);
        stroke3px.setStrokeWidth(3);

        Stroke stroke4px = new Stroke();
        stroke4px.setId(4l);
        stroke4px.setStrokeWidth(4);

        Stroke stroke10px = new Stroke();
        stroke10px.setId(5l);
        stroke10px.setStrokeWidth(10);

        this.strokeIndex.put(stroke1px.getId(), stroke1px);
        this.strokeIndex.put(stroke2px.getId(), stroke2px);
        this.strokeIndex.put(stroke3px.getId(), stroke3px);
        this.strokeIndex.put(stroke4px.getId(), stroke4px);
        this.strokeIndex.put(stroke10px.getId(), stroke10px);
    }
}
