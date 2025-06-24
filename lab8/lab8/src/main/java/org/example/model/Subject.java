package org.example.model;

import java.io.Serializable;

/**
 * Описывает учебный предмет.
 */
public class Subject implements Serializable {
    /**
     * Название предмета.
     */
    private String title;

    /**
     * Создаёт предмет с указанным названием.
     *
     * @param title название предмета
     */
    public Subject(String title) {
        this.title = title;
    }

    /**
     * Возвращает название предмета.
     *
     * @return название предмета
     */
    public String getTitle() {
        return title;
    }
}
