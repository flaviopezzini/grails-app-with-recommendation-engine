package it.drivek.car

class Model {
    String name

    static belongsTo = [make: Make]
    static hasMany = [cars: Car]
    static constraints = {
        name blank:false, nullable: false, maxSize: 40
        make nullable: false
    }
}
