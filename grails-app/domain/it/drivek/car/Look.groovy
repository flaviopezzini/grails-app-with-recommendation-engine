package it.drivek.car

class Look {
    String description

    static hasMany = [cars: Car]
    static constraints = {
        description blank:false, nullable: false, maxSize: 40
    }

}
