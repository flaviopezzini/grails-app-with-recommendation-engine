package it.drivek.car

class Make {
    String name;
    static hasMany = [models: Model]

    static constraints = {
        name blank:false, maxSize: 40
    }

}
