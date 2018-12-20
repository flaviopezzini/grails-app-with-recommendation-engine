package it.drivek.car.load

import grails.converters.JSON
import it.drivek.car.Car
import it.drivek.car.FuelType
import it.drivek.car.InternalSpace
import it.drivek.car.Look
import it.drivek.car.Make
import it.drivek.car.Model
import it.drivek.car.Segment
import org.apache.commons.text.similarity.LevenshteinDistance
import org.codehaus.groovy.grails.web.json.JSONElement

public class CarImporter {
    private static final String[] validInternalSpaceValues = ["For Singles", "2-4 people", "4 people comfort", "5 people comfort", "7 seats", "> 7 seats"];
    private static final String[] validSegmentValues = ["citycar", "utilitarian", "compact", "crossover", "berlina-2v", "berlina-3v", "station",
                                                        "monovolume", "suv", "supercar"];
    private static final String[] validFuelTypeValues = ["gasoline", "gpl", "gas", "hybrid", "electrical", "diesel"];
    private static final String[] validLookValues = ["classic", "modern", "sport", "original"];
    public static final int MAX_RECOMMENDATIONS = 6;

    private final File sourceFile;

    private List<Car> allCars;
    private List<Make> allMakes;
    private List<Model> allModels;
    private List<InternalSpace> allInternalSpaces;
    private List<Segment> allSegments;

    private List<FuelType> allFuelTypes;
    private List<Look> allLooks;

    public CarImporter(String sourcePath) {
        this.sourceFile = new File(sourcePath)
    }

    public List<Car> getAllCars()  {
        return allCars
    }

    public List<Make> getAllMakes() {
        return allMakes
    }

    public List<Model> getAllModels() {
        return allModels
    }

    public List<InternalSpace> getAllInternalSpaces() {
        return allInternalSpaces;
    }

    public List<Segment> getAllSegments() {
        return allSegments;
    }

    List<FuelType> getAllFuelTypes() {
        return allFuelTypes
    }

    List<Look> getAllLooks() {
        return allLooks
    }

    /**
     * Retrive a map with all the possible internal space values
     * @return
     */
    private Map<String, InternalSpace> getInternalSpaceMap() {
        Map<String, InternalSpace> retMap = new HashMap<>();
        validInternalSpaceValues.each() { currentDescription ->
            InternalSpace internalSpace = new InternalSpace(description: currentDescription);
            retMap.put(currentDescription, internalSpace);
            allInternalSpaces.add(internalSpace);
        }
        return retMap;
    }

    /**
     * Retrive a map with all the possible segments
     * @return
     */
    private Map<String, Segment> getSegmentMap() {
        Map<String, Segment> retMap = new HashMap<>();
        validSegmentValues.each() { currentDescription ->
            Segment segment = new Segment(description: currentDescription);
            retMap.put(currentDescription, segment);
            allSegments.add(segment);
        }
        return retMap;
    }

    /**
     * Retrive a map with all the possible fuel types
     * @return
     */
    private Map<String, FuelType> getFuelTypeMap() {
        Map<String, FuelType> retMap = new HashMap<>();
        validFuelTypeValues.each() { currentDescription ->
            FuelType fuelType = new FuelType(description: currentDescription);
            retMap.put(currentDescription, fuelType);
            allFuelTypes.add(fuelType);
        }
        return retMap;
    }

    /**
     * Retrive a map with all the possible looks
     * @return
     */
    private Map<String, Look> getLookMap() {
        Map<String, Look> retMap = new HashMap<>();
        validLookValues.each() { currentDescription ->
            Look look = new Look(description: currentDescription);
            retMap.put(currentDescription, look);
            allLooks.add(look);
        }
        return retMap;
    }

    /**
     * Import the data into the various data domains
     */
    public void importData() {
        String content = sourceFile.getText('UTF-8');
        allCars = new ArrayList<>();

        JSONElement jsonContent = JSON.parse(content);

        allMakes = new ArrayList<>();
        allModels = new ArrayList<>();
        allInternalSpaces = new ArrayList<>();
        allSegments = new ArrayList<>();
        allFuelTypes = new ArrayList<>();
        allLooks = new ArrayList<>();

        Map<Car, Set<String>> recommendationMap = new HashMap<>();

        // keep the unique make names
        Map<String, Make> makeMap = new HashMap<>();
        Map<String, Model> modelMap = new HashMap<>();
        Map<String, InternalSpace> internalSpaceMap = getInternalSpaceMap();
        Map<String, Look> lookMap = getLookMap()
        Map<String, FuelType> fuelTypeMap = getFuelTypeMap()
        Map<String, Segment> segmentMap = getSegmentMap()

        jsonContent.each {obj ->
            // load the make
            String make = obj.attrs.make;
            Make carMake = makeMap.get(make);
            if (carMake == null) {
                carMake = new Make(name: make);
                makeMap.put(make, carMake);
                allMakes.add(carMake);
            }
            // load the model
            String model = obj.attrs.model;
            String modelKey = make + "-" + model;
            Model carModel = modelMap.get(modelKey)
            if (carModel == null) {
                carModel = new Model(name: model, make: carMake);
                modelMap.put(modelKey, carModel);
                allModels.add(carModel);
            }
            // load internal space
            String internalSpace = obj.tags."Internal space";
            InternalSpace carInternalSpace = internalSpaceMap.get(internalSpace);
            if (carInternalSpace == null) {
                carInternalSpace = new InternalSpace(description: internalSpace);
                internalSpaceMap.put(internalSpace, carInternalSpace);
            }
            // load look
            String look = obj.tags.Look;
            Look carLook = lookMap.get(look);
            if (carLook == null) {
                carLook = new Look(description: internalSpace);
                lookMap.put(look, carLook);
            }
            // load fuel type
            String fuelType = obj.tags."Fuel type";
            FuelType carFuelType = fuelTypeMap.get(fuelType);
            if (carFuelType == null) {
                carFuelType = new FuelType(description: fuelType);
                fuelTypeMap.put(fuelType, carFuelType);
            }
            // load segment
            String segment = obj.tags.Segment;
            Segment carSegment = segmentMap.get(segment);
            if (carSegment == null) {
                carSegment = new Segment(description: segment);
                segmentMap.put(segment, carSegment);
            }
            String price = obj.tags.Price;
            // strip away unnecessary info
            price = price.replace("% percentile", "");

            Car newCar = new Car(
                    model: carModel,
                    submodel: obj.attrs.submodel,
                    year: obj.attrs.year,
                    img: obj.attrs.img,
                    internalSpace: carInternalSpace,
                    segment: carSegment,
                    fuelType: carFuelType,
                    look: carLook,
                    price: price,
                    recommendations: new ArrayList<>()
            );
            newCar.id = obj.attrs.carId;
            //
            allCars.add(newCar);
        }
    }
}