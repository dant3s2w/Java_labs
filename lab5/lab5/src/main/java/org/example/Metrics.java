// File: src/org/example/Metrics.java
package org.example;

/**
 * Метрики для одного размера коллекции:
 * общее и медианное время операций add и remove.
 */
public class Metrics {
    public final int size;
    public long addTotal, addMedian;
    public long removeTotal, removeMedian;

    public Metrics(int size) {
        this.size = size;
    }
}
