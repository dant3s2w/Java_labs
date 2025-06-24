// File: src/org/example/GraphManagerPanel.java
package org.example;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Панель слева: загрузка/удаление файлов, выбор настроек.
 */
public class GraphManagerPanel extends JPanel {
    private final GraphModel model = new GraphModel();
    private GraphDisplayPanel display;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> fileList = new JList<>(listModel);

    // Слушатель выбора элемента списка
    private final ListSelectionListener listSelListener = e -> {
        if (e.getValueIsAdjusting()) return;
        model.setCurrentIndex(fileList.getSelectedIndex());
        updateFields();
    };

    public GraphManagerPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 0));

        // Список файлов
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.addListSelectionListener(listSelListener);

        // Кнопки загрузки/удаления
        JButton loadBtn = new JButton("Загрузить файл");
        loadBtn.addActionListener(e -> onLoad());
        JButton removeBtn = new JButton("Удалить файл");
        removeBtn.addActionListener(e -> onRemove());

        JPanel top = new JPanel(new FlowLayout());
        top.add(loadBtn);
        top.add(removeBtn);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(fileList), BorderLayout.CENTER);

        // Панель настроек
        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));

        // Оверлей / Перекл.
        JCheckBox overlayCb = new JCheckBox("Наложение графиков", true);
        overlayCb.addActionListener(e -> model.setOverlay(overlayCb.isSelected()));
        settings.add(overlayCb);

        // Логарифмический масштаб
        JCheckBox logX = new JCheckBox("log X");
        JCheckBox logY = new JCheckBox("log Y");
        settings.add(logX);
        settings.add(logY);

        // Цвет
        JButton colorBtn = new JButton("Выбрать цвет");
        settings.add(colorBtn);

        // Подписи
        JTextField titleField = new JTextField("Заголовок");
        JTextField xLabelField = new JTextField("X");
        JTextField yLabelField = new JTextField("Y");
        settings.add(new JLabel("Заголовок:"));
        settings.add(titleField);
        settings.add(new JLabel("Метка X:"));
        settings.add(xLabelField);
        settings.add(new JLabel("Метка Y:"));
        settings.add(yLabelField);

        // Навигация в режиме перерисовки
        JButton prevBtn = new JButton("<< Предыдущий");
        JButton nextBtn = new JButton("Следующий >>");
        settings.add(prevBtn);
        settings.add(nextBtn);

        add(settings, BorderLayout.SOUTH);

        // Слушаем модель — обновляем список без рекурсии
        model.addChangeListener(() -> {
            // убираем слушатель, чтобы не зациклиться
            fileList.removeListSelectionListener(listSelListener);

            listModel.clear();
            for (DataFile df : model.getFiles()) {
                listModel.addElement(df.path);
            }
            fileList.setSelectedIndex(model.getCurrentIndex());

            // возвращаем слушатель
            fileList.addListSelectionListener(listSelListener);
        });

        // Обработчики полей
        colorBtn.addActionListener(e -> {
            DataFile df = getCurrent();
            if (df == null) return;
            Color c = JColorChooser.showDialog(this, "Цвет графика", df.color);
            if (c != null) {
                df.color = c;
                model.fireChange();
            }
        });
        logX.addActionListener(e -> {
            DataFile df = getCurrent();
            if (df != null) {
                df.logX = logX.isSelected();
                model.fireChange();
            }
        });
        logY.addActionListener(e -> {
            DataFile df = getCurrent();
            if (df != null) {
                df.logY = logY.isSelected();
                model.fireChange();
            }
        });
        titleField.getDocument().addDocumentListener(new SimpleDocListener(() -> {
            DataFile df = getCurrent();
            if (df != null) {
                df.title = titleField.getText();
                model.fireChange();
            }
        }));
        xLabelField.getDocument().addDocumentListener(new SimpleDocListener(() -> {
            DataFile df = getCurrent();
            if (df != null) {
                df.xLabel = xLabelField.getText();
                model.fireChange();
            }
        }));
        yLabelField.getDocument().addDocumentListener(new SimpleDocListener(() -> {
            DataFile df = getCurrent();
            if (df != null) {
                df.yLabel = yLabelField.getText();
                model.fireChange();
            }
        }));
        prevBtn.addActionListener(e -> model.setCurrentIndex(model.getCurrentIndex() - 1));
        nextBtn.addActionListener(e -> model.setCurrentIndex(model.getCurrentIndex() + 1));
    }

    /** Устанавливается из Main6App. */
    public void setDisplayPanel(GraphDisplayPanel dp) {
        this.display = dp;
    }

    public GraphModel getModel() {
        return model;
    }

    private void onLoad() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            MetricsSeries ms = LogParser.parse(f.getAbsolutePath());
            DataFile df = new DataFile(f.getName(), ms);
            if (!model.addFile(df)) {
                JOptionPane.showMessageDialog(this, "Можно загрузить не более 5 файлов");
            }
        }
    }

    private void onRemove() {
        int idx = fileList.getSelectedIndex();
        if (idx >= 0) model.removeFile(idx);
    }

    private DataFile getCurrent() {
        int idx = model.getCurrentIndex();
        return idx >= 0 && idx < model.getFiles().size()
                ? model.getFiles().get(idx)
                : null;
    }

    /** Устанавливаем поля из текущего DataFile. */
    private void updateFields() {
        DataFile df = getCurrent();
        // TODO: синхронизовать состояние полей (логические флаги, цвет, тексты) с df
    }

    /** Вспомогательный слушатель изменений документа. */
    private class SimpleDocListener implements javax.swing.event.DocumentListener {
        private final Runnable action;
        SimpleDocListener(Runnable action) {
            this.action = action;
        }
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            action.run();
        }
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            action.run();
        }
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            action.run();
        }
    }
}
