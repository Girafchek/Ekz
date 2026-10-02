package logic;

import models.CloudResource;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ResourcePolicy<T extends CloudResource> {

    private String policyName;
    //Predicate<T> - Ожидает лямбда выражение (т.е. функцию) условие (т.e. возвращает boolean)
    private Predicate<T> condition;
    //Consumer<T> - ожидает лямбда выражение (т.e. функцию) действие (т.e. возвращает void)
    private Consumer<T> action;

    public ResourcePolicy(String policyName, Predicate<T> condition, Consumer<T> action) {
        this.policyName = policyName;
        this.condition = condition;
        this.action = action;
    }


    public void apply(T resource){
        if (condition.test(resource)) { action.accept(resource); }
        //Да, фактически решение прописанно прямо в условии задания
    }
}
