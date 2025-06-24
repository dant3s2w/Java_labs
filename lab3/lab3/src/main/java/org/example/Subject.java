package org.example;

/**
 * Условный предмет обучения.
 */
public class Subject {
    private String name;

    /** @param name название */
    public Subject(String name) { this.name = name; }
    /** @param name новое название */
    public void SetName(String name) { this.name = name; }
    /** @return название */
    public String GetName() { return name; }
}