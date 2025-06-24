package org.example;

import javax.swing.*;

/**
 * Главный класс приложения.
 * Лабораторная работа №6: Построение графиков по логам лабораторной работы №5.
 * <p>
 * Интерфейс позволяет:
 * <ul>
 *     <li>Загружать до 5 файлов с логами</li>
 *     <li>Настраивать отображение графиков (цвет, масштаб, подписи)</li>
 *     <li>Выбирать режим отображения: совмещение графиков или поочерёдное отображение</li>
 * </ul>
 */

public class Main6App {

    /**
     * Точка входа в программу.
     * Запускает Swing-приложение в потоке Event Dispatch Thread (EDT).
     *
     * @param args аргументы командной строки (не используются)
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ЛР6: Построение графиков");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new java.awt.BorderLayout());

            GraphManagerPanel manager = new GraphManagerPanel();
            GraphDisplayPanel display = new GraphDisplayPanel(manager.getModel());
            manager.setDisplayPanel(display);

            frame.add(manager, java.awt.BorderLayout.WEST);
            frame.add(display, java.awt.BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}