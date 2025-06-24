package org.example;

/**
 *
 * @author Николаев Даниил
 * @version 0.0.1
 */
public class Main {
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