package it.drivek.lead

import grails.converters.JSON
import it.drivek.car.Car
import org.springframework.web.servlet.support.RequestContextUtils

class LeadController {

    def leadService

    def messageSource

    protected static final String INVALID_PARAMETERS = "Invalid parameters"
    protected static final String CAR_NOT_FOUND = "Car not found"
    protected static final String LEAD_NOT_FOUND = "Lead not found"
    protected static final String INVALID_STATUS_VALUE = "Invalid status value"
    protected static final String INVALID_STATUS_CHANGE = "Invalid status change"
    protected static final String SOLD_DATE_REQUIRED = "Sold date required when changing lead status to sold";

    private static final HashSet<String> VALID_STATUS_CHANGES;

    static allowedMethods = [create: 'POST', updateStatus: 'PUT'];

    static {
        VALID_STATUS_CHANGES = new HashSet<>();
        VALID_STATUS_CHANGES.add(LeadStatus.CREATED.toString() + LeadStatus.VALID.toString());
        VALID_STATUS_CHANGES.add(LeadStatus.CREATED.toString() + LeadStatus.CANCELED.toString());
        VALID_STATUS_CHANGES.add(LeadStatus.CREATED.toString() + LeadStatus.INVALID.toString());
        VALID_STATUS_CHANGES.add(LeadStatus.VALID.toString() + LeadStatus.SOLD.toString());
        VALID_STATUS_CHANGES.add(LeadStatus.VALID.toString() + LeadStatus.CLOSED.toString());
    }

    def create() {
        Car car = Car.findById(request.JSON.carId);
        if (car == null) {
            return render(contentType:"application/json") {
                [error: CAR_NOT_FOUND]
            }
        }
        Lead newLead = new Lead(
          car: car,
          name: request.JSON.name,
          surname: request.JSON.surname,
          phone: request.JSON.phone,
          status: LeadStatus.CREATED,
          soldDate: null
        );
        newLead.save(failOnError: false);
        if (newLead.hasErrors()) {
            def errorMessages = newLead.errors.allErrors.collect{messageSource.getMessage(it, RequestContextUtils.getLocale(request))}
            return render(contentType:"application/json") {
                [errors: errorMessages]
            }
        }
        render newLead as JSON;
    }

    def updateStatus() {
        if (!params.id || !params.status) {
            return render(contentType:"application/json") {
                [error: INVALID_PARAMETERS]
            }
        }
        String statusParam = params.status.toUpperCase();
        LeadStatus newStatus = null;
        try {
            newStatus = statusParam as LeadStatus;
        } catch(IllegalArgumentException iae) {
            return render(contentType:"application/json") {
                [error: INVALID_STATUS_VALUE]
            }
        }
        Lead toUpdate = Lead.findById(params.id);
        if (toUpdate == null) {
            return render(contentType:"application/json") {
                [error: LEAD_NOT_FOUND]
            }
        }
        String statusChange = toUpdate.status.toString() + statusParam;
        if (!VALID_STATUS_CHANGES.contains(statusChange)) {
            return render(contentType:"application/json") {
                [error: INVALID_STATUS_CHANGE]
            }
        }
        if (newStatus == LeadStatus.SOLD) {
            if (!params.soldDate) {
                return render(contentType:"application/json") {
                    [error: SOLD_DATE_REQUIRED]
                }
            }
            Date soldDate = Date.parse('yyyyMMdd', params.soldDate);
            toUpdate.soldDate = soldDate;
        }
        toUpdate.setStatus(newStatus);
        Lead updated = leadService.update(toUpdate);
        if (updated.hasErrors()) {
            def errorMessages = updated.errors.allErrors.collect{messageSource.getMessage(it, RequestContextUtils.getLocale(request))}
            return render(contentType:"application/json") {
                [errors: errorMessages]
            }
        }
        render updated as JSON;
    }
}