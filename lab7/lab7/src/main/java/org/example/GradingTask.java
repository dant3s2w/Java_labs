package org.example;

/**
 * Задача, выполняющая оценивание студента в отдельном потоке.
 */
public class GradingTask implements Runnable {
    private final Teacher teacher;
    private final Student student;

    /**
     * Конструктор.
     * @param teacher преподаватель
     * @param student студент
     */
    public GradingTask(Teacher teacher, Student student) {
        this.teacher = teacher;
        this.student = student;
    }

    @Override
    public void run() {
        System.out.println("Начало оценивания: " + student.GetName() + " (" + Thread.currentThread().getName() + ")");
        synchronized (student.GetParent()) {
            teacher.gradeStudent(student);
            student.getStatusAndPremium(teacher.getSubjectName());
        }
        System.out.println("Оценивание завершено: " + student.GetName() + " | Премия: " + student.getPremium());
    }
}
