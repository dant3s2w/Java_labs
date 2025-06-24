package org.example.network;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.zip.CRC32;

/**
 * Набор статических методов для шифрования, дешифрования
 * и проверки целостности данных.
 */
public class NetworkUtils {
    private static final byte[] KEY = "0123456789abcdef".getBytes();
    private static final byte[] IV = "fedcba9876543210".getBytes();

    /**
     * Вычисляет CRC32 для массива байт.
     *
     * @param data входные данные для расчёта
     * @return значение CRC32 в виде long
     */
    public static long crc32(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }

    /**
     * Создаёт поток, который шифрует данные по алгоритму AES/CBC/PKCS5Padding.
     *
     * @param rawOut исходный поток вывода
     * @return поток, в который можно записывать зашифрованные данные
     * @throws Exception если не удалось инициализировать шифр
     */
    public static OutputStream encryptedOut(OutputStream rawOut) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(
                Cipher.ENCRYPT_MODE,
                new SecretKeySpec(KEY, "AES"),
                new IvParameterSpec(IV)
        );
        return new CipherOutputStream(rawOut, cipher);
    }

    /**
     * Создаёт поток, который дешифрует данные по алгоритму AES/CBC/PKCS5Padding.
     *
     * @param rawIn исходный поток ввода с зашифрованными данными
     * @return поток, из которого можно читать дешифрованные данные
     * @throws Exception если не удалось инициализировать шифр
     */
    public static InputStream decryptedIn(InputStream rawIn) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(
                Cipher.DECRYPT_MODE,
                new SecretKeySpec(KEY, "AES"),
                new IvParameterSpec(IV)
        );
        return new CipherInputStream(rawIn, cipher);
    }

    /**
     * Преобразует объект в массив байт через стандартную сериализацию.
     *
     * @param obj объект для сериализации
     * @return массив байт с данными объекта
     * @throws IOException если произошла ошибка ввода‑вывода при записи
     */
    public static byte[] toBytes(Object obj) throws IOException {
        try (var bos = new ByteArrayOutputStream();
             var oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }

    /**
     * Восстанавливает объект из массива байт через стандартную десериализацию.
     *
     * @param data массив байт с данными объекта
     * @return восстановленный объект
     * @throws IOException            если произошла ошибка ввода‑вывода при чтении
     * @throws ClassNotFoundException если класс объекта не найден
     */
    public static Object fromBytes(byte[] data) throws IOException, ClassNotFoundException {
        try (var ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ois.readObject();
        }
    }
}
