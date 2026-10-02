package trains.management;

import trains.abstractions.Train;
import java.util.ArrayList;
import java.util.List;

public class TrainManager {
    private final ArrayList<Train> trains = new ArrayList<>();

    public void addTrain(Train train) {
        trains.add(train);
    }

    public Train findTrainByNumber(String number) {
        for (Train t : trains) {
            if (t.getTrainNumber().equalsIgnoreCase(number.trim())) {
                return t;
            }
        }
        return null;
    }

    public boolean removeTrain(String number) {
        Train train = findTrainByNumber(number);
        if (train != null) {
            trains.remove(train);
            return true;
        }
        return false;
    }


    public List<Train> getAllTrains() {
        return new ArrayList<>(trains);
    }

    public List<Train> searchTrains(String query) {
        List<Train> results = new ArrayList<>();
        for (Train t : trains) {
            if (t.matches(query)) {
                results.add(t);
            }
        }
        return results;
    }

    public boolean isEmpty() {
        return trains.isEmpty();
    }

}
