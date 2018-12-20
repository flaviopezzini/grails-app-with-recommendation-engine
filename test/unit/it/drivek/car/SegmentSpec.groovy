package it.drivek.car

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Segment)
@Mock(Segment)
class SegmentSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test empty data validation"() {
        when:
        Segment segment = new Segment();
        segment.save()

        then:
        segment.hasErrors()
        segment.errors.getFieldError('description')
        Segment.count() == 0
    }

    void "test max size validation"() {
        when:
        Segment segment = new Segment(description: RandomStringUtils.randomAlphanumeric(41));
        segment.save()

        then:
        segment.hasErrors()
        segment.errors.getFieldError('description')
        Segment.count() == 0
    }

    void "test save success"() {
        when:
        Segment segment = new Segment(description: "segment1");
        segment.id = 123L
        segment.save()

        then:
        !segment.hasErrors()
        Segment.count() == 1
    }
}