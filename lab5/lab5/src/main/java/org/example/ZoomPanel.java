package org.example;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Map;

/**
 * Панель с диапазонами по осям X и Y.
 * Слайдер X задаёт, сколько точек (первых N) отображать.
 * Слайдер Y задаёт, до какого процента от максимального значения по Y рисовать.
 */
public class ZoomPanel extends JPanel {
    private final GraphPanel graph;
    private final JSlider sliderX;
    private final JSlider sliderY;

    /**
     * @param title  заголовок рамки
     * @param data   данные (серии метрик)
     * @param sizes  массив размеров коллекций
     * @param median true — медианное время, false — суммарное
     */
    public ZoomPanel(String title,
                     Map<String, MetricsSeries> data,
                     int[] sizes,
                     boolean median) {

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(title));

        // сам график
        graph = new GraphPanel(data, sizes, median);
        add(graph, BorderLayout.CENTER);

        // сколько точек по X (1..sizes.length), старт — все точки
        sliderX = new JSlider(1, sizes.length, sizes.length);
        sliderX.setBorder(BorderFactory.createTitledBorder("Диапазон X (точек)"));

        // какое % от Y‑макс отображать (0..100%), старт — 8%
        sliderY = new JSlider(0, 100, 8);
        sliderY.setBorder(BorderFactory.createTitledBorder("Диапазон Y (%)"));

        // слушатель, обновляющий график при движении ползунков
        ChangeListener cl = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int xCount = sliderX.getValue();
                double yPct = sliderY.getValue() / 100.0;
                graph.setRanges(xCount, yPct);
            }
        };
        sliderX.addChangeListener(cl);
        sliderY.addChangeListener(cl);

        // панель с ползунками
        JPanel controls = new JPanel(new GridLayout(1, 2, 10, 0));
        controls.add(sliderX);
        controls.add(sliderY);
        add(controls, BorderLayout.SOUTH);

        // применяем стартовые значения к графику сразу
        graph.setRanges(sliderX.getValue(), sliderY.getValue() / 100.0);
    }
}
