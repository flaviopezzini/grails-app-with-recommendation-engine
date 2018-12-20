package it.drivek.lead

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import it.drivek.car.Car
import it.drivek.car.Make
import it.drivek.car.Model
import org.springframework.context.MessageSource
import spock.lang.Specification
import static javax.servlet.http.HttpServletResponse.SC_OK
import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED
import it.drivek.utils.MarshallingSettings

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(LeadController)
@Mock([Car, LeadService, Lead])
class LeadControllerSpec extends Specification {

    def setupSpec() {
        MarshallingSettings.setUp();
    }

    def setup() {
        controller.leadService = LeadService
    }

    def cleanup() {
    }

    void "test create invalid method"() {
        when:
        request.method = "GET"
        controller.create()

        then:
        response.status == SC_METHOD_NOT_ALLOWED
    }

    void "test create car not found"() {
        when:
        request.JSON.carId = ""
        request.method = "POST"
        controller.create()

        then:
        response.status == SC_OK
        def expected = ([error: LeadController.CAR_NOT_FOUND]) as JSON;
        response.json.toString() == expected.toString();
    }

    void "test create invalid parameters"() {
        when:
        Car mockCar = new Car();
        Car.metaClass.static.findById = { String id -> mockCar }
        request.JSON.carId = "123"
        request.JSON.name = ""
        request.JSON.surname = ""
        request.JSON.phone = ""
        request.method = "POST"
        controller.create()

        then:
        response.status == SC_OK
        response.json.errors.size() == 3
    }

    void "test create success"() {
        when:
        Car mockCar = new Car();
        mockCar.model = new Model(name:"Kangoo");
        mockCar.model.make = new Make(name: "Renault")
        Car.metaClass.static.findById = { String id -> mockCar }
        request.JSON.carId = "123"
        request.JSON.name = "name"
        request.JSON.surname = "surname"
        request.JSON.phone = "phone"
        request.method = "POST"
        controller.create()

        then:
        response.status == SC_OK
        response.json.car.make == "Renault"
        response.json.car.model == "Kangoo"
        response.json.name == "name"
        response.json.surname == "surname"
        response.json.phone == "phone"
    }

    void "test updateStatus invalid method"() {
        when:
        request.method = "GET"
        controller.updateStatus()

        then:
        response.status == SC_METHOD_NOT_ALLOWED
    }

    void "test updateStatus invalid parameters"() {
        when:
        params.id = ""
        params.status = ""
        request.method = "PUT"
        controller.updateStatus()
        then:
        response.status == SC_OK
        def expected = ([error: LeadController.INVALID_PARAMETERS]) as JSON;
        response.json.toString() == expected.toString();
    }

    void "test updateStatus lead not found"() {
        when:
        params.id = "123"
        params.status = "valid"
        request.method = "PUT"
        controller.updateStatus()
        then:
        response.status == SC_OK
        def expected = ([error: LeadController.LEAD_NOT_FOUND]) as JSON;
        response.json.toString() == expected.toString();
    }

    void "test updateStatus invalid status value"() {
        when:
        params.id = "123"
        params.status = "thisisinvalidstatus"
        request.method = "PUT"
        controller.updateStatus()
        then:
        response.status == SC_OK
        def expected = ([error: LeadController.INVALID_STATUS_VALUE]) as JSON;
        response.json.toString() == expected.toString();
    }

    void "test updateStatus invalid status change"() {
        when:
        params.id = "123"
        params.status = "valid"
        Lead mockLead = new Lead(status: "CANCELED" as LeadStatus)
        Lead.metaClass.static.findById = { String id -> mockLead }
        request.method = "PUT"
        controller.updateStatus()
        then:
        response.status == SC_OK
        def expected = ([error: LeadController.INVALID_STATUS_CHANGE]) as JSON;
        response.json.toString() == expected.toString();
    }

    void "test updateStatus to sold no date"() {
        when:
        params.id = "123"
        params.status = "sold"
        params.soldDate = null
        Lead mockLead = new Lead(status: "VALID" as LeadStatus)
        Lead.metaClass.static.findById = { String id -> mockLead }
        request.method = "PUT"
        controller.updateStatus()

        then:
        response.status == SC_OK
        def expected = ([error: LeadController.SOLD_DATE_REQUIRED]) as JSON;
        response.json.toString() == expected.toString();
    }

    void "test updateStatus success"() {
        when:
        params.id = "123"
        params.status = "sold"
        Date today = new Date()
        params.soldDate = (today + 1).format("yyyyMMdd")
        //
        Car mockCar = new Car();
        mockCar.model = new Model(name:"Kangoo", make: new Make(name: "Renault"));
        Lead mockLead = new Lead(status: "VALID" as LeadStatus, car: mockCar)
        Lead.metaClass.static.findById = { String id -> mockLead }
        LeadService.metaClass.static.update = { Lead lead -> lead }
        //
        request.method = "PUT"
        controller.updateStatus()

        then:
        response.status == SC_OK
        response.json.status.name == "SOLD"
        response.json.soldDate == params.soldDate
    }

}
