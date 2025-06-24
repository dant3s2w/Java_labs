package org.example;

import java.io.*;
import java.util.*;

/**
 * Простая текстовая «база данных» студентов.
 * Формат файла: id,name,age,gender (по строкам).
 * Если файл отсутствует, он создаётся автоматически.
 */
public class StudentDB {
    private final String filename;
    private final AppLogger log;
    private final Map<Integer, Student> data = new LinkedHashMap<>();
    private int nextId = 1;
    private final Parent dummy = new Parent("Parent", 0, "Unknown");

    /**
     * Конструктор. Если файла нет, создаёт его пустым.
     *
     * @param filename путь к файлу БД
     * @param log      логгер для записи операций
     */
    public StudentDB(String filename, AppLogger log) {
        this.filename = filename;
        this.log = log;
        File f = new File(filename);
        if (!f.exists()) {
            try {
                if (f.createNewFile()) {
                    log.log("Создан новый файл БД: " + filename);
                }
            } catch (IOException e) {
                log.recordError("Не могу создать файл БД: " + e.getMessage());
            }
        }
    }

    /**
     * Загружает всех студентов из файла.
     * Если файл пустой — возвращает пустой список.
     */
    public void load() {
        data.clear();
        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("Файл БД не найден, создаём новый...");
            log.log("Load DB: файл не найден, создан новый");
            try {
                f.createNewFile();
            } catch (IOException e) {
                log.recordError("Не могу создать файл БД при загрузке: " + e.getMessage());
            }
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                int id = Integer.parseInt(parts[0]);
                Student s = new Student(
                        parts[1],
                        Integer.parseInt(parts[2]),
                        parts[3],
                        dummy
                );
                data.put(id, s);
                nextId = Math.max(nextId, id + 1);
            }
            System.out.println("Загружено студентов: " + data.size());
            log.log("Load DB: " + data.size());
        } catch (Exception e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
            log.recordError("Load: " + e.getMessage());
        }
    }

    /**
     * Сохраняет текущее состояние в файл (перезапись).
     */
    public void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (var entry : data.entrySet()) {
                int id = entry.getKey();
                Student s = entry.getValue();
                writer.printf("%d,%s,%d,%s%n",
                        id,
                        s.GetName(),
                        s.GetAge(),
                        s.GetGender());
            }
            System.out.println("Сохранено студентов: " + data.size());
            log.log("Save DB: " + data.size());
        } catch (Exception e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
            log.recordError("Save: " + e.getMessage());
        }
    }

    /**
     * Выводит всех студентов в консоль.
     */
    public void listAll() {
        data.forEach((id, s) ->
                System.out.printf("%d: %s%n", id, s)
        );
        log.log("List all: " + data.size());
    }

    /**
     * Добавляет нового студента, данные вводятся с консоли.
     */
    public void put(BufferedReader in) {
        try {
            System.out.print("Имя: ");
            String name = in.readLine();
            System.out.print("Возраст: ");
            int age = Integer.parseInt(in.readLine());
            System.out.print("Пол: ");
            String gender = in.readLine();
            Student s = new Student(name, age, gender, dummy);
            data.put(nextId, s);
            log.log("Add ID=" + nextId);
            nextId++;
        } catch (Exception e) {
            System.out.println("Ошибка добавления: " + e.getMessage());
            log.recordError("Put: " + e.getMessage());
        }
    }

    /**
     * Изменяет данные студента по ID.
     */
    public void change(BufferedReader in) {
        try {
            System.out.print("ID для изменения: ");
            int id = Integer.parseInt(in.readLine());
            Student s = data.get(id);
            if (s == null) {
                System.out.println("Не найдено");
                return;
            }
            System.out.print("Новое имя: ");
            s.SetName(in.readLine());
            System.out.print("Новый возраст: ");
            s.SetAge(Integer.parseInt(in.readLine()));
            System.out.print("Новый пол: ");
            s.SetGender(in.readLine());
            log.log("Change ID=" + id);
        } catch (Exception e) {
            System.out.println("Ошибка изменения: " + e.getMessage());
            log.recordError("Change: " + e.getMessage());
        }
    }

    /**
     * Удаляет студента по ID.
     */
    public void del(BufferedReader in) {
        try {
            System.out.print("ID для удаления: ");
            int id = Integer.parseInt(in.readLine());
            if (data.remove(id) != null) {
                log.log("Del ID=" + id);
            } else {
                System.out.println("Не найдено");
            }
        } catch (Exception e) {
            System.out.println("Ошибка удаления: " + e.getMessage());
            log.recordError("Del: " + e.getMessage());
        }
    }
}
