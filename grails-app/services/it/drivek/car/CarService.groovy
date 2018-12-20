package it.drivek.car

import grails.transaction.Transactional

@Transactional
class CarService {
    def sessionFactory

    List<Make> getAllMakes() {
        def list = Make.list();
        return list;
    }

    List<Car> search(String qMake, String qModel, String qYear) {
        def c = Car.createCriteria()
        def list = c.list{
            and {
                if (qMake) {
                    model {
                        make {
                            eq("name", qMake, [ignoreCase: true])
                        }
                    }
                }
                if (qModel) {
                    model {
                        eq("name", qModel, [ignoreCase: true])
                    }
                }
                if (qYear) {
                    eq("year", qYear)
                }
            }
        }

        return list;
    }

    List<Car> getRecommendations(Car car) {

        String query = "SELECT TOP 6 * FROM car where id <> :id ORDER BY ABS(:sourceRecommendationIndex - recommendation_Index)";
        final session = sessionFactory.currentSession
        final sqlQuery = session.createSQLQuery(query)

        // Use Groovy with() method to invoke multiple methods
        // on the sqlQuery object.
        final results = sqlQuery.with {
            addEntity(Car);
            setString('id', car.id);
            setLong("sourceRecommendationIndex", car.recommendationIndex);
            list()
        }

        return results
    }
}