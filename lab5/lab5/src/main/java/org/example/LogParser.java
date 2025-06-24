// File: src/org/example/LogParser.java
package org.example;

import java.io.*;
import java.util.*;

/**
 * Читает лог-файлы и извлекает из них ключевые метрики:
 * addTotalTime, addMedianTime, removeTotalTime, removeMedianTime.
 */
public class LogParser {
    /**
     * Парсит для одной коллекции все файлы логов по заданным размерам.
     *
     * @param name  префикс файла (ArrayList или LinkedList)
     * @param sizes массив размеров
     * @return серия метрик для этой коллекции
     */
    public static MetricsSeries parse(String name, int[] sizes) {
        MetricsSeries series = new MetricsSeries(name);
        for (int n : sizes) {
            String fname = String.format("%s_%d.log", name, n);
            Metrics m = new Metrics(n);
            try (BufferedReader r = new BufferedReader(new FileReader(fname))) {
                String line;
                while ((line = r.readLine()) != null) {
                    if (line.startsWith("addTotalTime")) {
                        m.addTotal = Long.parseLong(line.split("=\\s*")[1]);
                    } else if (line.startsWith("addMedianTime")) {
                        m.addMedian = Long.parseLong(line.split("=\\s*")[1]);
                    } else if (line.startsWith("removeTotalTime")) {
                        m.removeTotal = Long.parseLong(line.split("=\\s*")[1]);
                    } else if (line.startsWith("removeMedianTime")) {
                        m.removeMedian = Long.parseLong(line.split("=\\s*")[1]);
                    }
                }
            } catch (IOException e) {
                System.err.printf("Ошибка чтения %s: %s%n", fname, e.getMessage());
            }
            series.add(m);
        }
        return series;
    }
}
