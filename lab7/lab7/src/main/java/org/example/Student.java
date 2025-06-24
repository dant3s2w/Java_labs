package org.example;

import java.util.*;

/**
 * Класс Student представляет студента, который хранит информацию об оценках,
 * среднем балле, родителе и премиальных. Реализован с учётом многопоточности.
 */
public class Student extends Human {

    private final Map<String, List<Integer>> grades = new HashMap<>();
    private final Map<String, Double> averageGrades = new HashMap<>();
    private final Parent parent;
    private int premium;

    /**
     * Создаёт нового студента и автоматически добавляет его родителю.
     *
     * @param name   имя студента
     * @param age    возраст
     * @param gender пол
     * @param parent родитель студента
     */
    public Student(String name, int age, String gender, Parent parent) {
        super(name, age, gender);
        this.parent = parent;
        this.premium = 0;
        parent.addChild(this);
    }

    /**
     * Добавляет новую оценку по предмету.
     *
     * @param subject название предмета
     * @param grade   оценка
     */
    public synchronized void addGrade(String subject, int grade) {
        grades.computeIfAbsent(subject, k -> new ArrayList<>()).add(grade);
    }

    /**
     * Вычисляет и сохраняет среднюю оценку по предмету.
     *
     * @param subject название предмета
     * @return средний балл
     */
    public synchronized double calculateAverage(String subject) {
        List<Integer> list = grades.getOrDefault(subject, Collections.emptyList());
        double avg = list.stream().mapToInt(i -> i).average().orElse(0.0);
        averageGrades.put(subject, avg);
        return avg;
    }

    /**
     * Рассчитывает премию и настроение родителя по среднему баллу.
     *
     * @param subject название предмета
     */
    public void getStatusAndPremium(String subject) {
        double avg = calculateAverage(subject);
        synchronized (parent) {
            if (avg >= 3 && avg < 4) {
                parent.setMood("хмурый");
            } else if (avg <= 4.5) {
                parent.setMood("удовлетворённый");
                parent.addPremium(5000);
                premium += 5000;
            } else {
                parent.setMood("радостный");
                parent.addPremium(10000);
                premium += 10000;
            }
        }
    }

    /**
     * @return родитель этого студента
     */
    public Parent GetParent() {
        return parent;
    }

    /**
     * Возвращает сумму премии.
     * @return премиальные
     */
    public int getPremium() {
        return premium;
    }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\tСредняя: ");
        averageGrades.forEach((sub, av) -> sb.append(sub).append(":").append(String.format("%.2f", av)).append(" "));
        sb.append("\tПремиальные: ").append(premium);
        return sb.toString();
    }
}
