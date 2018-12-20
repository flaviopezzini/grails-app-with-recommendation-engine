package it.drivek.car

class InternalSpace {
    String description

    static hasMany = [cars: Car]
    static constraints = {
        description blank:false, nullable: false, maxSize: 40
    }
}