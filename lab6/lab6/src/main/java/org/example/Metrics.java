package org.example;

/**
 * Класс, представляющий метрику для одного замера (одного размера коллекции).
 * Используется для хранения времени операций добавления и удаления.
 * <p>
 * Значения могут быть как суммарными, так и медианными:
 * <ul>
 *     <li>{@code addTotal} — суммарное время добавления элементов</li>
 *     <li>{@code addMedian} — медианное время добавления</li>
 *     <li>{@code removeTotal} — суммарное время удаления элементов</li>
 *     <li>{@code removeMedian} — медианное время удаления</li>
 * </ul>
 */

public class Metrics {
    public final int size;
    public long addTotal, addMedian, removeTotal, removeMedian;

    /**
     * Конструктор.
     *
     * @param size размер коллекции, по которому записана метрика
     */
    public Metrics(int size) {
        this.size = size;
    }
}