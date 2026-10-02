import trains.CargoTrain;
import trains.HighSpeedTrain;
import trains.PassengerTrain;
import trains.TrainType;
import trains.abstractions.Train;
import trains.management.ConsoleHelper;
import trains.management.TrainManager;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final TrainManager manager = new TrainManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== УПРАВЛЕНИЕ ПАРКОМ ПОЕЗДОВ ===");
            System.out.println("1. Добавить поезд\n2. Показать все поезда\n3. Редактировать поезд");
            System.out.println("4. Удалить поезд\n5. Поиск поездов\n6. Выход");
            System.out.print("Выберите действие: ");

            switch (ConsoleHelper.readInt(scanner)) {
                case 1 -> addTrain();
                case 2 -> showAllTrains();
                case 3 -> editTrain();
                case 4 -> deleteTrain();
                case 5 -> searchTrains();
                case 6 -> {
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void addTrain() {
        System.out.println("\n1. Пассажирский\n2. Грузовой\n3. Высокоскоростной");
        System.out.print("Тип поезда: ");
        int type = ConsoleHelper.readInt(scanner);

        System.out.print("Номер: ");
        String number = scanner.nextLine();
        System.out.print("Маршрут: ");
        String route = scanner.nextLine();
        System.out.print("Длина маршрута (км): ");
        double routeLength = ConsoleHelper.readDouble(scanner);
        System.out.print("Количество вагонов: ");
        int cars = ConsoleHelper.readInt(scanner);
        System.out.print("Машинист: ");
        String driver = scanner.nextLine();

        Train train = switch (type) {
            case 1 -> {
                System.out.print("Пассажиров: ");
                PassengerTrain pt = new PassengerTrain();
                pt.setPassengers(ConsoleHelper.readInt(scanner));
                yield pt;
            }
            case 2 -> {
                System.out.print("Вес груза (т): ");
                CargoTrain ct = new CargoTrain();
                ct.setCargoWeight(ConsoleHelper.readDouble(scanner));
                yield ct;
            }
            case 3 -> {
                System.out.print("Доп. скорость (км/ч): ");
                HighSpeedTrain hst = new HighSpeedTrain();
                hst.setAdditionalSpeed(ConsoleHelper.readInt(scanner));
                yield hst;
            }
            default -> null;
        };

        if (train == null) {
            System.out.println("Неверный тип.");
            return;
        }

        train.setTrainNumber(number);
        train.setRoute(route);
        train.setRouteLength(routeLength);
        train.setNumberOfCars(cars);
        train.setDriver(driver);
        train.setType(switch (type) {
            case 1 -> TrainType.Passenger;
            case 2 -> TrainType.Freight;
            default -> TrainType.HighSpeed;
        });
        manager.addTrain(train);
        System.out.println("Добавлен!");
    }

    private static void showAllTrains() {
        if (manager.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }
        int i = 1;
        for (Train t : manager.getAllTrains())
            System.out.println("\n--- Поезд #" + i++ + " ---\n" + t.getTrainDetails());
    }

    private static void editTrain() {
        System.out.print("Номер поезда: ");
        Train t = manager.findTrainByNumber(scanner.nextLine());
        if (t == null) {
            System.out.println("Не найден.");
            return;
        }

        System.out.println(t.getTrainDetails());

        String val = ConsoleHelper.readField(scanner, "Номер", t.getTrainNumber());
        if (val != null) t.setTrainNumber(val);
        val = ConsoleHelper.readField(scanner, "Маршрут", t.getRoute());
        if (val != null) t.setRoute(val);
        Double newRouteLength = ConsoleHelper.readDoubleField(scanner, "Длина маршрута (км)", t.getRouteLength());
        if (newRouteLength != null) t.setRouteLength(newRouteLength);
        Integer newCars = ConsoleHelper.readIntField(scanner, "Вагонов", t.getNumberOfCars());
        if (newCars != null) t.setNumberOfCars(newCars);
        val = ConsoleHelper.readField(scanner, "Машинист", t.getDriver());
        if (val != null) t.setDriver(val);

        switch (t) {
            case PassengerTrain pt -> {
                Integer newPassengers = ConsoleHelper.readIntField(scanner, "Пассажиров", pt.getPassengers());
                if (newPassengers != null) pt.setPassengers(newPassengers);
            }
            case CargoTrain ct -> {
                Double newCargoWeight = ConsoleHelper.readDoubleField(scanner, "Вес груза (т)", ct.getCargoWeight());
                if (newCargoWeight != null) ct.setCargoWeight(newCargoWeight);
            }
            case HighSpeedTrain hst -> {
                Integer newAddSpeed = ConsoleHelper.readIntField(scanner, "Доп. скорость (км/ч)", hst.getAdditionalSpeed());
                if (newAddSpeed != null) hst.setAdditionalSpeed(newAddSpeed);
            }
            default -> {
            }
        }
        System.out.println("Обновлён!");
    }

    private static void deleteTrain() {
        System.out.print("Номер поезда: ");
        System.out.println(manager.removeTrain(scanner.nextLine()) ? "Удалён." : "Не найден.");
    }

    private static void searchTrains() {
        System.out.print("Запрос (номер, машинист, вагоны): ");
        List<Train> results = manager.searchTrains(scanner.nextLine());
        if (results.isEmpty()) {
            System.out.println("Ничего не найдено.");
            return;
        }
        for (Train t : results) System.out.println("\n" + t.getTrainDetails());
    }
}
