package models;

public class CargoHelicopter extends Transport {
    private static final long serialVersionUID = 1L;
    private double flightRange;

    public CargoHelicopter(String id, String model, double batteryLevel, double flightRange) {
        super(id, model, batteryLevel);
        this.flightRange = flightRange;
    }

    public double getFlightRange() { return flightRange; }
    public void setFlightRange(double flightRange) { this.flightRange = flightRange; }

    @Override
    public String getDetails() {
        return String.format("Вертолет [%s] Модель: %s | Заряд: %.1f%% | Статус: %s | Дальность: %.1f км",
                id, model, batteryLevel, isInTransit() ? "В пути" : "На базе", flightRange);
    }
}