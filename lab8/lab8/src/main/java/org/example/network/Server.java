package org.example.network;

import org.example.model.Student;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервер для приёма объектов Student.
 * Для каждого соединения получает данные одного студента,
 * проверяет контрольную сумму и сохраняет в хранилище.
 */
public class Server {
    /**
     * Порт, на котором сервер ожидает подключения.
     */
    public static final int PORT = 34567;

    /**
     * Список принятых и проверенных студентов.
     */
    private final List<Student> store = new ArrayList<>();

    /**
     * Запускает сервер.
     * <ul>
     *   <li>Открывает ServerSocket на порту PORT.</li>
     *   <li>В бесконечном цикле ждёт и обрабатывает подключения.</li>
     *   <li>Для каждого клиента:
     *     <ul>
     *       <li>Инициализирует поток для расшифровки входящих данных.</li>
     *       <li>Инициализирует поток для шифрования ответных данных.</li>
     *       <li>Читает длину массива байт и сам массив.</li>
     *       <li>Сравнивает полученную и вычисленную CRC32.</li>
     *       <li>При совпадении десериализует объект Student и добавляет в store.</li>
     *       <li>Отправляет клиенту true или false в зависимости от успеха.</li>
     *     </ul>
     *   </li>
     *   <li>Выводит в консоль стадии обработки и размер хранилища.</li>
     * </ul>
     *
     * @throws IOException если произошла ошибка ввода‑вывода при работе с сокетом
     */
    public void start() throws IOException {
        try (var server = new ServerSocket(PORT)) {
            System.out.println(" Encrypted Server started on port " + PORT);
            while (true) {
                try (Socket sock = server.accept()) {
                    System.out.println(" Client connected: " + sock.getInetAddress());

                    DataInputStream in;
                    try {
                        in = new DataInputStream(
                                new BufferedInputStream(
                                        NetworkUtils.decryptedIn(sock.getInputStream())
                                )
                        );
                    } catch (Exception e) {
                        System.err.println(" Ошибка инициализации входного потока: " + e.getMessage());
                        continue;
                    }
                    DataOutputStream out;
                    try {
                        out = new DataOutputStream(
                                new BufferedOutputStream(
                                        NetworkUtils.encryptedOut(sock.getOutputStream())
                                )
                        );
                    } catch (Exception e) {
                        System.err.println(" Ошибка инициализации выходного потока: " + e.getMessage());
                        continue;
                    }

                    int length = in.readInt();
                    System.out.println(" Получена длина: " + length);
                    byte[] data = new byte[length];
                    int total = 0;
                    while (total < data.length) {
                        int count = in.read(data, total, data.length - total);
                        if (count == -1) throw new IOException("Stream closed before full data received");
                        total += count;
                    }
                    long receivedCrc = in.readLong();
                    long actualCrc = NetworkUtils.crc32(data);

                    System.out.println(" Получено " + length +
                            " байт, CRC: " + receivedCrc +
                            " (ожидаемый: " + actualCrc + ")");

                    if (receivedCrc != actualCrc) {
                        System.out.println(" CRC не совпала");
                        out.writeBoolean(false);
                        out.flush();
                        continue;
                    }

                    try {
                        Student student = (Student) NetworkUtils.fromBytes(data);
                        store.add(student);
                        System.out.println(" Добавлен студент: " + student.getName());
                        out.writeBoolean(true);
                        out.flush();
                    } catch (Exception e) {
                        System.err.println(" Ошибка десериализации: " + e.getMessage());
                        out.writeBoolean(false);
                        out.flush();
                    }

                    System.out.println(" Всего студентов: " + store.size());
                }
            }
        }
    }

    /**
     * Точка входа приложения.
     * Создаёт сервер и запускает метод start().
     *
     * @param args аргументы командной строки (не используются)
     * @throws IOException если не удалось запустить сервер
     */
    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}
