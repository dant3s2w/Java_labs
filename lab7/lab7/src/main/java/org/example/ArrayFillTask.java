package org.example;

import java.util.Random;

/**
 * Задача многопоточного заполнения массива студентов.
 * Каждый поток создаёт студентов для своего диапазона индексов
 * и выставляет им оценки.
 */
public class ArrayFillTask implements Runnable {
    private final Student[] array;
    private final int from;
    private final int to;
    private final Parent parent;
    private final Teacher teacher;
    private static final Random rand = new Random();

    /**
     * @param array общий массив студентов
     * @param from  первый индекс (включительно)
     * @param to    последний индекс (исключительно)
     * @param parent общий родитель для всех студентов
     * @param teacher преподаватель, который будет проставлять оценки
     */
    public ArrayFillTask(Student[] array, int from, int to, Parent parent, Teacher teacher) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.parent = parent;
        this.teacher = teacher;
    }

    @Override
    public void run() {
        for (int i = from; i < to; i++) {
            // создаём нового студента
            Student s = new Student(
                    "Student" + i,
                    18 + rand.nextInt(5),
                    "М",
                    parent
            );
            // проставляем оценки
            teacher.gradeStudent(s);
            // сохраняем в массив
            array[i] = s;
            // для наглядности раз в 100 записей выводим прогресс
            if (i % 100 == 0) {
                System.out.println(
                        Thread.currentThread().getName()
                                + " заполнил индекс " + i
                );
            }
        }
    }
}
