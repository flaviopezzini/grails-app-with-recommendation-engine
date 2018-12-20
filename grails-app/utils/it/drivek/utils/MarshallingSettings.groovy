package it.drivek.utils

import grails.converters.JSON
import it.drivek.car.Car
import it.drivek.car.FuelType
import it.drivek.car.InternalSpace
import it.drivek.car.Look
import it.drivek.car.Make
import it.drivek.car.Model
import it.drivek.car.Segment
import it.drivek.lead.Lead
import it.drivek.lead.LeadStatus

/**
 * Sets up the marshalling settings for all domain classes
 */
class MarshallingSettings {

    public static void setUp() {
        JSON.registerObjectMarshaller(Model) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['name'] = it.name
            return returnArray
        }
        JSON.registerObjectMarshaller(Make) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['name'] = it.name
            return returnArray
        }
        JSON.registerObjectMarshaller(InternalSpace) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['description'] = it.description
            return returnArray
        }
        JSON.registerObjectMarshaller(Segment) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['description'] = it.description
            return returnArray
        }
        JSON.registerObjectMarshaller(Look) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['description'] = it.description
            return returnArray
        }
        JSON.registerObjectMarshaller(FuelType) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['description'] = it.description
            return returnArray
        }
        JSON.registerObjectMarshaller(Car) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['make'] = it.model.make
            returnArray['model'] = it.model
            returnArray['submodel'] = it.submodel
            returnArray['year'] = it.year
            returnArray['img'] = it.img
            returnArray['internalSpace'] = it.internalSpace
            returnArray['segment'] = it.segment
            returnArray['fuelType'] = it.fuelType
            returnArray['look'] = it.look
            returnArray['price'] = it.price

            return returnArray
        }

        JSON.registerObjectMarshaller(Lead) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['car'] = [make: it.car.model.make.name, model: it.car.model.name]
            returnArray['name'] = it.name
            returnArray['surname'] = it.surname
            returnArray['phone'] = it.phone
            returnArray['status'] = [name: it.status.toString()]
            if (it.status == LeadStatus.SOLD) {
                returnArray['soldDate'] = it.soldDate.format("yyyyMMdd")
            }
            return returnArray
        }
    }
}
