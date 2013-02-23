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

import de.iew.framework.domain.ModelNotFoundException;
import de.iew.services.AclEditorService;
import de.iew.services.SketchPadService;
import de.iew.services.events.SketchPadEvent;
import de.iew.sketchpad.domain.*;
import de.iew.sketchpad.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.message.GenericMessage;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Demo-Implementierung der {@link SketchPadService}-Schnittstelle und
 * Spring-ACL Integration.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.01.2013 - 14:41
 */
@Service(value = "sketchPadService")
public class SketchPadServiceImpl implements SketchPadService {

    /**
     * {@inheritDoc}
     *
     * @see <a href="http://stackoverflow.com/questions/9595781/how-do-i-reference-a-nested-type-in-spel">http://stackoverflow.com/questions/9595781/how-do-i-reference-a-nested-type-in-spel</a>
     */
    @PostAuthorize(value = "hasPermission(returnObject, 'READ') and " +
            "returnObject.state == T(de.iew.sketchpad.domain.SketchPad$State).ACTIVE")
    public SketchPad getActiveSketchPad() throws ModelNotFoundException {
        SketchPad activeSketchPad = this.sketchPadDao.findActive();

        if (activeSketchPad == null) {
            throw new ModelNotFoundException("An active sketch pad was not found.");
        }

        return activeSketchPad;
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.sketchpad.domain.SketchPad', 'READ')")
    @PostFilter(value = "hasPermission(filterObject, 'READ')")
    public List<Polygon> listAllPolygons(long sketchPadId) {
        return this.polygonDao.listPolygons(sketchPadId);
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.sketchpad.domain.SketchPad', 'READ')")
    @PostFilter(value = "hasPermission(filterObject, 'READ')")
    public List<Polygon> listAllPolygons(long sketchPadId, long fromPolygonId) {
        return this.polygonDao.listPolygonsFrom(sketchPadId, fromPolygonId);
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.sketchpad.domain.SketchPad', 'WRITE') and " +
            "hasPermission(#lineColorId, 'de.iew.sketchpad.domain.RgbColor', 'READ') and " +
            "hasPermission(#strokeId, 'de.iew.sketchpad.domain.Stroke', 'READ')"
    )
    public Polygon createPolygon(Authentication sketchPadUser, long sketchPadId, double[] x, double[] y, long lineColorId, long strokeId) throws ModelNotFoundException {
        Assert.isTrue(x.length == y.length);

        SketchPad sketchPad = this.sketchPadDao.findById(sketchPadId);
        RgbColor lineColor = getColorBySketchPadIdAndId(sketchPadId, lineColorId);
        Stroke stroke = getStrokeBySketchPadAndId(sketchPadId, strokeId);

        Polygon polygon = new Polygon();
        polygon.setState(Polygon.State.OPEN);
        polygon.setSketchPad(sketchPad);
        polygon.setLineColor(lineColor);
        polygon.setStroke(stroke);

        Segment segment;
        for (int segmentNum = 0; segmentNum < x.length; segmentNum++) {
            segment = new Segment();
            segment.setOrdinalNumber(segmentNum);
            segment.setX(x[segmentNum]);
            segment.setY(y[segmentNum]);

            segment.setPolygon(polygon);
            polygon.getSegments().add(segment);
        }
        polygon = this.polygonDao.save(polygon);

        if (this.applicationEventChannel != null) {
            SketchPadEvent sketchPadEvent = new SketchPadEvent(this, SketchPadEvent.Action.POLYGON_CREATED, polygon, sketchPadUser);
            Message<SketchPadEvent> eventMessage = new GenericMessage<SketchPadEvent>(sketchPadEvent);
            this.applicationEventChannel.send(eventMessage);
        }

        // ACL aufsetzen
        this.aclEditorService.setupDemoSketchPadPolygonPermissionsIfSketchPadAdmin(polygon.getId());
        this.aclEditorService.setupDemoSketchPadPolygonPermissionsIfSketchPadUser(polygon.getId());

        return polygon;
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.sketchpad.domain.SketchPad', 'READ')")
    @PostFilter(value = "hasPermission(filterObject, 'READ')")
    public List<RgbColor> listAllColors(long sketchPadId) {
        return this.sketchPadColorDao.listColorsBySketchPadId(sketchPadId);
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.sketchpad.domain.SketchPad', 'READ') and " +
            "hasPermission(#colorId, 'de.iew.sketchpad.domain.RgbColor', 'READ')")
    public RgbColor chooseColor(Authentication sketchPadUser, long sketchPadId, long colorId) throws ModelNotFoundException {
        return getColorBySketchPadIdAndId(sketchPadId, colorId);
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.sketchpad.domain.SketchPad', 'READ')")
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Stroke> listAllStrokes(long sketchPadId) {
        return this.sketchPadStrokeDao.listStrokesBySketchPadId(sketchPadId);
    }

    @PreAuthorize(value = "hasPermission(#sketchPadId, 'de.iew.sketchpad.domain.SketchPad', 'READ') and " +
            "hasPermission(#strokeId, 'de.iew.sketchpad.domain.Stroke', 'READ')")
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

    private MessageChannel applicationEventChannel;

    private AclEditorService aclEditorService;

    private SketchPadDao sketchPadDao;

    private PolygonDao polygonDao;

    private SegmentDao segmentDao;

    private RgbColorDao sketchPadColorDao;

    private StrokeDao sketchPadStrokeDao;

    @Autowired(required = false)
    public void setApplicationEventChannel(MessageChannel applicationEventChannel) {
        this.applicationEventChannel = applicationEventChannel;
    }

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
