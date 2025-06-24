package org.example.network;

import org.example.model.Parent;
import org.example.model.Student;
import org.example.model.Subject;
import org.example.model.Teacher;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Клиент для отправки объектов Student на сервер.
 * Открывает отдельное соединение для каждого студента,
 * сериализует данные, вычисляет CRC32 и отправляет их.
 */
public class Client {
    /**
     * Список студентов для передачи.
     */
    private final List<Student> students;

    /**
     * Создаёт клиент с указанным списком студентов.
     *
     * @param students список студентов для отправки
     */
    public Client(List<Student> students) {
        this.students = students;
    }

    /**
     * Последовательно отправляет каждого студента из списка.
     * <ul>
     *   <li>Открывает сокетное соединение с сервером.</li>
     *   <li>Шифрует и сериализует объект Student.</li>
     *   <li>Вычисляет и отправляет CRC32 для проверки целостности.</li>
     *   <li>Закрывает соединение или повторяет передачу при ошибке.</li>
     * </ul>
     */
    public void sendAll() {
        System.out.println("🔌 Подключение к серверу...");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            try (Socket sock = new Socket("localhost", Server.PORT)) {
                OutputStream encryptedOut = NetworkUtils.encryptedOut(sock.getOutputStream());
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(encryptedOut));
                InputStream decryptedIn = NetworkUtils.decryptedIn(sock.getInputStream());
                DataInputStream in = new DataInputStream(new BufferedInputStream(decryptedIn));

                byte[] data = NetworkUtils.toBytes(s);
                long crc = NetworkUtils.crc32(data);

                System.out.println(" Отправка студента #" + i + ": " + s.getName());
                System.out.println("   Размер данных: " + data.length + " байт | CRC32: " + crc);

                out.writeInt(data.length);
                out.write(data);
                out.flush();

                out.writeLong(crc);
                out.flush();
                out.close();
                sock.close();

                System.out.println(" Студент отправлен: " + s.getName());

            } catch (Exception e) {
                System.err.println(" Ошибка при передаче: " + e.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                i--;
            }
        }

        System.out.println(" Все студенты отправлены успешно.");
    }

    /**
     * Точка входа в приложение.
     * Создаёт примеры объектов Student, Parent и Teacher,
     * выставляет оценки и отправляет их на сервер.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Student s1 = new Student("Алексей", 20, "М", new Parent("Иван", 45, "М"));
        Student s2 = new Student("Мария", 21, "Ж", new Parent("Анна", 44, "Ж"));
        Student s3 = new Student("Олег", 22, "М", new Parent("Светлана", 47, "Ж"));

        Teacher t = new Teacher("Петров", 50, "М", new Subject("Математика"));
        t.gradeStudent(s1);
        t.gradeStudent(s2);
        t.gradeStudent(s3);

        new Client(List.of(s1, s2, s3)).sendAll();
    }
}
