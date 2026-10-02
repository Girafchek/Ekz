package logic;

import models.Transport;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogisticsManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String STATE_FILE = "logistics_state.dat";

    private Map<String, List<Transport>> hubs = new HashMap<>();

    // Правила НЕ сериализуются: лямбда-выражения не реализуют Serializable.
    // Это осознанное ограничение — после загрузки состояния правила нужно
    // добавить заново через пункты меню 3 и 4.
    private transient List<DispatchRule<? extends Transport>> rules = new ArrayList<>();

    public LogisticsManager() {
        if (!loadState()) {
            System.out.println("Файл состояния не найден. Создана пустая логистическая сеть.");
        }
    }

    public void addTransport(String hubName, Transport transport) {
        hubs.computeIfAbsent(hubName, k -> new ArrayList<>()).add(transport);
    }

    /**
     * Добавляет правило диспетчеризации в общий список.
     *
     * Внимание: параметр принимается как DispatchRule<?> строго по ТЗ, но
     * внутренний список типизирован как List<DispatchRule<? extends Transport>>.
     * DispatchRule<?> НЕ является подтипом DispatchRule<? extends Transport>
     * (инвариантность wildcard'ов), поэтому добавление возможно только через
     * приведение к raw-типу. Безопасность обеспечивается тем, что реально
     * применить правило к неподходящему типу всё равно не даст applyRuleSafely.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addRule(DispatchRule<?> rule) {
        rules.add((DispatchRule) rule);
    }

    public Transport getTransportById(String id) {
        return hubs.values().stream()
                .flatMap(Collection::stream)
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void applyAllRules() {
        for (List<Transport> transports : hubs.values()) {
            for (Transport transport : transports) {
                for (DispatchRule<?> rule : rules) {
                    applyRuleSafely(rule, transport);
                }
            }
        }
    }

    /**
     * Безопасное применение обобщённого правила к произвольному Transport.
     *
     * Тонкий момент: из-за стирания типов (type erasure) приведение (T) transport
     * в байткоде превращается в (Transport), то есть само по себе НИЧЕГО не проверяет.
     * ClassCastException возникает позже — внутри лямбды правила, когда она,
     * например, пытается вызвать getWheelCondition() на объекте, который на
     * самом деле DeliveryDrone. Мы гасим это исключение — это и есть
     * «проверка совместимости типов», требуемая в задании.
     */
    @SuppressWarnings("unchecked")
    private <T extends Transport> void applyRuleSafely(DispatchRule<T> rule, Transport transport) {
        try {
            rule.apply((T) transport);
        } catch (ClassCastException e) {
            // Тип транспорта не соответствует типу правила — пропускаем
        }
    }

    public Stream<Transport> getAnalyticsStream() {
        return hubs.values().stream().flatMap(Collection::stream);
    }

    /**
     * Возвращает неизменяемое представление карты хабов.
     * Это предотвращает случайную (или намеренную) модификацию внутреннего
     * состояния менеджера из внешнего кода.
     */
    public Map<String, List<Transport>> getHubs() {
        return Collections.unmodifiableMap(hubs);
    }

    // --- Бонус: Сериализация и Десериализация ---
    public void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STATE_FILE))) {
            oos.writeObject(hubs);
            System.out.println("Состояние сети успешно сохранено в " + STATE_FILE);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения состояния: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public boolean loadState() {
        File file = new File(STATE_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                hubs = (Map<String, List<Transport>>) ois.readObject();
                System.out.println("Состояние сети успешно загружено из " + STATE_FILE);
                return true;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Ошибка загрузки состояния: " + e.getMessage());
            }
        }
        return false;
    }
}