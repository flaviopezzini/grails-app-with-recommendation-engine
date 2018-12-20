package it.drivek.car.load

import it.drivek.car.Car
import it.drivek.car.FuelType
import it.drivek.car.InternalSpace
import it.drivek.car.Look
import it.drivek.car.Make
import it.drivek.car.Model
import it.drivek.car.Segment

class CarImportService {
    def grailsApplication

    void initialize() throws Exception {
        log.info("CarImportService: Starting import...")
        def sourcePath = grailsApplication.config.cars.path
        CarImporter importer = new CarImporter(sourcePath)

        importer.importData();

        List<InternalSpace> allInternalSpaces = importer.getAllInternalSpaces()
        allInternalSpaces.each() { record ->
            record.save(failOnError: true)
        }
        List<Segment> allSegments = importer.getAllSegments()
        allSegments.each() { record ->
            record.save(failOnError:true)
        }
        List<FuelType> allFuelTypes = importer.getAllFuelTypes()
        allFuelTypes.each() { record ->
            record.save(failOnError:true)
        }
        List<Look> allLooks = importer.getAllLooks()
        allLooks.each() { record ->
            record.save(failOnError:true)
        }
        List<Make> allMakes = importer.getAllMakes()
        allMakes.each() { record ->
            record.save(failOnError:true)
        }
        List<Model> allModels = importer.getAllModels()
        allModels.each() { record ->
            record.save(failOnError:true)
        }
        List<Car> allCars = importer.getAllCars()
        allCars.each() { record ->
            record.save(failOnError: true);
        }
        log.info("CarImportService: Completed! ${allCars.size()} cars imported.");
    }
}