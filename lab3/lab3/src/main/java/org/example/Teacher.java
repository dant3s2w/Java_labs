package org.example;

import java.util.Random;

/**
 * Преподаватель: наследник Human, выставляет оценки.
 */
public class Teacher extends Human {
    private Subject subject;

    /**
     * @param name имя
     * @param age возраст
     * @param gender пол
     * @param subject преподаваемый предмет
     */
    public Teacher(String name, int age, String gender, Subject subject) {
        super(name, age, gender);
        this.subject = subject;
    }

    /**
     * Проставляет 5 случайных оценок студенту.
     * @param student студент
     */
    public void gradeStudent(Student student) {
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            int g = 3 + rand.nextInt(3);
            student.addGrade(subject.GetName(), g);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\tПредмет:" + subject.GetName();
    }
}