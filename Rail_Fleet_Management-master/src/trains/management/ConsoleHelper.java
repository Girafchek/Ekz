package trains.management;

import java.util.Scanner;

public class ConsoleHelper {

    public static String readField(Scanner scanner, String prompt, String current) {
        System.out.print(prompt + " (" + current + "): ");
        String val = scanner.nextLine().trim();
        return val.isEmpty() ? null : val;
    }

    public static Integer readIntField(Scanner scanner, String prompt, int current) {
        System.out.print(prompt + " (" + current + "): ");
        String val = scanner.nextLine().trim();
        if (val.isEmpty()) return null;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            System.out.println("Неверное число, оставлено прежнее значение.");
            return null;
        }
    }

    public static Double readDoubleField(Scanner scanner, String prompt, double current) {
        System.out.print(prompt + " (" + current + "): ");
        String val = scanner.nextLine().trim();
        if (val.isEmpty()) return null;
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            System.out.println("Неверное число, оставлено прежнее значение.");
            return null;
        }
    }

    public static int readInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Введите целое число: ");
            }
        }
    }

    public static double readDouble(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Введите число: ");
            }
        }
    }
}
