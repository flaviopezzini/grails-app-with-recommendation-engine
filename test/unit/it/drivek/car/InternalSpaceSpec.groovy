package it.drivek.car

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(InternalSpace)
@Mock(InternalSpace)
class InternalSpaceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test empty data validation"() {
        when:
        InternalSpace internalSpace = new InternalSpace();
        internalSpace.save()

        then:
        internalSpace.hasErrors()
        internalSpace.errors.getFieldError('description')
        InternalSpace.count() == 0
    }

    void "test max size validation"() {
        when:
        InternalSpace internalSpace = new InternalSpace(description: RandomStringUtils.randomAlphanumeric(41));
        internalSpace.save()

        then:
        internalSpace.hasErrors()
        internalSpace.errors.getFieldError('description')
        InternalSpace.count() == 0
    }

    void "test save success"() {
        when:
        InternalSpace internalSpace = new InternalSpace(description: "internalSpace1");
        internalSpace.id = 123L
        internalSpace.save()

        then:
        !internalSpace.hasErrors()
        InternalSpace.count() == 1
    }
}