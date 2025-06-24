package org.example;

import java.util.Random;

/**
 * Класс Teacher представляет преподавателя, который может выставлять оценки студентам.
 */
public class Teacher extends Human {
    private final Subject subject;
    private final Random random = new Random();

    /**
     * Создаёт преподавателя по предмету.
     *
     * @param name    имя преподавателя
     * @param age     возраст
     * @param gender  пол
     * @param subject предмет, который он преподаёт
     */
    public Teacher(String name, int age, String gender, Subject subject) {
        super(name, age, gender);
        this.subject = subject;
    }

    /**
     * Выставляет студенту 5 случайных оценок по предмету.
     * Оценки от 3 до 5 включительно.
     *
     * @param student студент
     */
    public void gradeStudent(Student student) {
        for (int i = 0; i < 5; i++) {
            int grade = 3 + random.nextInt(3); // 3, 4 или 5
            student.addGrade(subject.GetName(), grade);
        }
    }

    /**
     * Возвращает название предмета.
     * @return название предмета
     */
    public String getSubjectName() {
        return subject.GetName();
    }

    @Override
    public String toString() {
        return super.toString() + "\tПредмет: " + subject.GetName();
    }
}
