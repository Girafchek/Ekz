package models;

public class DeliveryDrone extends Transport {
    private static final long serialVersionUID = 1L;
    private double maxPayload;

    public DeliveryDrone(String id, String model, double batteryLevel, double maxPayload) {
        super(id, model, batteryLevel);
        this.maxPayload = maxPayload;
    }

    public double getMaxPayload() { return maxPayload; }
    public void setMaxPayload(double maxPayload) { this.maxPayload = maxPayload; }

    @Override
    public String getDetails() {
        return String.format("Дрон [%s] Модель: %s | Заряд: %.1f%% | Статус: %s | Грузоподъемность: %.1f кг",
                id, model, batteryLevel, isInTransit() ? "В пути" : "На базе", maxPayload);
    }
}