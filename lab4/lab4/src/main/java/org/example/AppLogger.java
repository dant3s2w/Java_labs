package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Логгер для приложения. Всегда дозаписывает в файл app.log.
 * Метки времени: yyyy-MM-dd HH:mm:ss
 */
public class AppLogger implements AutoCloseable {
    private final PrintWriter out;

    /**
     * Открывает файл лога на дозапись.
     * @param filename имя файла (app.log)
     * @throws IOException при ошибке I/O
     */
    public AppLogger(String filename) throws IOException {
        this.out = new PrintWriter(new FileWriter(filename, true), true);
    }

    /**
     * Запись сообщения с меткой времени.
     * @param msg текст
     */
    public void log(String msg) {
        String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        out.println(ts + " " + msg);
    }

    /**
     * Запись ошибки.
     * @param err текст ошибки
     */
    public void recordError(String err) {
        log("ERROR: " + err);
    }

    @Override
    public void close() {
        out.close();
    }
}
