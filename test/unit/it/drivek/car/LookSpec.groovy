package it.drivek.car

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Look)
@Mock(Look)
class LookSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test empty data validation"() {
        when:
        Look look = new Look();
        look.save()

        then:
        look.hasErrors()
        look.errors.getFieldError('description')
        Look.count() == 0
    }

    void "test max size validation"() {
        when:
        Look look = new Look(description: RandomStringUtils.randomAlphanumeric(41));
        look.save()

        then:
        look.hasErrors()
        look.errors.getFieldError('description')
        Look.count() == 0
    }

    void "test save success"() {
        when:
        Look look = new Look(description: "look1");
        look.id = 123L
        look.save()

        then:
        !look.hasErrors()
        Look.count() == 1
    }
}