package it.drivek.car

import org.apache.commons.text.similarity.LevenshteinDistance

class Car {

    // algorithm to calculate string similarity
    static LevenshteinDistance ld = new LevenshteinDistance();
    // the comparison text contains the possible values for the tags being compared
    private static final String MASTER_COMPARISON_TEXT = "For Singles, 2­4 people, 4 people comfort, 5 people confort, 7 seats, > 7 seats" +
            "citycar, utilitarian, compact, crossover, berlina­2v, berlina­3v, station, monovolume, suv, supercar" +
            "gasoline, gpl, gas, hybrid, electrical, diesel" +
            "classic, modern, sport, original";

    static belongsTo = [
            model: Model,
            internalSpace: InternalSpace,
            segment: Segment,
            fuelType: FuelType,
            look: Look]

    static mapping = {
        id generator:'assigned'
    }

    String id
    String submodel
    int year
    String img
    int price
    int recommendationIndex

    static constraints = {
        id blank: false, nullable: false
        year blank: false, nullable: false
        model nullable: false
        internalSpace nullable: false
        segment nullable: false
        fuelType nullable: false
        look nullable: false
        img blank: false, nullable: false, maxSize: 200
        price blank: false, nullable: false, range: 1..100
        submodel blank: false, nullable: false, maxSize: 100
        recommendationIndex blank: false, nullable: false
    }

    def beforeInsert() {
        this.beforeUpdate()
    }
    def beforeUpdate() {
        this.recommendationIndex = calculateRecommendationIndex();
    }

    /**
     * Calculate how similar this car is to others
     * based on its tags. Cars with the most similar tags
     * should also have a very similar recommendation indexa
     * @return
     */
    protected int calculateRecommendationIndex() {
        def recommendationLine = StringBuilder.newInstance()
        recommendationLine << this.year;
        recommendationLine << this.price;
        // intentionally doubling this variable as it has a heavier weight
        recommendationLine << this.internalSpace.description;
        recommendationLine << this.internalSpace.description;
        recommendationLine << this.segment.description;
        recommendationLine << this.fuelType.description;
        recommendationLine << this.look.description;
        // this should give how similar this car is in comparison with the other cars
        // we'll use the recommendation index to spot similarity between cars
        return ld.apply(recommendationLine, MASTER_COMPARISON_TEXT);
    }
}