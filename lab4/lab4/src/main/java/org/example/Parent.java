package org.example;

import java.util.*;

/**
 * Родитель: наследник org.example.Human, хранит детей и премиальные.
 */
public class Parent extends Human {
    private String mood;
    private int totalPremium;
    private List<Student> children = new ArrayList<>();

    /**
     * @param name имя
     * @param age возраст
     * @param gender пол
     */
    public Parent(String name, int age, String gender) {
        super(name, age, gender);
        this.mood = "Нейтральный";
        this.totalPremium = 0;
    }

    /** @param mood настроение */
    public void setMood(String mood) { this.mood = mood; }
    /** @param amount сумма премии */
    public void addPremium(int amount) { totalPremium += amount; }
    /** @param child добавляет ребенка */
    public void addChild(Student child) { children.add(child); }
    /** @return список детей */
    public List<Student> getChildren() { return children; }

    @Override
    public String toString() {
        String kids = children.isEmpty()?"Нет детей":
                String.join(", ",
                        children.stream().map(Student::GetName).toList());
        return super.toString()
                + "\tПремиальные:" + totalPremium
                + "\tДети:" + kids;
    }
}