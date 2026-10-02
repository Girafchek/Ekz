package logic;

import models.Transport;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DispatchRule<T extends Transport> {
    private String ruleName;
    private Predicate<T> condition;
    private Consumer<T> action;

    public DispatchRule(String ruleName, Predicate<T> condition, Consumer<T> action) {
        this.ruleName = ruleName;
        this.condition = condition;
        this.action = action;
    }

    public void apply(T transport) {
        if (condition.test(transport)) {
            action.accept(transport);
        }
    }

    public String getRuleName() {
        return ruleName;
    }
}