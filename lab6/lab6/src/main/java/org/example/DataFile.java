package org.example;

import java.awt.*;

/**
 * Класс DataFile представляет собой модель одного загруженного файла с данными.
 * Хранит:
 * - путь к файлу,
 * - данные графика (в виде MetricsSeries),
 * - настройки отображения графика: цвет, логарифмический масштаб, подписи осей и заголовок.
 */

public class DataFile {
    public final String path;
    public final MetricsSeries series;
    public Color color = Color.RED;
    public boolean logX = false, logY = false;
    public String title = "График";
    public String xLabel = "Размер коллекции";
    public String yLabel = "Время (нс)";

    /**
     * Конструктор модели данных.
     *
     * @param path путь к файлу, отображаемый в списке и как идентификатор
     * @param series данные, извлечённые из файла, в виде списка метрик
     */
    public DataFile(String path, MetricsSeries series) {
        this.path = path;
        this.series = series;
    }
}