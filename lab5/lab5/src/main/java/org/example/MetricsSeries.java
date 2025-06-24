// File: src/org/example/MetricsSeries.java
package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Набор метрик для одной коллекции.
 * Содержит список Metrics для каждого размера.
 */
public class MetricsSeries {
    public final String name;
    public final List<Metrics> list = new ArrayList<>();

    public MetricsSeries(String name) {
        this.name = name;
    }

    /** Добавляет новую метрику (для одного размера). */
    public void add(Metrics m) {
        list.add(m);
    }
}
