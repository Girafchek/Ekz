package main;

import logic.CloudManager;
import logic.ResourcePolicy;
import models.CloudDatabase;
import models.CloudResource;
import models.LoadBalancer;
import models.VirtualMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class main {

    static CloudManager manager;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args){

        if (manager == null){
            manager = new CloudManager(
                    new HashMap<>(),
                    new ArrayList<>()
            );
        }

        while (true){
            getInfo();
            printCommands();
            String command = scanner.nextLine().strip();
            try {
                switch (command) {
                    case ("1") -> addNewResources();
                    case ("2") -> switchStatus();
                    case ("3") -> addBasePolicy();
                    case ("4") -> addCustomPolicy();
                    case ("5") -> manager.applyAllPolicies();
                    case ("6") -> analise();
                    case ("7") -> {return;}
                    default -> System.out.println("Комманда не распознана, попробуйте ещё раз");
                }
            } catch (Exception e) {
                System.out.println("Не получилось совершить действие, попробуйте ещё раз");
            }


        }
    }

    public static void printCommands(){
        System.out.println(
                "-------------------------------------" + System.lineSeparator() +
                        "Введите номер команды:" + System.lineSeparator() +
                        "1. Добавить новый ресурс в проект" + System.lineSeparator() +
                        "2. Запустить/остановить ресурс" + System.lineSeparator() +
                        "3. Добавить базовую политику «Сброс фантомной нагрузки»" + System.lineSeparator() +
                        "4. Добавить политику автоматизации в систему" + System.lineSeparator() +
                        "5. Запустить проверку всех добавленных политик" + System.lineSeparator() +
                        "6. Выполнить аналитический запрос"+ System.lineSeparator() +
                        "7. Выход из программы"
        );
    }

    public static void addNewResources(){
        System.out.println("Укажите вид подключаемого ресурса, где:" + System.lineSeparator() +
                "1) CloudDatabase;" + System.lineSeparator() +
                "2) LoadBalancer;" + System.lineSeparator() +
                "3) VirtualMachine;");
        int type = Integer.parseInt(scanner.nextLine());

        System.out.print("Project: ");
        String project = scanner.nextLine();

        System.out.print("Id: ");
        String id = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Percentage: ");
        double percentage = Double.parseDouble(scanner.nextLine());

        switch (type) {
            case (1) -> {
                System.out.print("storageUsed: ");
                double storageUsed = Double.parseDouble(scanner.nextLine());
                manager.addResource(project, new CloudDatabase(id, name, percentage, storageUsed));
            }
            case (2) -> {
                System.out.print("activeConnection: ");
                int activeConnection = Integer.parseInt(scanner.nextLine());
                manager.addResource(project, new LoadBalancer(id, name, percentage, activeConnection));
            }
            case (3) -> {
                System.out.print("ramSize: ");
                int ramSize = Integer.parseInt(scanner.nextLine());
                manager.addResource(project, new VirtualMachine(id, name, percentage, ramSize));
            }
        }
    }

    public static void getInfo(){
        manager.getAnalyticsStream().forEach(
                resource -> System.out.println(resource.getDetails())
        );
    }

    public static void switchStatus(){
        System.out.print("Id: ");
        String id = scanner.nextLine();
        CloudResource resource = manager.getResourceById(id);
        if (resource.isActive()) resource.stop();
        else resource.start();
    }

    public static void addBasePolicy(){
        manager.addPolicy(new ResourcePolicy<CloudResource>(
            "Сброс фантомной нагрузки",
                cloudResource -> !cloudResource.isActive() && cloudResource.getLoadPercentage() > 0,
                cloudResource -> cloudResource.setLoadPercentage(0)
        ));
    }

    //В принципе эти методы с созданием политик можно не выносить из main и оставить прямя в кейсах блока switch

    /**
     * Если Виртуальная машина (VirtualMachine) запущена и её
     * loadPercentage > 90.0, увеличить её ramSize на 8 (ГБ)
     */
    public static void addCustomPolicy(){
        manager.addPolicy(new ResourcePolicy<VirtualMachine>( //Здесь указываем класс, на который распрострняеться политика
                "VirtualMachine", // название метода
                virtualMachine -> virtualMachine.isActive() & virtualMachine.getLoadPercentage() > 90, //условие
                virtualMachine -> virtualMachine.setRamSize(virtualMachine.getRamSize() + 8)           //действие
                )
        );
    }

    /**
     * Выводит полученные из аналитики данные в читаемом формает
     */
    public static void analise(){
        Map<String, Long> counts = countSystem();
        for (String key : counts.keySet()){
            System.out.println(key + " - " + counts.get(key));
        }
    }

    /**
     * Подсчитать точное количество ресурсов каждого конкретного класса во всем облаке
     * @return counts of class
     */
    public static Map<String, Long> countSystem(){
        Map<String, Long> counts = new HashMap<>();
        manager.getAnalyticsStream().forEach(
                resource -> {
                    String name = resource.getClass().getSimpleName();
                    if (!counts.containsKey(name)){
                        counts.put(name, 0L); //0L - Parse to Long; Запоминать не стоит, при вводе обычного числа
                    }                            //компилятор сам предложит заменить ну нужный формат
                    counts.put(name, counts.get(name) + 1);
                }
        );
        return counts;
    }
}
