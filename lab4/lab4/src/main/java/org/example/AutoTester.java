package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.Map;

/**
 * Автотесты для проверки CRUD и I/O.
 * Демонстрирует добавление, загрузку, сохранение, изменение и удаление.
 */
public class AutoTester {
    private final AppLogger log;
    private final String dbFile;

    public AutoTester(AppLogger log, String dbFile) {
        this.log    = log;
        this.dbFile = dbFile;
    }

    /** Запуск всех автотестов. */
    public void runAll() {
        log.log("Autotests start");
        testEmptyLoad();
        testCRUD();
        log.log("Autotests end");
    }

    /** Тест: загрузка отсутствующего файла. */
    private void testEmptyLoad() {
        new File(dbFile).delete();
        StudentDB db = new StudentDB(dbFile, log);
        db.load();
        System.out.println("Test empty load: OK");
        log.log("Test empty load: OK");
    }

    /** Тест основных операций CRUD: put, save, load, change, delete. */
    private void testCRUD() {
        // Подготовка: удаляем старый файл, создаём новый
        new File(dbFile).delete();
        StudentDB db = new StudentDB(dbFile, log);

        // Подготовка ввода для put, change и del
        String input = String.join("\n",
                // для put:
                "Alice",      // имя
                "21",         // возраст
                "Женский",    // пол
                // для change:
                "1",          // ID
                "AliceNew",   // новое имя
                "22",         // новый возраст
                "Женский",    // новый пол
                // для del:
                "1"           // ID для удаления
        );
        BufferedReader reader = new BufferedReader(new StringReader(input));

        // 1) put
        db.put(reader);
        // 2) save
        db.save();
        // 3) load в новый объект
        StudentDB db2 = new StudentDB(dbFile, log);
        db2.load();
        boolean okLoad = (count(db2) == 1);
        System.out.println("Test put/save/load: " + (okLoad ? "OK" : "FAIL"));
        log.log("Test put/save/load: " + (okLoad ? "OK" : "FAIL"));

        // 4) change
        db2.change(reader);
        boolean okChange = false;
        try {
            var field = StudentDB.class.getDeclaredField("data");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<Integer, Student> map = (Map<Integer, Student>) field.get(db2);
            for (Student s : map.values()) {
                if ("AliceNew".equals(s.GetName())) {
                    okChange = true;
                    break;
                }
            }
        } catch (Exception e) {
            log.recordError("Reflect change check: " + e.getMessage());
        }
        System.out.println("Test change: " + (okChange ? "OK" : "FAIL"));
        log.log("Test change: " + (okChange ? "OK" : "FAIL"));

        // 5) delete
        db2.del(reader);
        boolean okDel = (count(db2) == 0);
        System.out.println("Test delete: " + (okDel ? "OK" : "FAIL"));
        log.log("Test delete: " + (okDel ? "OK" : "FAIL"));
    }

    /**
     * Подсчитывает количество студентов в БД через рефлексию.
     */
    private int count(StudentDB db) {
        try {
            var field = StudentDB.class.getDeclaredField("data");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<?, ?> map = (Map<?, ?>) field.get(db);
            return map.size();
        } catch (Exception e) {
            log.recordError("Reflect count error: " + e.getMessage());
            return -1;
        }
    }
}
