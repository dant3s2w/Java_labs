// File: src/org/example/RandomStudentGenerator.java
package org.example;

import java.util.Random;

/**
 * Создает случайные объекты Student для заполнения коллекций.
 */
public class RandomStudentGenerator {
    private static final String[] NAMES = {
            "Иван", "Пётр", "Анна", "Мария", "Дмитрий", "Елена"
    };
    private static final String[] GENDERS = {"Мужской", "Женский"};
    private final Random rnd = new Random();
    private final Parent dummy = new Parent("Родитель‑Тестер", 40, "Неизвестно");

    /**
     * @return новый Student со случайными именем, возрастом и полом
     */
    public Student next() {
        String name = NAMES[rnd.nextInt(NAMES.length)]
                + rnd.nextInt(100);
        int age = 18 + rnd.nextInt(8);
        String gender = GENDERS[rnd.nextInt(GENDERS.length)];
        return new Student(name, age, gender, dummy);
    }
}
