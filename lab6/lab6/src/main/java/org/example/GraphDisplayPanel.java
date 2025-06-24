package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Панель для отображения графиков.
 * Отображает один или несколько графиков в зависимости от режима overlay.
 * Использует данные из модели {@link GraphModel}, каждый график — это {@link DataFile}.
 */

public class GraphDisplayPanel extends JPanel {
    /** Модель, содержащая загруженные данные и настройки. */
    private final GraphModel model;
    /**
     * Конструктор панели. Подключается к модели и автоматически
     * вызывает перерисовку при её изменении.
     *
     * @param model модель с данными и настройками отображения
     */
    public GraphDisplayPanel(GraphModel model) {
        this.model = model;
        model.addChangeListener(this::repaint);
    }

    /**
     * Метод отрисовки. Вызывается автоматически при перерисовке панели.
     * В зависимости от режима overlay рисует либо один активный график, либо все сразу.
     *
     * @param g графический контекст
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        List<DataFile> files = model.getFiles();
        if (files.isEmpty()) return;

        if (model.isOverlay()) {
            for (DataFile df : files) {
                drawSeries(g2, df);
            }
        } else {
            DataFile df = files.get(model.getCurrentIndex());
            drawSeries(g2, df);
        }
    }

    /**
     * Отрисовка одного графика по данным.
     *
     * @param g2 графический контекст
     * @param df данные и настройки одного файла
     */
    private void drawSeries(Graphics2D g2, DataFile df) {
        List<Metrics> list = df.series.list;
        if (list.isEmpty()) return;
        int n = list.size();
        int w = getWidth(), h = getHeight(), m = 50;

        g2.setColor(Color.BLACK);
        g2.drawString(df.title, m, m - 20);
        g2.drawString(df.xLabel, w / 2, h - m + 40);
        g2.drawString(df.yLabel, m - 40, h / 2);

        g2.drawLine(m, m, m, h - m);
        g2.drawLine(m, h - m, w - m, h - m);

        double minX = df.logX ? Math.log(1) : 1;
        double maxX = df.logX ? Math.log(n) : n;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            Metrics mt = list.get(i);
            double Y = df.logY ? Math.log(mt.addTotal) : mt.addTotal;
            minY = Math.min(minY, Y);
            maxY = Math.max(maxY, Y);
        }

        g2.setColor(df.color);
        Point prev = null;
        for (int i = 0; i < n; i++) {
            Metrics mt = list.get(i);
            double X = df.logX ? Math.log(i + 1) : (i + 1);
            double Y = df.logY ? Math.log(mt.addTotal) : mt.addTotal;

            int x = m + (int) ((X - minX) / (maxX - minX) * (w - 2 * m));
            int y = m + (int) ((maxY - Y) / (maxY - minY) * (h - 2 * m));

            if (prev != null) {
                g2.drawLine(prev.x, prev.y, x, y);
            }
            prev = new Point(x, y);
        }
    }
}