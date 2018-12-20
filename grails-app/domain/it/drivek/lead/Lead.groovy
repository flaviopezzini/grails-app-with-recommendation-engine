package it.drivek.lead

import it.drivek.car.Car

class Lead {
    Car car
    String name
    String surname
    String phone
    LeadStatus status
    Date soldDate

    static constraints = {
        car nullable: false
        name blank:false, nullable: false, maxSize: 100
        surname blank: false, nullable: false, maxSize: 100
        phone blank: false, nullable: false, maxSize: 25
        status nullable: false
        soldDate nullable: true
    }
}