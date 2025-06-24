package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс MetricsSeries представляет собой серию метрик,
 * соответствующую одному источнику данных (например, одному файлу логов).
 *
 * Каждая серия содержит список объектов {@link Metrics},
 * каждый из которых соответствует определённому размеру коллекции и замеру времени.
 */
public class MetricsSeries {
    public final String name;
    public final List<Metrics> list = new ArrayList<>();

    /**
     * Конструктор.
     *
     * @param name имя серии (обычно имя файла)
     */
    public MetricsSeries(String name) {
        this.name = name;
    }

    /**
     * Добавляет новую метрику в серию.
     *
     * @param m объект {@link Metrics}, содержащий данные по одному размеру
     */
    public void add(Metrics m) {
        list.add(m);
    }
}