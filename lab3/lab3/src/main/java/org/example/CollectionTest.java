package org.example;

import java.util.*;
import java.util.function.Supplier;

/**
 * Проверяет работу списка студентов.
 * Заполняет список объектами Student, затем удаляет часть элементов,
 * фиксирует время работы операций и возвращает итоговые данные.
 *
 * @param <C> тип списка, реализующий List<Student>
 */
public class CollectionTest<C extends List<Student>> {
    private final Supplier<C> factory;
    private final String tag;
    private final int count;
    private final RandomStudentGenerator gen = new RandomStudentGenerator();

    /**
     * Создаёт тест для списка студентов.
     *
     * @param factory поставщик пустого списка нужного типа
     * @param tag     метка для логирования операций
     * @param count   число студентов для добавления
     */
    public CollectionTest(Supplier<C> factory, String tag, int count) {
        this.factory = factory;
        this.tag = tag;
        this.count = count;
    }

    /**
     * Запускает тест с логированием.
     * <ol>
     *   <li>Логирует время старта и метку.</li>
     *   <li>Добавляет заданное число студентов, фиксируя время каждой операции.</li>
     *   <li>Удаляет 10% элементов случайным образом, фиксируя время каждой операции.</li>
     *   <li>Вычисляет общее и медианное время добавления и удаления.</li>
     *   <li>Записывает итоговые метрики в лог.</li>
     *   <li>Закрывает логгер и возвращает объект с результатами.</li>
     * </ol>
     *
     * @param logger объект для записи сообщений в файл
     * @return результат теста с метриками по добавлению и удалению
     */
    public TestResult runWithLogger(MessageLogger logger) {
        logger.logTime("Start program");
        logger.log(tag);

        C list = factory.get();
        List<Long> addTimes = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            Student s = gen.next();
            long t0 = System.nanoTime();
            try {
                list.add(s);
            } catch (Exception e) {
                logger.recordError(e.getMessage());
            }
            long dt = System.nanoTime() - t0;
            addTimes.add(dt);
            logger.log(String.format("add, ID = %d, %d", i + 1, dt));
        }

        int remCnt = count / 10;
        List<Long> remTimes = new ArrayList<>(remCnt);
        Random rnd = new Random();
        for (int i = 0; i < remCnt; i++) {
            int idx = rnd.nextInt(list.size());
            long t0 = System.nanoTime();
            try {
                list.remove(idx);
            } catch (Exception e) {
                logger.recordError(e.getMessage());
            }
            long dt = System.nanoTime() - t0;
            remTimes.add(dt);
            logger.log(String.format("remove, ID = %d, %d", idx + 1, dt));
        }

        long addTotal = addTimes.stream().mapToLong(Long::longValue).sum();
        long addMedian = median(addTimes);
        long remTotal = remTimes.stream().mapToLong(Long::longValue).sum();
        long remMedian = median(remTimes);

        logger.log(String.format("addTotalCount = %d", addTimes.size()));
        logger.log(String.format("addTotalTime = %d", addTotal));
        logger.log(String.format("addMedianTime = %d", addMedian));

        logger.log(String.format("removeTotalCount = %d", remTimes.size()));
        logger.log(String.format("removeTotalTime = %d", remTotal));
        logger.log(String.format("removeMedianTime = %d", remMedian));

        logger.logTime("Finish program");
        if (logger.getErrorCount() > 0) {
            logger.log("Errors total: " + logger.getErrorCount());
        }
        logger.close();

        return new TestResult(count, addTotal, addMedian, remTotal, remMedian);
    }

    /**
     * Вычисляет медиану списка значений.
     *
     * @param list список чисел (время операций)
     * @return медиана или 0, если список пуст
     */
    private long median(List<Long> list) {
        if (list.isEmpty()) {
            return 0;
        }
        Collections.sort(list);
        int m = list.size() / 2;
        if (list.size() % 2 == 1) {
            return list.get(m);
        } else {
            return (list.get(m - 1) + list.get(m)) / 2;
        }
    }

    /**
     * Хранит результаты одного прогона теста.
     */
    public static class TestResult {
        public final int size;
        public final long addTotal;
        public final long addMedian;
        public final long removeTotal;
        public final long removeMedian;

        /**
         * Создаёт объект с итогами.
         *
         * @param size         число добавленных элементов
         * @param addTotal     общее время добавления
         * @param addMedian    медианное время добавления
         * @param removeTotal  общее время удаления
         * @param removeMedian медианное время удаления
         */
        public TestResult(int size, long addTotal, long addMedian,
                          long removeTotal, long removeMedian) {
            this.size = size;
            this.addTotal = addTotal;
            this.addMedian = addMedian;
            this.removeTotal = removeTotal;
            this.removeMedian = removeMedian;
        }
    }
}
