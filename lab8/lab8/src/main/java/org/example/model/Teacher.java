package org.example.model;

import java.io.Serializable;

/**
 * Представляет преподавателя, наследника класса Human.
 * Отвечает за выставление оценок студентам по определённому предмету.
 */
public class Teacher extends Human {
    /**
     * Предмет, который ведёт преподаватель.
     */
    private Subject subject;

    /**
     * Создаёт объект Teacher с заданными именем, возрастом, полом и предметом.
     *
     * @param name    имя преподавателя
     * @param age     возраст преподавателя в полных годах
     * @param gender  пол преподавателя ("М" или "Ж")
     * @param subject предмет, который ведёт преподаватель
     */
    public Teacher(String name, int age, String gender, Subject subject) {
        super(name, age, gender);
        this.subject = subject;
    }

    /**
     * Ставит студенту случайную оценку от 1 до 5 включительно.
     *
     * @param student студент, которому выставляется оценка
     */
    public void gradeStudent(Student student) {
        student.setGrade((int) (Math.random() * 5 + 1));
    }
}
