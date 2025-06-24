package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Панель, рисующая ломаные графики,
 * отображает и add, и remove зависимости.
 * Поддерживает обрезку по X и Y.
 */
public class GraphPanel extends JPanel {
    private final Map<String, MetricsSeries> seriesMap;
    private final int[] sizes;
    private final boolean median;

    private int xCount;       // сколько точек выводить
    private double yFraction; // какой % от maxY показывать

    public GraphPanel(Map<String, MetricsSeries> seriesMap,
                      int[] sizes,
                      boolean median) {
        this.seriesMap = seriesMap;
        this.sizes     = sizes;
        this.median    = median;
        this.xCount    = sizes.length;
        this.yFraction = 1.0;
    }

    /**
     * Устанавливает новые диапазоны и перерисовывает.
     * @param xCount    число точек по X (1..sizes.length)
     * @param yFraction доля от полного maxY (0..1)
     */
    public void setRanges(int xCount, double yFraction) {
        this.xCount    = xCount;
        this.yFraction = yFraction;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth(), h = getHeight(), m = 50;

        // заголовок
        g2.drawString(median ? "Медианное время" : "Суммарное время", m, m-10);

        // оси
        g2.drawLine(m, h-m, w-m, h-m); // X
        g2.drawLine(m, m, m, h-m);     // Y

        // вычисляем глобальный максимум Y
        long globalMaxY = 0;
        for (MetricsSeries ms : seriesMap.values()) {
            for (Metrics mt : ms.list) {
                long v1 = median ? mt.addMedian    : mt.addTotal;
                long v2 = median ? mt.removeMedian : mt.removeTotal;
                globalMaxY = Math.max(globalMaxY, Math.max(v1, v2));
            }
        }
        long maxY = (long)(globalMaxY * yFraction);

        // метки X (до xCount)
        for (int i = 0; i < xCount; i++) {
            int x = m + i * (w - 2*m) / (sizes.length - 1);
            g2.drawLine(x, h-m, x, h-m+5);
            g2.drawString(String.valueOf(sizes[i]), x-15, h-m+20);
        }

        // метки Y
        int steps = 5;
        for (int i = 0; i <= steps; i++) {
            int y = m + i*(h - 2*m)/steps;
            long label = maxY - i*(maxY/steps);
            g2.drawLine(m-5, y, m, y);
            g2.drawString(String.valueOf(label), 5, y+5);
        }

        // готовим стили для add и remove
        Color[] cols = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};
        Stroke defaultStroke = g2.getStroke();
        BasicStroke addStroke = new BasicStroke(2f);
        BasicStroke remStroke = new BasicStroke(
                2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                1f, new float[]{5f,5f}, 0f
        );

        // рисуем ломаные для add и remove
        int ci = 0;
        for (Map.Entry<String, MetricsSeries> entry : seriesMap.entrySet()) {
            String name = entry.getKey();
            MetricsSeries ms = entry.getValue();
            Color addColor = cols[ci % cols.length];
            Color remColor = addColor.darker();

            // add линия
            g2.setColor(addColor);
            g2.setStroke(addStroke);
            Point prevAdd = null;
            for (int i = 0; i < xCount; i++) {
                Metrics mt = ms.list.get(i);
                int x = m + i * (w - 2*m) / (sizes.length - 1);
                long val = median ? mt.addMedian : mt.addTotal;
                int y = (int)(m + (maxY - Math.min(val, maxY)) * (h - 2*m) / (double)maxY);
                if (prevAdd != null) {
                    g2.drawLine(prevAdd.x, prevAdd.y, x, y);
                }
                prevAdd = new Point(x, y);
            }

            // remove линия
            g2.setColor(remColor);
            g2.setStroke(remStroke);
            Point prevRem = null;
            for (int i = 0; i < xCount; i++) {
                Metrics mt = ms.list.get(i);
                int x = m + i * (w - 2*m) / (sizes.length - 1);
                long val = median ? mt.removeMedian : mt.removeTotal;
                int y = (int)(m + (maxY - Math.min(val, maxY)) * (h - 2*m) / (double)maxY);
                if (prevRem != null) {
                    g2.drawLine(prevRem.x, prevRem.y, x, y);
                }
                prevRem = new Point(x, y);
            }

            // легенда
            g2.setStroke(defaultStroke);
            g2.setColor(addColor);
            g2.drawString(name + " add", w-m-120, m + 20*ci);
            g2.setColor(remColor);
            g2.drawString(name + " rem", w-m-120, m + 20*ci + 12);

            ci++;
        }
    }
}