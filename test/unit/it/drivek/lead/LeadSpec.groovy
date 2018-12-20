package it.drivek.lead

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import it.drivek.car.Car
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Lead)
@Mock(Lead)
class LeadSpec extends Specification {
    def setup() {
    }

    def cleanup() {
    }

    void "test empty data validation"() {
        when:
        Lead lead = new Lead();
        lead.save()

        then:
        lead.hasErrors()
        lead.errors.getFieldError('name')
        lead.errors.getFieldError('surname')
        lead.errors.getFieldError('phone')
        lead.errors.getFieldError('status')
        Lead.count() == 0
    }

    void "test max size validation"() {
        when:
        Lead lead = new Lead(
            name: RandomStringUtils.randomAlphanumeric(101),
            surname: RandomStringUtils.randomAlphanumeric(101),
            phone: RandomStringUtils.randomAlphanumeric(26)
        );
        lead.save()

        then:
        lead.hasErrors()
        lead.errors.getFieldError('name')
        lead.errors.getFieldError('surname')
        lead.errors.getFieldError('phone')
        Lead.count() == 0
    }

    void "test save success"() {
        when:
        Date today = new Date()
        Date soldDate = (today + 1)

        Lead lead = new Lead(
                name: "name",
                surname: "surname",
                phone: "phone",
                car: new Car(),
                status: LeadStatus.CREATED,
                soldDate: soldDate);
        lead.save()

        then:
        !lead.hasErrors()
        Lead.count() == 1
    }
}