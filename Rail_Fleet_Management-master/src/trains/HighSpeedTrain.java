package trains;

import trains.abstractions.Train;

public class HighSpeedTrain extends Train {

    private int additionalSpeed;

    public int getAdditionalSpeed() {
        return additionalSpeed;
    }

    public void setAdditionalSpeed(int additionalSpeed) {
        if (additionalSpeed < 0) throw new IllegalArgumentException("Недопустимое значение");
        this.additionalSpeed = additionalSpeed;
    }

    @Override
    public double calculateTime() {
        return getRouteLength() / (getBasicSpeed() + getAdditionalSpeed());
    }

    @Override
    public String getTrainDetails() {
        return super.getTrainDetails() + "\nДополнительная скорость - " + additionalSpeed + " км/ч\nВремя в пути - " + String.format("%.1f ч", calculateTime());
    }
}
