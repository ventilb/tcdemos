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

package de.iew.framework.log4j;

import de.iew.services.events.AuditEvent;
import de.iew.services.events.GenericAuditEvent;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.message.GenericMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * Implements a Log4j appender to fire Log4j messages as audit events.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.02.13 - 21:51
 */
public class AuditServiceAppender extends AppenderSkeleton {

    /**
     * The constant DEFAULT_AUDIT_SERVICE_APPENDER_LOG4J_PRIORITY.
     */
    public static final Level DEFAULT_AUDIT_SERVICE_APPENDER_LOG4J_PRIORITY = Level.INFO;

    private volatile transient MessageChannel auditEventChannel;

    private Level auditServiceAppenderLog4jPriority = DEFAULT_AUDIT_SERVICE_APPENDER_LOG4J_PRIORITY;

    public AuditServiceAppender(MessageChannel auditEventChannel) {
        Assert.notNull(auditEventChannel);
        this.auditEventChannel = auditEventChannel;
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if (loggingEvent.getLevel().isGreaterOrEqual(this.auditServiceAppenderLog4jPriority)) {
            AuditEvent.Severity severity = log4jLevelToSeverity(loggingEvent.getLevel());
            if (severity != null) {
                log(loggingEvent.getLocationInformation(), loggingEvent.getTimeStamp(), getAuthentication(), severity, loggingEvent.getRenderedMessage(), loggingEvent.getThrowableInformation());
            }
        }
    }

    protected void log(LocationInfo locationInfo, long ts, Authentication auth, AuditEvent.Severity severity, String msg, ThrowableInformation ti) {
        MessageChannel messageChannel;

        synchronized (this) {
            messageChannel = this.auditEventChannel;
        }

        if (messageChannel != null) {
            Throwable t = null;
            if (ti != null) {
                t = ti.getThrowable();
            }

            String caller = locationInfo.fullInfo;

            GenericAuditEvent event = new GenericAuditEvent(caller, ts, auth, severity, msg, t);
            Message<GenericAuditEvent> eventMessage = new GenericMessage<GenericAuditEvent>(event);
            messageChannel.send(eventMessage);
        }
    }

    protected Authentication getAuthentication() {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        return securityContextHolder.getAuthentication();
    }

    protected AuditEvent.Severity log4jLevelToSeverity(Level level) {
        if (Level.TRACE.equals(level)) {
            return AuditEvent.Severity.TRACE;
        } else if (Level.DEBUG.equals(level)) {
            return AuditEvent.Severity.DEBUG;
        } else if (Level.INFO.equals(level)) {
            return AuditEvent.Severity.INFO;
        } else if (Level.WARN.equals(level)) {
            return AuditEvent.Severity.WARN;
        } else if (Level.ERROR.equals(level)) {
            return AuditEvent.Severity.CRITICAL;
        } else if (Level.FATAL.equals(level)) {
            return AuditEvent.Severity.CRITICAL;
        } else {
            return null;
        }
    }

    public synchronized void close() {
        this.auditEventChannel = null;
    }

    public boolean requiresLayout() {
        return false;
    }

    public void setAuditServiceAppenderLog4jPriority(Level auditServiceAppenderLog4jPriority) {
        this.auditServiceAppenderLog4jPriority = auditServiceAppenderLog4jPriority;
    }
}
