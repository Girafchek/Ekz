package trains;

import trains.abstractions.Train;

public class PassengerTrain extends Train {
    private int passengers;

    @Override
    public double calculateTime() {
        return getRouteLength() / (getBasicSpeed() - (double) getPassengers() / 2);
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        if (passengers < 0) throw new IllegalArgumentException("Недопустимое значение");
        this.passengers = passengers;
    }

    @Override
    public String getTrainDetails() {
        return super.getTrainDetails() + "\nПассажиров - " + passengers + "\nВремя в пути - " + String.format("%.1f ч", calculateTime());
    }
}
