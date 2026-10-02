package models;

public class LoadBalancer extends CloudResource{
    private int activeConnection;

    public LoadBalancer(String id, String name, double loadPercentage, int activeConnection) {
        super(id, name, loadPercentage);
        this.activeConnection = activeConnection;
    }

    public int getActiveConnection() {
        return activeConnection;
    }

    public void setActiveConnection(int activeConnection) {
        this.activeConnection = activeConnection;
    }

    @Override
    public String getDetails() {
        return super.getDetails() + "; active connection: " + getActiveConnection();
    }
}
