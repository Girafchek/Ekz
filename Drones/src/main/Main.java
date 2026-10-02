package main;

import logic.DispatchRule;
import logic.LogisticsManager;
import models.*;

import java.util.*;

public class Main {
    private static LogisticsManager manager = new LogisticsManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt();

            switch (choice) {
                case 1 -> addTransportMenu();
                case 2 -> changeStatusMenu();
                case 3 -> addCalibrationRule();
                case 4 -> addVariant10Rule();
                case 5 -> applyRules();
                case 6 -> runAnalytics();
                case 7 -> {
                    manager.saveState();
                    running = false;
                    System.out.println("Выход из программы...");
                }
                default -> System.out.println("Неверный ввод. Попробуйте снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== СИСТЕМА УПРАВЛЕНИЯ АВТОПАРКОМ ===");
        System.out.println("1. Добавить новый транспорт в хаб");
        System.out.println("2. Отправить/вернуть транспорт");
        System.out.println("3. Добавить базовое правило «Калибровка батареи»");
        System.out.println("4. Добавить правило диспетчеризации (Вариант 10: Техобслуживание)");
        System.out.println("5. Запустить проверку всех правил");
        System.out.println("6. Выполнить аналитический запрос (Вариант 10: Макс заряд в пути)");
        System.out.println("7. Выход из программы");
        System.out.print("Выберите пункт: ");
    }

    private static void addTransportMenu() {
        System.out.println("Выберите тип транспорта (1 - Дрон, 2 - Вертолет, 3 - Робот): ");
        int type = readInt();
        System.out.print("Введите ID: ");
        String id = scanner.nextLine();
        System.out.print("Введите модель: ");
        String model = scanner.nextLine();
        System.out.print("Введите название хаба: ");
        String hub = scanner.nextLine();

        System.out.print("Введите начальный заряд батареи: ");
        double battery = readDouble();

        Transport transport = null;
        if (type == 1) {
            System.out.print("Введите макс. грузоподъемность (кг): ");
            transport = new DeliveryDrone(id, model, battery, readDouble());
        } else if (type == 2) {
            System.out.print("Введите дальность полета (км): ");
            transport = new CargoHelicopter(id, model, battery, readDouble());
        } else if (type == 3) {
            System.out.print("Введите износ колес: ");
            transport = new GroundRobot(id, model, battery, readWheelCondition());
        } else {
            System.out.println("Неверный тип.");
            return;
        }

        manager.addTransport(hub, transport);
        System.out.println("Транспорт успешно добавлен в хаб '" + hub + "'.");
    }

    private static void changeStatusMenu() {
        System.out.print("Введите ID транспорта: ");
        String id = scanner.nextLine();
        Transport t = manager.getTransportById(id);
        if (t != null) {
            System.out.println("Найден: " + t.getDetails());
            System.out.print("Отправить в путь (1) или Вернуть на базу (2)? ");
            int action = readInt();
            if (action == 1) t.dispatch();
            else if (action == 2) t.recall();
            System.out.println("Статус изменен. " + t.getDetails());
        } else {
            System.out.println("Транспорт с таким ID не найден.");
        }
    }

    private static void addCalibrationRule() {
        DispatchRule<Transport> rule = new DispatchRule<>(
                "Калибровка батареи",
                t -> t.getBatteryLevel() < 0,
                t -> {
                    t.setBatteryLevel(0.0);
                    if (t.isInTransit()) {
                        t.recall();
                        System.out.println("Экстренный возврат для " + t.getId() + " из-за ошибки датчика!");
                    }
                }
        );
        manager.addRule(rule);
        System.out.println("Правило «Калибровка батареи» добавлено.");
    }

    private static void addVariant10Rule() {
        DispatchRule<GroundRobot> rule = new DispatchRule<>(
                "Техобслуживание наземных роботов",
                r -> !r.isInTransit() && r.getWheelCondition() < 30,
                r -> {
                    r.setBatteryLevel(100);
                    r.setWheelCondition(100);
                    System.out.println("Проведено техобслуживание робота " + r.getId());
                }
        );
        manager.addRule(rule);
        System.out.println("Правило «Техобслуживание» (Вариант 10) добавлено.");
    }

    private static void applyRules() {
        System.out.println("Запуск применения всех правил...");
        manager.applyAllRules();
        System.out.println("Проверка завершена.");
    }

    private static void runAnalytics() {
        System.out.println("\n--- Аналитический запрос (Вариант 10) ---");
        System.out.println("Поиск транспорта с максимальным зарядом среди тех, что в пути...");

        Optional<Transport> maxBatteryInTransit = manager.getAnalyticsStream()
                .filter(Transport::isInTransit)
                .max(Comparator.comparingDouble(Transport::getBatteryLevel));

        if (maxBatteryInTransit.isPresent()) {
            System.out.println("Результат: " + maxBatteryInTransit.get().getDetails());
        } else {
            System.out.println("Результат: В данный момент нет транспорта, находящегося в пути.");
        }
    }

    private static int readInt() {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine());
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Ошибка ввода. Введите целое число: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                double val = Double.parseDouble(scanner.nextLine().replace(",", "."));
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Ошибка ввода. Введите число: ");
            }
        }
    }

    private static int readWheelCondition() {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Ошибка: введите целое число. Попробуйте снова: ");
            }
        }
    }
}