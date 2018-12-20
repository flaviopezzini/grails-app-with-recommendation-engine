package it.drivek.car

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Car)
@Mock(Car)
class CarSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test empty data validation"() {
        when:
        Car car = new Car(year: '',model: null,internalSpace: null,segment: null,fuelType: null,look: null);
        car.save()

        then:
        car.hasErrors()
        car.errors.getFieldError('year')
        car.errors.getFieldError('model')
        car.errors.getFieldError('internalSpace')
        car.errors.getFieldError('segment')
        car.errors.getFieldError('fuelType')
        car.errors.getFieldError('look')
        Car.count() == 0
    }

    void "test size and range validation"() {
        when:
        Car car = new Car(
                year: "12345",
                model: new Model(),
                internalSpace: new InternalSpace(),
                segment: new Segment(),
                fuelType: new FuelType(),
                look: new Look(),
                img: RandomStringUtils.randomAlphanumeric(201),
                price: 101,
                submodel: RandomStringUtils.randomAlphanumeric(101));
        car.id = "id";
        car.save()

        then:
        car.hasErrors()
        car.errors.getFieldError('img')
        car.errors.getFieldError('price')
        car.errors.getFieldError('submodel')
        Car.count() == 0
    }

    void "test type validation"() {
        when:
        Car car = new Car(
                year: "abcd",
                model: new Model(),
                internalSpace: new InternalSpace(),
                segment: new Segment(),
                fuelType: new FuelType(),
                look: new Look(),
                img: "img",
                price: "xpto",
                submodel: "submodel");
        car.id = "id"
        car.save()

        then:
        car.hasErrors()
        car.errors.getFieldError('year')
        car.errors.getFieldError('price')
        Car.count() == 0
    }

    void "test save success"() {
        when:
        Car car = new Car(
                year: 2015,
                model: new Model(),
                internalSpace: new InternalSpace(),
                segment: new Segment(),
                fuelType: new FuelType(),
                look: new Look(),
                img: "img",
                price: 97,
                submodel: "submodel");
        car.id = "id"
        car.save()

        then:
        !car.hasErrors()
        Car.count() == 1
    }

    void "test calculate recommendation index"() {
        when:
        Car renaultTwizy = createCar("2015", "For Singles", "citycar", "electrical", "modern", "1");
        Car opelAdam = createCar("2015", "For Singles", "citycar", "gasoline", "original", "13");
        Car hyundayI30 = createCar("2015", "5 people comfort", "station", "diesel", "modern", "26");
        Car opelInsignia = createCar("2015", "5 people comfort", "station", "gasoline", "modern", "53");
        Car renaultScenic = createCar("2015", "4 people comfort", "monovolume", "gasoline", "modern", "34");

        then:
        // the small cars should be more similar
        calculateDifferenceBetweenRecommendationIndexes(renaultTwizy, opelAdam) <
                calculateDifferenceBetweenRecommendationIndexes(renaultTwizy, hyundayI30);
        // the bigger cars should be more similar
        calculateDifferenceBetweenRecommendationIndexes(hyundayI30, opelInsignia) <
                calculateDifferenceBetweenRecommendationIndexes(hyundayI30, renaultTwizy);
        calculateDifferenceBetweenRecommendationIndexes(hyundayI30, renaultScenic) <
                calculateDifferenceBetweenRecommendationIndexes(hyundayI30, renaultTwizy);
    }

    private int calculateDifferenceBetweenRecommendationIndexes(Car c1, Car c2) {
        return Math.abs(c1.recommendationIndex - c2.recommendationIndex);
    }

    private createCar(String year, String internalSpace, String segment, String fuelType, String look, String price) {
        Car car = new Car(
                year: year,
                model: new Model(),
                internalSpace: new InternalSpace(description: internalSpace),
                segment: new Segment(description: segment),
                fuelType: new FuelType(description: fuelType),
                look: new Look(description: look),
                img: "img",
                price: price,
                submodel: "submodel");
        car.id = "id";
        // calculate recommendation index
        car.beforeInsert();
        return car;
    }
}