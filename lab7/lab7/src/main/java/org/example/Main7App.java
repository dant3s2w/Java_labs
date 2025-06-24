package org.example;

import java.util.concurrent.*;

/**
 * Лабораторная работа №7.
 * Демонстрация многопоточной оценки студентов
 * и конкурентного заполнения массива объектов.
 */
public class Main7App {
    public static void main(String[] args) throws InterruptedException {
        // --- Часть 1: конкурентное заполнение массива ---
        final int N = 1000;
        Student[] students = new Student[N];
        Parent commonParent = new Parent("Родитель", 40, "М");
        Subject subj = new Subject("Многопоточное");
        Teacher fillerTeacher = new Teacher("Заполнитель", 50, "Ж", subj);

        int threadsCount = 4;
        Thread[] fillers = new Thread[threadsCount];
        int chunk = N / threadsCount;
        for (int t = 0; t < threadsCount; t++) {
            int start = t * chunk;
            int end   = (t == threadsCount - 1) ? N : start + chunk;
            fillers[t] = new Thread(
                    new ArrayFillTask(students, start, end, commonParent, fillerTeacher),
                    "Filler-" + t
            );
            fillers[t].start();
        }
        // ждём, пока все потоки заполнят массив
        for (Thread th : fillers) {
            th.join();
        }
        System.out.println("Массив из " + N + " студентов заполнен.\n");

        // --- Часть 2: многопоточная оценка двух студентов ---
        Subject math    = new Subject("Математика");
        Subject physics = new Subject("Физика");

        Parent ivan   = new Parent("Иван",   45, "М");
        Parent elena  = new Parent("Елена",  43, "Ж");

        Student oleg  = new Student("Олег",  20, "М", ivan);
        Student olya  = new Student("Оля",   19, "Ж", elena);

        Teacher petrov    = new Teacher("Петров П.", 40, "М", math);
        Teacher smirnova  = new Teacher("Смирнова А.", 38, "Ж", physics);

        Thread t1 = new Thread(new GradingTask(petrov,  oleg), "Поток-Олег");
        Thread t2 = new Thread(new GradingTask(smirnova, olya), "Поток-Оля");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("\nИтоговые результаты:\n");
        System.out.println(oleg);
        System.out.println(oleg.GetParent());
        System.out.println(olya);
        System.out.println(olya.GetParent());
    }
}
