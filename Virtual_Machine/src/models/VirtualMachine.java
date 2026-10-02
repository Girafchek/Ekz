package models;

public class VirtualMachine extends CloudResource {

    private int ramSize;

    public VirtualMachine(String id, String name, double loadPercentage, int ramSize) {
        super(id, name, loadPercentage);
        this.ramSize = ramSize;
    }

    public int getRamSize() {
        return ramSize;
    }

    public void setRamSize(int ramSize) {
        this.ramSize = ramSize;
    }

    @Override
    public String getDetails(){
        String answer = super.getDetails();
        answer += "; RAM size: " + getRamSize();
        return answer;
    };
}
