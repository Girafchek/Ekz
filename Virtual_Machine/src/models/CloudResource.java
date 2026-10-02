package models;

import interfaces.Manageable;

public class CloudResource implements Manageable {
    protected String id;
    protected String name;
    protected boolean status;
    protected double loadPercentage;

    public String getDetails(){
        String answer = getId() + " " + this.getClass().getSimpleName();
        answer += "; name: " + getName();
        answer += "; status: " + isActive();
        answer += "; load percentage: " + getLoadPercentage();
        return answer;
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Данный геттер не имеет смысла, т.к. дублирует метод isActive() из интерфейса Manageable.
     */
//    public boolean isStatus() {
//        return status;
//    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getLoadPercentage() {
        return loadPercentage;
    }

    public void setLoadPercentage(double loadPercentage) {
        this.loadPercentage = Math.max(Math.min(loadPercentage, 100), 0);
        //this.loadPercentage = Math.clamp(loadPercentage, 0, 100); Только с Java 21+
    }

    @Override
    public void start() {
        status = true;
    }

    @Override
    public void stop() {
        status = false;
    }

    @Override
    public boolean isActive() {
        return status;
    }


    public CloudResource(String id, String name, double loadPercentage) {
        setId(id);
        setName(name);
        setStatus(false); //По умолчанию сначала выключен
        setLoadPercentage(loadPercentage);
        //Вообще, здесь под сет нужно оборачивать только setLoadPercentage т.к. у нас есть вложенное условие
        //но переписывать всё на set являеться хорошей практикой, хотя в условиях экзаменв можно и принебречь.
        //Далее по методам я уже не буду переписывать на метод сеттер бех должной необходимости
    }
}
