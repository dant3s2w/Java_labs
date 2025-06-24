// File: src/org/example/MainFrame.java
package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Главное окно: два ZoomPanel — по одному на медиану и суммарное время.
 */
public class MainFrame extends JFrame {
    public MainFrame(Map<String, MetricsSeries> seriesMap, int[] sizes) {
        super("ЛР №5: Производительность коллекций (с зумом)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new GridLayout(2,1));

        // ZoomPanel(title, data, sizes, medianFlag)
        add(new ZoomPanel("Медианное время (нс)", seriesMap, sizes, true));
        add(new ZoomPanel("Суммарное время (нс)", seriesMap, sizes, false));
    }
}
