// File: src/org/example/Lab5App.java
package org.example;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Основной класс ЛР№5.
 * Разбирает логи ЛР№3 и строит графики производительности коллекций.
 */
public class Lab5App {
    /** Размеры коллекций для анализа. */
    private static final int[] SIZES = {10, 100, 1000, 10000, 100000};
    /** Имена коллекций (префикс файлов логов). */
    private static final String[] COLLECTIONS = {"ArrayList", "LinkedList"};

    public static void main(String[] args) {
        // Парсим логи и собираем серии метрик
        Map<String, MetricsSeries> seriesMap = new LinkedHashMap<>();
        for (String name : COLLECTIONS) {
            MetricsSeries ms = LogParser.parse(name, SIZES);
            seriesMap.put(name, ms);
        }

        // Запускаем GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(seriesMap, SIZES);
            frame.setVisible(true);
        });
    }
}
