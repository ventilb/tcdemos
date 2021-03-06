package de.iew.web.controllers;

import de.iew.framework.domain.audit.AuditEventMessage;
import de.iew.framework.domain.utils.AbstractDomainModelVisitor;
import de.iew.framework.domain.utils.CollectionHolder;
import de.iew.services.AuditService;
import de.iew.web.forms.AuditEventForm;
import de.iew.web.isc.DSRequest;
import de.iew.web.isc.DSResponseCollection;
import de.iew.web.isc.annotations.IscRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Implements a controller to manage the audit event messages.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 01.02.13 - 23:01
 */
@Controller
@RequestMapping(value = "/audit")
public class AuditController {

    private static final Log log = LogFactory.getLog(AuditController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String indexAction() {
        return "audit";
    }

    @RequestMapping(value = "/fetch.json", method = RequestMethod.GET)
    @ResponseBody
    public Model fetchAction(
            @IscRequest DSRequest dsRequest
    ) throws Exception {
        CollectionHolder<AuditEventMessage> auditEventMessages = this.auditService.getAuditEventMessages(dsRequest.getStartRow(), dsRequest.getRowCount());

        AuditEventMessageTransformer auditEventMessageTransformer = new AuditEventMessageTransformer();

        CollectionHolder<AuditEventForm> auditEventForms = new CollectionHolder<AuditEventForm>(auditEventMessageTransformer.visitCollection(auditEventMessages), auditEventMessages.getFirstItem(), auditEventMessages.getTotalCount());

        return new DSResponseCollection(auditEventForms);
    }

    @ExceptionHandler
    public Model onException(Exception e) {
        if (log.isErrorEnabled()) {
            log.error("Fehler während AuditController Verarbeitung", e);
        }
        return null;
    }

    // Service und Dao Abhängigkeiten /////////////////////////////////////////

    private AuditService auditService;

    @Autowired
    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    protected static class AuditEventMessageTransformer extends AbstractDomainModelVisitor<AuditEventMessage, AuditEventForm> {

        public AuditEventForm visit(AuditEventMessage domainModel) {
            AuditEventForm auditEventForm = new AuditEventForm();
            auditEventForm.setId(domainModel.getId());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(domainModel.getTimestamp());

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            auditEventForm.setTimestamp(sdf.format(domainModel.getTimestamp()));
            auditEventForm.setPrincipal(domainModel.getPrincipal());
            auditEventForm.setSeverity(domainModel.getSeverity().name());
            auditEventForm.setMessage(domainModel.getMessage());
            return auditEventForm;
        }
    }
}
