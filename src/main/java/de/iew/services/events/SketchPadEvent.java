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

import de.iew.framework.audit.AuditEvent;
import de.iew.framework.domain.audit.Severity;
import de.iew.sketchpad.domain.Polygon;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

/**
 * Implements a spring application event to notify interested listeners about
 * changes in the sketch pad service.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @see <a href="http://learningviacode.blogspot.de/2012/07/spring-and-events-and-listeners.html">http://learningviacode.blogspot.de/2012/07/spring-and-events-and-listeners.html</a>
 * @since 24.01.13 - 21:02
 */
public class SketchPadEvent extends ApplicationEvent implements AuditEvent {

    /**
     * The authentication which caused this event.
     */
    private Authentication authentication;

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
     * @param source         the component that published the event (never <code>null</code>)
     * @param eventAction    the event action
     * @param polygon        the polygon
     * @param authentication the authentication causing this sketch pad event
     */
    public SketchPadEvent(Object source, Action eventAction, Polygon polygon, Authentication authentication) {
        super(source);
        Assert.notNull(eventAction);
        Assert.notNull(polygon);

        this.eventAction = eventAction;
        this.polygon = polygon;
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return this.authentication;
    }

    public Severity getSeverity() {
        return Severity.DEBUG;
    }

    public String getMessage() {
        String message;

        switch (this.eventAction) {
            case POLYGON_CREATED:
                message = "The polygon " + this.polygon.getId() + " was created.";
                break;
            case POLYGON_UPDATED:
                message = "The polygon " + this.polygon.getId() + " was changed.";
                break;
            case POLYGON_CLOSED:
                message = "The polygon " + this.polygon.getId() + " was closed.";
                break;
            default:
                message = "UNSUPPORTED AuditEvent action " + this.eventAction + ". The polygon " + this.polygon.getId() + " changed.";
        }

        return message;
    }

    public Throwable getThrowable() {
        return null;
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
        POLYGON_CLOSED
    }
}
