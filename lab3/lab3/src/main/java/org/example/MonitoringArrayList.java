// File: src/org/example/MonitoringArrayList.java
package org.example;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * ArrayList, детектирующий и логирующий переразмерения внутреннего массива.
 *
 * @param <E> элемент списка
 */
public class MonitoringArrayList<E> extends ArrayList<E> {
    private final MessageLogger logger;
    private int lastCapacity;

    /**
     * @param logger логгер для записи resize-событий
     */
    public MonitoringArrayList(MessageLogger logger) {
        super();
        this.logger = logger;
        this.lastCapacity = fetchCapacity();
    }

    @Override
    public boolean add(E e) {
        boolean res = super.add(e);
        checkResize();
        return res;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        checkResize();
    }

    /** Читает текущее значение capacity через рефлексию. */
    private int fetchCapacity() {
        try {
            Field fld = ArrayList.class.getDeclaredField("elementData");
            fld.setAccessible(true);
            Object[] data = (Object[]) fld.get(this);
            return data.length;
        } catch (Exception ex) {
            return -1;
        }
    }

    /** Логирует, если capacity вырос. */
    private void checkResize() {
        int cap = fetchCapacity();
        if (cap > lastCapacity) {
            logger.log(String.format(
                    "resize, oldCap = %d, newCap = %d",
                    lastCapacity, cap));
            lastCapacity = cap;
        }
    }
}
