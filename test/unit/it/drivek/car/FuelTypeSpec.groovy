package it.drivek.car

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FuelType)
@Mock(FuelType)
class FuelTypeSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test empty data validation"() {
        when:
        FuelType fuelType = new FuelType();
        fuelType.save()

        then:
        fuelType.hasErrors()
        fuelType.errors.getFieldError('description')
        FuelType.count() == 0
    }

    void "test max size validation"() {
        when:
        FuelType fuelType = new FuelType(description: RandomStringUtils.randomAlphanumeric(41));
        fuelType.save()

        then:
        fuelType.hasErrors()
        fuelType.errors.getFieldError('description')
        FuelType.count() == 0
    }

    void "test save success"() {
        when:
        FuelType fuelType = new FuelType(description: "fuelType1");
        fuelType.id = 123L
        fuelType.save()

        then:
        !fuelType.hasErrors()
        FuelType.count() == 1
    }
}