package it.drivek.car

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Model)
@Mock(Model)
class ModelSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test empty data validation"() {
        when:
        Model model = new Model();
        model.save()

        then:
        model.hasErrors()
        model.errors.getFieldError('name')
        Model.count() == 0
    }

    void "test max size validation"() {
        when:
        Model model = new Model(name: RandomStringUtils.randomAlphanumeric(41));
        model.save()

        then:
        model.hasErrors()
        model.errors.getFieldError('name')
        Model.count() == 0
    }

    void "test save success"() {
        when:
        Model model = new Model(name: "model1", make: new Make(name: "make1"));
        model.id = 123L
        model.save()

        then:
        !model.hasErrors()
        Model.count() == 1
    }
}