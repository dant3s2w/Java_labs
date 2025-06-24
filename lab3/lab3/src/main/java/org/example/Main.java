package org.example;

/**
 * Класс Main — точка входа в программу.
 * Создаёт предметы, родителей, студентов и преподавателей,
 * выставляет оценки и выводит информацию о студентах и их родителях.
 *
 * @author Николаев Даниил
 * @version 0.0.1
 */
public class Main {

    /**
     * Главный метод. Запускает последовательность действий:
     * <ul>
     *   <li>Создаёт объекты предметов (Математика, Физика, Химия).</li>
     *   <li>Создаёт объекты родителей с их данными.</li>
     *   <li>Создаёт объекты студентов и связывает их с родителями.</li>
     *   <li>Создаёт объект преподавателя и ставит оценки студентам.</li>
     *   <li>Получает статус и премию студентов по заданному предмету.</li>
     *   <li>Выводит информацию о студентах и их родителях в консоль.</li>
     * </ul>
     *
     * @param args массив аргументов командной строки
     */

    public static void main(String[] args) {
        Subject m = new Subject("Математика");
        Subject p = new Subject("Физика");
        Subject c = new Subject("Химия");

        Parent pr1 = new Parent("Иван", 45, "М");
        Parent pr2 = new Parent("Елена", 43, "Ж");

        Student st1 = new Student("Олег", 20, "М", pr1);
        Student st2 = new Student("Оля", 19, "Ж", pr2);

        Teacher t1 = new Teacher("Петров Петр", 35, "М", m);
        t1.gradeStudent(st1);
        t1.gradeStudent(st2);

        st1.getStatusAndPremium(m.GetName());
        st2.getStatusAndPremium(m.GetName());

        System.out.println(st1);
        System.out.println(st1.GetParent());
        System.out.println(st2);
        System.out.println(st2.GetParent());
    }
}