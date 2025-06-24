package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Основной класс Лабораторной работы №4.
 * Реализует ввод-вывод, работу с файлами настроек и базой данных.
 */
public class Lab4App {
    public static final String CONFIG_FILE = "config.properties";
    public static final String DB_FILE     = "students.db";
    public static final String LOG_FILE    = "app.log";

    public static void main(String[] args) {
        try {
            // Загружаем настройки
            Settings cfg = Settings.load(CONFIG_FILE);
            // Открываем лог в режиме дозаписи
            AppLogger log = new AppLogger(LOG_FILE);
            log.log("Программа запущена пользователем " + cfg.username);

            // Автотесты при старте, если включены
            if (cfg.autotests) {
                AutoTester tester = new AutoTester(log, DB_FILE);
                tester.runAll();
            }

            // Вход в систему
            if (!login(cfg, log)) {
                log.log("Выход: неверный логин/пароль");
                log.close();
                return;
            }
            System.out.println("Добро пожаловать — " + cfg.username);
            if (cfg.debug) {
                log.log("Режим отладки включен");
            }

            // Работа с базой студентов
            StudentDB db = new StudentDB(DB_FILE, log);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            boolean exit = false;

            while (!exit) {
                printMenu(cfg);
                String sel = in.readLine();
                switch (sel) {
                    case "1": db.load();     break;
                    case "2": db.save();     break;
                    case "3": db.listAll();  break;
                    case "4": db.put(in);    break;
                    case "5": db.change(in); break;
                    case "6": db.del(in);    break;
                    case "7":
                        if ("root".equals(cfg.group)) {
                            debugMenu(log);
                        }
                        break;
                    case "8":
                        if ("root".equals(cfg.group)) {
                            runAutotests(log);
                        }
                        break;
                    case "0": exit = true;     break;
                    default: System.out.println("Неверный выбор");
                }
            }

            log.log("Программа завершена пользователем " + cfg.username);
            log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean login(Settings cfg, AppLogger log) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Логин: ");
        String u = in.readLine();
        System.out.print("Пароль: ");
        String p = in.readLine();
        if (u.equals(cfg.username) && p.equals(cfg.password)) {
            log.log("Успешный вход: " + u);
            return true;
        }
        System.out.println("Неверный логин или пароль");
        log.log("Неудачная попытка входа: " + u);
        return false;
    }

    /**
     * Вывод главного меню на русском языке.
     */
    private static void printMenu(Settings cfg) {
        System.out.println("\nМеню:");
        System.out.println("1. Загрузить базу данных");
        System.out.println("2. Сохранить базу данных");
        System.out.println("3. Показать всех студентов");
        System.out.println("4. Добавить студента");
        System.out.println("5. Изменить данные студента");
        System.out.println("6. Удалить студента");
        int num = 7;
        if ("root".equals(cfg.group)) {
            System.out.println(num++ + ". Отладка");
            System.out.println(num++ + ". Автотесты");
        }
        System.out.println("0. Выход");
        System.out.print("Выбор: ");
    }

    /**
     * Меню отладки (для root).
     */
    private static void debugMenu(AppLogger log) {
        System.out.println("-- Отладочная информация --");
        log.log("Пункт меню: Отладка");
        System.out.println("Лог пишется в файл " + LOG_FILE);
    }

    /**
     * Запуск автотестов из меню (для root).
     */
    private static void runAutotests(AppLogger log) {
        System.out.println("-- Запуск автотестов --");
        log.log("Пункт меню: Автотесты");
        AutoTester tester = new AutoTester(log, DB_FILE);
        tester.runAll();
    }
}
