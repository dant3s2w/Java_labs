package org.example;

import java.util.*;

/**
 * Класс GraphModel представляет собой модель данных для построения графиков.
 * Содержит список загруженных файлов с данными и настройки отображения.
 * Также реализует механизм подписки на изменения (паттерн Observer).
 */

public class GraphModel {
    private final List<DataFile> files = new ArrayList<>();
    private final List<ChangeListener> listeners = new ArrayList<>();
    private boolean overlay = true;
    private int currentIndex = 0;

    /**
     * Добавляет новый файл в модель.
     *
     * @param file объект DataFile с данными графика
     * @return true — если добавлен успешно, false — если превышен лимит (5 файлов)
     */

    public boolean addFile(DataFile file) {
        if (files.size() >= 5) return false;
        files.add(file);
        currentIndex = files.size() - 1;
        fireChange();
        return true;
    }

    /**
     * Удаляет файл по индексу.
     *
     * @param idx индекс файла в списке
     */
    public void removeFile(int idx) {
        if (idx < 0 || idx >= files.size()) return;
        files.remove(idx);
        currentIndex = Math.max(0, Math.min(currentIndex, files.size()-1));
        fireChange();
    }

    /**
     * Получает список всех загруженных файлов (только для чтения).
     *
     * @return неизменяемый список DataFile
     */
    public List<DataFile> getFiles() { return Collections.unmodifiableList(files); }

    public boolean isOverlay() { return overlay; }
    /**
     * Устанавливает режим отображения графиков.
     *
     * @param o true — наложение графиков, false — по одному
     */
    public void setOverlay(boolean o) { overlay = o; fireChange(); }

    public int getCurrentIndex() { return currentIndex; }
    public void setCurrentIndex(int idx) {
        if (idx>=0 && idx<files.size()) {
            currentIndex = idx;
            fireChange();
        }
    }

    public void addChangeListener(ChangeListener l) { listeners.add(l); }
    public void fireChange() { listeners.forEach(ChangeListener::stateChanged); }

    @FunctionalInterface public interface ChangeListener {
        void stateChanged();
    }
}