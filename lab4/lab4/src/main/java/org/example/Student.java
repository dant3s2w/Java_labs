package org.example;

import java.util.*;

/**
 * Студент: наследник org.example.Human, хранит оценки по предметам.
 */
public class Student extends Human {
    private Map<String, List<Integer>> grades = new HashMap<>();
    private Map<String, Double> averageGrades = new HashMap<>();
    private Parent parent;
    private int premium;

    /**
     * @param name имя
     * @param age возраст
     * @param gender пол
     * @param parent родитель
     */
    public Student(String name, int age, String gender, Parent parent) {
        super(name, age, gender);
        this.parent = parent;
        parent.addChild(this);
        this.premium = 0;
    }

    /**
     * Добавляет оценку по предмету.
     * @param subject название предмета
     * @param grade оценка
     */
    public void addGrade(String subject, int grade) {
        grades.computeIfAbsent(subject, k -> new ArrayList<>()).add(grade);
    }

    /**
     * Вычисляет средний балл по предмету.
     * @param subject название предмета
     * @return средний балл
     */
    public double calculateAverage(String subject) {
        List<Integer> list = grades.getOrDefault(subject, Collections.emptyList());
        double avg = list.stream().mapToDouble(i -> i).average().orElse(0);
        averageGrades.put(subject, avg);
        return avg;
    }

    /**
     * Обновляет настроение родителя и премию по среднему баллу.
     * @param subject предмет для оценки
     */
    public void getStatusAndPremium(String subject) {
        double avg = calculateAverage(subject);
        if (avg >= 3 && avg < 4) {
            parent.setMood("хмурый");
        } else if (avg <= 4.5) {
            parent.setMood("удовлетворенный");
            parent.addPremium(5000);
            premium += 5000;
        } else {
            parent.setMood("радостный");
            parent.addPremium(10000);
            premium += 10000;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\tСредняя: ");
        averageGrades.forEach((sub, av) -> sb.append(sub).append(":").append(av).append(" "));
        sb.append("\tПремиальные:").append(premium);
        return sb.toString();
    }

    /** @return родитель */
    public Parent GetParent() { return parent; }
}