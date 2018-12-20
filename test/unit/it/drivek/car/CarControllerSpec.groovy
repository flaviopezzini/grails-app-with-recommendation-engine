package it.drivek.car

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import it.drivek.utils.MarshallingSettings
import spock.lang.Specification

import static javax.servlet.http.HttpServletResponse.SC_OK
import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(CarController)
@Mock([Car, CarService])
class CarControllerSpec extends Specification {

    def setupSpec() {
        MarshallingSettings.setUp();
    }

    def setup() {
        controller.carService = CarService
    }

    def cleanup() {
    }

    void "test makes invalid method"() {
        when:
        request.method = "POST"
        controller.makes()

        then:
        response.status == SC_METHOD_NOT_ALLOWED
    }

    void "test makes success"() {
        given:
        List<Make> mockMakesList = new ArrayList<>();
        Make mockMake1 = new Make(name: "make1");
        Make mockMake2 = new Make(name: "make2");
        mockMakesList.add(mockMake1);
        mockMakesList.add(mockMake2);

        CarService.metaClass.static.getAllMakes = { -> mockMakesList}

        when:
        request.method = "GET"
        controller.makes()

        then:
        response.status == SC_OK
        response.json.size() == 2
        response.json[0].name == "make1"
        response.json[1].name == "make2"
    }

    void "test search invalid method"() {
        when:
        request.method = "POST"
        controller.search("", "", "")

        then:
        response.status == SC_METHOD_NOT_ALLOWED
    }

    void "test search success"() {
        when:
        Car result1 = new Car(model: new Model(name: "model1", make: new Make(name: "make1")));
        Car result2 = new Car(model: new Model(name: "model2", make: new Make(name: "make2")));
        List<Car> mockSearchList = new ArrayList<>();
        mockSearchList.add(result1);
        mockSearchList.add(result2);
        CarService.metaClass.static.search = { String make, String model, String year -> mockSearchList }
        request.method = "GET"
        controller.search("", "", "")

        then:
        response.status == SC_OK
        response.json.size() == 2
        response.json[0].make.name == "make1"
        response.json[1].make.name == "make2"
    }

    void "test recommendations invalid method"() {
        when:
        request.method = "POST"
        controller.recommendations()

        then:
        response.status == SC_METHOD_NOT_ALLOWED
    }

    void "test recommendations missing id"() {
        when:
        request.method = "GET"
        params.id = ""
        controller.recommendations()

        then:
        response.status == SC_OK
        def expected = ([error: CarController.ID_REQUIRED]) as JSON;
        response.json.toString() == expected.toString();
    }

    void "test recommendations car not found"() {
        when:
        //
        Car.metaClass.static.findById = { String id -> null }
        params.id = "123"
        request.method = "GET"
        controller.recommendations()

        then:
        response.status == SC_OK
        def expected = ([error: CarController.CAR_NOT_FOUND]) as JSON;
        response.json.toString() == expected.toString();
    }

    void "test recommendations success"() {
        when:
        //
        Car recommendation1 = new Car(model: new Model(name: "model1", make: new Make(name: "make1")));
        Car recommendation2 = new Car(model: new Model(name: "model2", make: new Make(name: "make2")));
        ArrayList<Car> mockRecommendationList = new ArrayList<>();
        mockRecommendationList.add(recommendation1);
        mockRecommendationList.add(recommendation2);
        CarService.metaClass.static.getRecommendations = {Car car -> mockRecommendationList}
        Car.metaClass.static.findById = { String id -> new Car() }
        params.id = "123"
        request.method = "GET"
        controller.recommendations()

        then:
        response.status == SC_OK
        response.json.size() == 2
        response.json[0].make.name == "make1"
        response.json[1].make.name == "make2"
    }
}