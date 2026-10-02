package trains;

import trains.abstractions.Train;

public class CargoTrain extends Train {

    private double cargoWeight;

    @Override
    public double calculateTime() {
        return getRouteLength() / (getBasicSpeed() - getCargoWeight() / 10);
    }

    public double getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(double cargoWeight) {
        if (cargoWeight < 0) throw new IllegalArgumentException("Недопустимое значение");
        this.cargoWeight = cargoWeight;
    }

    @Override
    public String getTrainDetails() {
        return super.getTrainDetails() + "\nВес груза - " + cargoWeight + " т\nВремя в пути - " + String.format("%.1f ч", calculateTime());
    }
}
