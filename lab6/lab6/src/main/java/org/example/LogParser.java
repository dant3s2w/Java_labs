package org.example;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Класс LogParser предназначен для чтения логов, полученных в лабораторной работе №5.
 * Лог содержит строки вида:
 * <pre>
 *     add,id=100,time=25106
 * </pre>
 * Парсер извлекает значение id (размер коллекции) и время выполнения операции add,
 * записывает их в объект {@link Metrics}, а затем добавляет в {@link MetricsSeries}.
 */

public class LogParser {
    /**
     * Читает лог-файл и формирует серию метрик на основе операций add.
     *
     * @param filepath путь к файлу лога
     * @return объект MetricsSeries, содержащий список метрик
     */
    public static MetricsSeries parse(String filepath) {
        MetricsSeries series = new MetricsSeries(filepath);
        Map<Integer, Long> addMap = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("add,")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        int id = Integer.parseInt(parts[1].split("=")[1].trim());
                        long time = Long.parseLong(parts[2].trim());
                        addMap.put(id, time);
                    }
                }
            }

            for (Map.Entry<Integer, Long> entry : addMap.entrySet()) {
                Metrics m = new Metrics(entry.getKey());
                m.addTotal = entry.getValue();
                series.add(m);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при чтении лога:\n" + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        return series;
    }
}