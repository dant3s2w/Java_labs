package org.example.model;

/**
 * Описывает студента с указанием родителя и выставленной оценки.
 */
public class Student extends Human {
    /**
     * Родитель студента.
     */
    private Parent parent;

    /**
     * Оценка студента по предмету.
     */
    private int grade;

    /**
     * Создаёт студента с именем, возрастом, полом и родителем.
     *
     * @param name   имя студента
     * @param age    возраст студента в годах
     * @param gender пол студента ("М" или "Ж")
     * @param parent объект родителя
     */
    public Student(String name, int age, String gender, Parent parent) {
        super(name, age, gender);
        this.parent = parent;
    }

    /**
     * Устанавливает оценку студенту.
     *
     * @param grade целочисленное значение оценки
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * Возвращает объект родителя студента.
     *
     * @return родитель студента
     */
    public Parent getParent() {
        return parent;
    }

    /**
     * Возвращает текущую оценку студента.
     *
     * @return значение оценки
     */
    public int getGrade() {
        return grade;
    }
}
