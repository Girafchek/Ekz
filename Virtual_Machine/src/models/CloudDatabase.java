package models;

public class CloudDatabase extends CloudResource{
    private double storageUsed;

    public CloudDatabase(String id, String name, double loadPercentage, double storageUsed) {
        super(id, name, loadPercentage);
        this.storageUsed = storageUsed;
    }

    public double getStorageUsed() {
        return storageUsed;
    }

    public void setStorageUsed(double storageUsed) {
        this.storageUsed = storageUsed;
    }

    @Override
    public String getDetails() {
        return super.getDetails() + "; used storage: " + getStorageUsed();
    }
}
