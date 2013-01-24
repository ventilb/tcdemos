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

package de.iew.services.events;

import de.iew.domain.sketchpad.Polygon;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;

/**
 * Implements a spring application event to notify interested listeners about
 * changes in the sketch pad service.
 *
 * @author Manuel Schulze <mschulze@geneon.de>
 * @see <a href="http://learningviacode.blogspot.de/2012/07/spring-and-events-and-listeners.html">http://learningviacode.blogspot.de/2012/07/spring-and-events-and-listeners.html</a>
 * @since 24.01.13 - 21:02
 */
public class SketchPadEvent extends ApplicationEvent implements AuditEvent {

    /**
     * The action which caused this event.
     */
    private final Action eventAction;

    /**
     * The polygon which caused this event.
     */
    private final Polygon polygon;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source      the component that published the event (never <code>null</code>)
     * @param eventAction the event action
     * @param polygon     the polygon
     */
    public SketchPadEvent(Object source, Action eventAction, Polygon polygon) {
        super(source);
        Assert.notNull(eventAction);
        Assert.notNull(polygon);

        this.eventAction = eventAction;
        this.polygon = polygon;
    }

    /**
     * Gets the action which caused this event.
     *
     * @return the action which caused this event
     */
    public Action getEventAction() {
        return eventAction;
    }

    /**
     * Gets the polygon which caused this event.
     *
     * @return the polygon which caused this event
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * The event actions.
     */
    public static enum Action {
        /**
         * The POLYGON_CREATED.
         */
        POLYGON_CREATED,
        /**
         * The POLYGON_UPDATED.
         */
        POLYGON_UPDATED,
        /**
         * The POLYGON_CLOSED.
         */
        POLYGON_CLOSED;
    }
}
