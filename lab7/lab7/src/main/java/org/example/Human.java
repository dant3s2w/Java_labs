package org.example;

/**
 * Абстрактный класс для представления человека.
 */
public abstract class Human {
    private String name;   // имя
    private int age;       // возраст
    private String gender; // пол

    /**
     * Конструктор.
     * @param name имя
     * @param age возраст
     * @param gender пол
     */
    protected Human(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    /** @return имя */
    public String GetName() { return name; }
    /** @return возраст */
    public int GetAge() { return age; }
    /** @return пол */
    public String GetGender() { return gender; }

    /** @param name новое имя */
    public void SetName(String name) { this.name = name; }
    /** @param age новый возраст */
    public void SetAge(int age) { this.age = age; }
    /** @param gender новый пол */
    public void SetGender(String gender) { this.gender = gender; }

    @Override
    public String toString() {
        return name + "\t" + age + "\t" + gender;
    }
}