package org.example.model;

/**
 * Представляет родителя студента.
 * Наследует поля имя, возраст и пол от класса Human.
 */
public class Parent extends Human {

    /**
     * Создаёт объект Parent с указанными данными.
     *
     * @param name   имя родителя
     * @param age    возраст родителя в годах
     * @param gender пол родителя ("М" или "Ж")
     */
    public Parent(String name, int age, String gender) {
        super(name, age, gender);
    }
}
