package it.drivek.car

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Make)
@Mock(Make)
class MakeSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test empty data validation"() {
        when:
        Make make = new Make();
        make.save()

        then:
        make.hasErrors()
        make.errors.getFieldError('name')
        Make.count() == 0
    }

    void "test max size validation"() {
        when:
        Make make = new Make(name: RandomStringUtils.randomAlphanumeric(41));
        make.save()

        then:
        make.hasErrors()
        make.errors.getFieldError('name')
        Make.count() == 0
    }

    void "test save success"() {
        when:
        Make make = new Make(name: "make1");
        make.id = 123L
        make.save()

        then:
        !make.hasErrors()
        Make.count() == 1
    }
}