package org.example.model;

import java.io.Serializable;

/**
 * Базовый класс для описания человека.
 * Содержит имя, возраст и пол.
 *
 * @author Николаев Даниил
 * @version 0.0.1
 */
public class Human implements Serializable {
    /**
     * Имя человека.
     */
    protected String name;

    /**
     * Возраст человека в полных годах.
     */
    protected int age;

    /**
     * Пол человека: "М" или "Ж".
     */
    protected String gender;

    /**
     * Создаёт объект Human с заданными параметрами.
     *
     * @param name   имя человека
     * @param age    возраст в полных годах
     * @param gender пол человека ("М" или "Ж")
     */
    public Human(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    /**
     * Возвращает имя человека.
     *
     * @return имя
     */
    public String getName() {
        return name;
    }
}
