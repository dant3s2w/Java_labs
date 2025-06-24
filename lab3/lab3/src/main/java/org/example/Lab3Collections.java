package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Запускает проверку коллекций ArrayList и LinkedList.
 * Автоматически собирает результаты и считает регресс.
 */
public class Lab3Collections {
    private static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000};

    /**
     * Точка входа в лабораторную работу №3.
     * Последовательно тестирует список ArrayList и LinkedList
     * для разных размеров.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        // ArrayList с мониторингом и сбором результатов
        List<CollectionTest.TestResult> arrayResults = new ArrayList<>();
        for (int n : SIZES) {
            String fname = String.format("ArrayList_%d.log", n);
            MessageLogger logger = new MessageLogger(fname, false);
            CollectionTest<MonitoringArrayList<Student>> test =
                    new CollectionTest<>(() -> new MonitoringArrayList<>(logger),
                            "ArrayList", n);
            arrayResults.add(test.runWithLogger(logger));
        }
        writeRegression("ArrayList", arrayResults);

        // LinkedList
        List<CollectionTest.TestResult> listResults = new ArrayList<>();
        for (int n : SIZES) {
            String fname = String.format("LinkedList_%d.log", n);
            MessageLogger logger = new MessageLogger(fname, false);
            CollectionTest<LinkedList<Student>> test =
                    new CollectionTest<>(LinkedList::new, "LinkedList", n);
            listResults.add(test.runWithLogger(logger));
        }
        writeRegression("LinkedList", listResults);
    }

    /**
     * Вычисляет и логирует проценты изменения метрик между запусками.
     * Для каждого размера > первого добавляет записи в лог.
     *
     * @param tag     метка списка (ArrayList или LinkedList)
     * @param results список результатов тестов по возрастанию размера
     */
    private static void writeRegression(
            String tag,
            List<CollectionTest.TestResult> results
    ) {
        for (int i = 1; i < results.size(); i++) {
            var prev = results.get(i - 1);
            var curr = results.get(i);
            double pctAdd = pct(curr.addTotal, prev.addTotal);
            double pctAddMed = pct(curr.addMedian, prev.addMedian);
            double pctRem = pct(curr.removeTotal, prev.removeTotal);
            double pctRemMed = pct(curr.removeMedian, prev.removeMedian);

            String fname = String.format("%s_%d.log", tag, curr.size);
            // открываем в режиме append, чтобы дописать данные
            try (MessageLogger log = new MessageLogger(fname, true)) {
                log.log("");  // разделитель между записями
                log.log(String.format(
                        "Regression addTotalTime %d→%d: %+,.1f%%",
                        prev.size, curr.size, pctAdd));
                log.log(String.format(
                        "Regression addMedianTime %d→%d: %+,.1f%%",
                        prev.size, curr.size, pctAddMed));
                log.log(String.format(
                        "Regression removeTotalTime %d→%d: %+,.1f%%",
                        prev.size, curr.size, pctRem));
                log.log(String.format(
                        "Regression removeMedianTime %d→%d: %+,.1f%%",
                        prev.size, curr.size, pctRemMed));
            }
        }
    }

    /**
     * Считает процент изменения между текущим и предыдущим значением.
     * Если предыдущее значение равно нулю, возвращает 0.
     *
     * @param curr текущее значение
     * @param prev предыдущее значение
     * @return процент роста или падения
     */
    private static double pct(long curr, long prev) {
        return prev == 0 ? 0 : (double)(curr - prev) / prev * 100.0;
    }
}
