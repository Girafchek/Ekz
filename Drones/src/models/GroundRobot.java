package models;

public class GroundRobot extends Transport {
    private static final long serialVersionUID = 1L;
    private int wheelCondition; // 0 - 100

    public GroundRobot(String id, String model, double batteryLevel, int wheelCondition) {
        super(id, model, batteryLevel);
        this.wheelCondition = wheelCondition;
    }

    public int getWheelCondition() { return wheelCondition; }
    public void setWheelCondition(int wheelCondition) { this.wheelCondition = wheelCondition; }

    @Override
    public String getDetails() {
        return String.format("Робот [%s] Модель: %s | Заряд: %.1f%% | Статус: %s | Износ колес: %d%%",
                id, model, batteryLevel, isInTransit() ? "В пути" : "На базе", wheelCondition);
    }
}