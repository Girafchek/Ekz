package trains.abstractions;


import trains.TrainType;

public abstract class Train implements Searchable {
    private static final int basicSpeed = 90;
    private String trainNumber;
    private TrainType type;
    private String route;
    private double routeLength;
    private int numberOfCars;
    private String driver;

    public static int getBasicSpeed() {
        return basicSpeed;
    }


    public String getTrainDetails() {
        return String.format("Номер поезда - %s\nТип поезда - %s\nМаршрут - %s\nДлина маршрута - %.1f км\nКоличество вагонов - %d\nМашинист - %s\nБазовая скорость - %d км/ч",
                trainNumber, type, route, routeLength, numberOfCars, driver, basicSpeed);
    }

    @Override
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) return false;
        String q = query.trim().toLowerCase();
        if (trainNumber != null && trainNumber.toLowerCase().contains(q)) return true;
        if (driver != null && driver.toLowerCase().contains(q)) return true;
        try {
            if (numberOfCars == Integer.parseInt(query.trim())) return true;
        } catch (NumberFormatException ignored) {}
        return false;
    }

    public abstract double calculateTime();

    public double getRouteLength() {
        return routeLength;
    }

    public void setRouteLength(double routeLength) {
        if (routeLength < 0) throw new IllegalArgumentException("Недопустимое значение");
        this.routeLength = routeLength;
    }

    public int getNumberOfCars() {
        return numberOfCars;
    }

    public void setNumberOfCars(int numberOfCars) {
        if (numberOfCars < 0) throw new IllegalArgumentException("Недопустимое значение");
        this.numberOfCars = numberOfCars;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        if (driver == null || driver.trim().isEmpty()) throw new IllegalArgumentException("Недопустимое значение");
        this.driver = driver;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        if (route == null || route.trim().isEmpty()) throw new IllegalArgumentException("Недопустимое значение");
        this.route = route;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        if (trainNumber == null || trainNumber.trim().isEmpty())
            throw new IllegalArgumentException("Недопустимое значение");
        this.trainNumber = trainNumber;
    }



    public void setType(TrainType type) {
        if (type == null) throw new IllegalArgumentException("Недопустимое значение");
        this.type = type;
    }
}
