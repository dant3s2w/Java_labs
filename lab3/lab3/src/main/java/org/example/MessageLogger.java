// File: src/org/example/MessageLogger.java
package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Логгер операций add/remove и ошибок.
 * Может открывать файл как на перезапись, так и на дозапись.
 */
public class MessageLogger implements AutoCloseable {
    private final PrintWriter out;
    private int errorCount = 0;

    /**
     * Открывает лог-файл.
     * @param filename имя файла (например "ArrayList_100.log")
     * @param append   если true — дозапись (append), иначе — перезапись
     */
    public MessageLogger(String filename, boolean append) {
        try {
            out = new PrintWriter(new FileWriter(filename, append));
        } catch (IOException e) {
            throw new RuntimeException("Не могу открыть лог: " + filename, e);
        }
    }

    /**
     * Перезаписывающий конструктор (как раньше).
     */
    public MessageLogger(String filename) {
        this(filename, false);
    }

    public void logTime(String label) {
        String ts = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                .format(new Date());
        out.println(label + ": " + ts);
        out.flush();
    }

    public void log(String msg) {
        out.println(msg);
        out.flush();
    }

    public void recordError(String err) {
        errorCount++;
        log("ERROR: " + err);
    }

    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public void close() {
        out.close();
    }
}
