package org.example;

import java.io.*;
import java.util.Properties;

/**
 * Загружает и хранит настройки приложения из config.properties.
 * Параметры:
 *  username, password, group, debug, autotests
 */
public class Settings {
    public final String username;
    public final String password;
    public final String group;
    public final boolean debug;
    public final boolean autotests;

    private Settings(String u, String p, String g, boolean d, boolean a) {
        username  = u;
        password  = p;
        group     = g;
        debug     = d;
        autotests = a;
    }

    /**
     * Загружает настройки. Если файла нет, создаёт с дефолтами.
     *
     * @param filename путь к config.properties
     * @return объект Settings
     * @throws IOException при ошибке I/O
     */
    public static Settings load(String filename) throws IOException {
        Properties prop = new Properties();
        File file = new File(filename);
        if (!file.exists()) {
            // дефолтные настройки
            prop.setProperty("username",  "admin");
            prop.setProperty("password",  "admin");
            prop.setProperty("group",     "root");
            prop.setProperty("debug",     "true");
            prop.setProperty("autotests", "true");
            try (FileOutputStream out = new FileOutputStream(file)) {
                prop.store(out, "Application settings");
            }
        } else {
            try (FileInputStream in = new FileInputStream(file)) {
                prop.load(in);
            }
        }
        String u = prop.getProperty("username",  "admin");
        String p = prop.getProperty("password",  "admin");
        String g = prop.getProperty("group",     "user");
        boolean d = Boolean.parseBoolean(prop.getProperty("debug",     "false"));
        boolean a = Boolean.parseBoolean(prop.getProperty("autotests", "false"));
        return new Settings(u, p, g, d, a);
    }
}
