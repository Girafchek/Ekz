package models;

import java.io.Serializable;

public abstract class Transport implements interfaces.Dispatchable, Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String model;
    protected boolean status; // true - в пути, false - на базе
    protected double batteryLevel;

    public Transport(String id, String model, double batteryLevel) {
        this.id = id;
        this.model = model;
        this.batteryLevel = batteryLevel;
        this.status = false; // По умолчанию на базе
    }

    @Override
    public void dispatch() {
        this.status = true;
    }

    @Override
    public void recall() {
        this.status = false;
    }

    @Override
    public boolean isInTransit() {
        return status;
    }

    public abstract String getDetails();

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public double getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(double batteryLevel) { this.batteryLevel = batteryLevel; }
}