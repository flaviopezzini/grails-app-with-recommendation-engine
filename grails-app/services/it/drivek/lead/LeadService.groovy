package it.drivek.lead

import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
class LeadService {

    public Lead update(Lead lead) {
        Lead dbLead = Lead.findById(lead.id.toString());
        InvokerHelper.setProperties(dbLead, lead.properties)
        dbLead.save(flush: true);
        return dbLead;
    }
}