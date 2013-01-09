/*
 * Copyright 2012-2013 Manuel Schulze <manuel_schulze@i-entwicklung.de>
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

package de.iew.services.impl;

import de.iew.domain.ModelNotFoundException;
import de.iew.domain.sketchpad.*;
import de.iew.persistence.*;
import de.iew.services.AclEditorService;
import de.iew.services.SketchPadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Demo-Implementierung der {@link SketchPadService}-Schnittstelle und
 * Spring-ACL Integration.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.13 - 14:41
 */
@Service(value = "sketchPadService")
public class SketchPadServiceImpl implements SketchPadService {

    /**
     * {@inheritDoc}
     *
     * @see <a href="http://stackoverflow.com/questions/9595781/how-do-i-reference-a-nested-type-in-spel">http://stackoverflow.com/questions/9595781/how-do-i-reference-a-nested-type-in-spel</a>
     */
    @PostAuthorize(value = "hasPermission(returnObject, 'READ') and " +
            "returnObject.state == T(de.iew.domain.sketchpad.SketchPad$State).ACTIVE")
    public SketchPad getActiveSketchPad() throws ModelNotFoundException {
        SketchPad activeSketchPad = this.sketchPadDao.findActive();

        if (activeSketchPad == null) {
            throw new ModelNotFoundException("An active sketch pad was not found.");
        }

        return activeSketchPad;
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.domain.sketchpad.SketchPad', 'READ')")
    @PostFilter(value = "hasPermission(filterObject, 'READ')")
    public List<Polygon> listAllPolygons(long sketchPadId) {
        return this.polygonDao.listPolygons(sketchPadId);
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.domain.sketchpad.SketchPad', 'WRITE') and " +
            "hasPermission(#lineColorId, 'de.iew.domain.sketchpad.RgbColor', 'READ') and " +
            "hasPermission(#strokeId, 'de.iew.domain.sketchpad.Stroke', 'READ')"
    )
    public Polygon createPolygon(Authentication sketchPadUser, long sketchPadId, double x, double y, long lineColorId, long strokeId) throws ModelNotFoundException {
        SketchPad activeSketchPad = this.sketchPadDao.findById(sketchPadId);
        RgbColor lineColor = getColorBySketchPadIdAndId(sketchPadId, lineColorId);
        Stroke stroke = getStrokeBySketchPadAndId(sketchPadId, strokeId);

        Polygon polygon = new Polygon();
        polygon.setState(Polygon.State.OPEN);
        polygon.setSketchPad(activeSketchPad);
        polygon.setLineColor(lineColor);
        polygon.setStroke(stroke);

        polygon = this.polygonDao.save(polygon);

        // ACL aufsetzen
        this.aclEditorService.setupDemoSketchPadPolygonPermissionsIfSketchPadAdmin(polygon.getId());
        this.aclEditorService.setupDemoSketchPadPolygonPermissionsIfSketchPadUser(polygon.getId());

        extendPolygon(sketchPadUser, polygon.getId(), x, y);

        return polygon;
    }

    @PreAuthorize(value = "hasPermission(#polygonId, 'de.iew.domain.sketchpad.Polygon', 'WRITE')")
    public boolean extendPolygon(Authentication sketchPadUser, long polygonId, double x, double y) throws ModelNotFoundException {
        Polygon polygon = getPolygonById(polygonId);

        Segment segment = new Segment();
        segment.setOrdinalNumber((int) this.polygonDao.getSegmentCount(polygonId));
        segment.setX(x);
        segment.setY(y);

        segment.setPolygon(polygon);
        this.segmentDao.save(segment);
        /*
        Achtung Bad Code: Wenn ein Polygon sehr viele Segmente hat, dann
        geht die Anwendung hier in die Knie weil getSegments() erst die Segmente
        laden muss.
        Besser: Speichern des Segments in die Dao Ebene verlagern. Dann können wir
        das besser behandeln. Oder das Polygon Objekt neu aus der Datenbank laden.
        Dann werden auch die Assoziationen neu geladen. Nachteil dieser Lösung
        ist, eine zusätzliche Datenbankabfrage.
         */
        //polygon.getSegments().add(segment);
        this.polygonDao.refresh(polygon);

        return true;
    }

    @PreAuthorize(value = "hasPermission(#polygonId, 'de.iew.domain.sketchpad.Polygon', 'WRITE')")
    public boolean closePolygon(Authentication sketchPadUser, long polygonId, double x, double y) throws ModelNotFoundException {
        extendPolygon(sketchPadUser, polygonId, x, y);
        Polygon polygon = getPolygonById(polygonId);
        polygon.setState(Polygon.State.CLOSED);

        // TODO Schreibrecht entziehen

        return true;
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.domain.sketchpad.SketchPad', 'READ')")
    @PostFilter(value = "hasPermission(filterObject, 'READ')")
    public List<RgbColor> listAllColors(long sketchPadId) {
        return this.sketchPadColorDao.listColorsBySketchPadId(sketchPadId);
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.domain.sketchpad.SketchPad', 'READ') and " +
            "hasPermission(#colorId, 'de.iew.domain.sketchpad.RgbColor', 'READ')")
    public RgbColor chooseColor(Authentication sketchPadUser, long sketchPadId, long colorId) throws ModelNotFoundException {
        return getColorBySketchPadIdAndId(sketchPadId, colorId);
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.domain.sketchpad.SketchPad', 'READ')")
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Stroke> listAllStrokes(long sketchPadId) {
        return this.sketchPadStrokeDao.listStrokesBySketchPadId(sketchPadId);
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.domain.sketchpad.SketchPad', 'READ') and " +
            "hasPermission(#strokeId, 'de.iew.domain.sketchpad.Stroke', 'READ')")
    public Stroke chooseStroke(Authentication sketchPadUser, long sketchPadId, long strokeId) throws ModelNotFoundException {
        return getStrokeBySketchPadAndId(sketchPadId, strokeId);
    }

    // Hilfsmethoden //////////////////////////////////////////////////////////

    public Polygon getPolygonById(long polygonId) throws ModelNotFoundException {
        Polygon polygon = this.polygonDao.findById(polygonId);

        if (polygon == null) {
            throw new ModelNotFoundException("The requested polygon " + polygonId + " was not found.");
        }

        return polygon;
    }

    public RgbColor getColorBySketchPadIdAndId(long sketchPadId, long colorId) throws ModelNotFoundException {
        RgbColor color = this.sketchPadColorDao.getColorBySketchPadIdAndId(sketchPadId, colorId);

        if (color == null) {
            throw new ModelNotFoundException("The requested color " + colorId + " was not found for sketch pad " + sketchPadId + ".");
        }

        return color;
    }

    public Stroke getStrokeBySketchPadAndId(long sketchPadId, long strokeId) throws ModelNotFoundException {
        Stroke stroke = this.sketchPadStrokeDao.getStrokeBySketchPadIdAndId(sketchPadId, strokeId);

        if (stroke == null) {
            throw new ModelNotFoundException("The requested stroke " + strokeId + " was not found for sketch spad " + sketchPadId + ".");
        }

        return stroke;
    }

    // Service und Dao Abhängigkeiten /////////////////////////////////////////

    private AclEditorService aclEditorService;

    private SketchPadDao sketchPadDao;

    private PolygonDao polygonDao;

    private SegmentDao segmentDao;

    private RgbColorDao sketchPadColorDao;

    private StrokeDao sketchPadStrokeDao;

    @Autowired
    public void setAclEditorService(AclEditorService aclEditorService) {
        this.aclEditorService = aclEditorService;
    }

    @Autowired
    public void setSketchPadDao(SketchPadDao sketchPadDao) {
        this.sketchPadDao = sketchPadDao;
    }

    @Autowired
    public void setPolygonDao(PolygonDao polygonDao) {
        this.polygonDao = polygonDao;
    }

    @Autowired
    public void setSegmentDao(SegmentDao segmentDao) {
        this.segmentDao = segmentDao;
    }

    @Autowired
    public void setSketchPadColorDao(RgbColorDao sketchPadColorDao) {
        this.sketchPadColorDao = sketchPadColorDao;
    }

    @Autowired
    public void setSketchPadStrokeDao(StrokeDao sketchPadStrokeDao) {
        this.sketchPadStrokeDao = sketchPadStrokeDao;
    }
}
