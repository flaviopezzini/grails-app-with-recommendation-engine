package it.drivek.car

import grails.converters.JSON
import grails.web.JSONBuilder

class CarController {
    def carService

    protected static final String ID_REQUIRED = "Id parameter required";
    protected static final String CAR_NOT_FOUND = "Car not found";

    static allowedMethods = [makes: 'GET', search: 'GET', recommendations: 'GET'];

    def makes() {
        def makes = carService.getAllMakes()

        render makes as JSON
    }

    def search(String make, String model, String year) {
        def cars = carService.search(make, model, year);

        render cars as JSON
    }

    def recommendations() {
        if (!params.id) {
            return render(contentType:"application/json") {
                [error: ID_REQUIRED]
            }
        }
        Car car = Car.findById(params.id);
        if (car == null) {
            return render(contentType:"application/json") {
                [error: CAR_NOT_FOUND]
            }
        }
        def recommendations = carService.getRecommendations(car);
        render recommendations as JSON
    }
}