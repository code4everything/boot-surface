package org.code4everything.boot.module.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author pantao
 * @since 2019/9/28
 */
public final class KryoUtils {

    private KryoUtils() {}

    /**
     * 序列化
     *
     * @param object 对象
     * @param outputFile 输出文件
     * @param <T> 对象类型
     *
     * @throws FileNotFoundException 异常
     * @since 1.1.7
     */
    public static <T> void serialize(T object, String outputFile) throws FileNotFoundException {
        serialize(object, new File(outputFile));
    }

    /**
     * 序列化
     *
     * @param object 对象
     * @param outputFile 输出文件
     * @param <T> 对象类型
     *
     * @throws FileNotFoundException 异常
     * @since 1.1.7
     */
    public static <T> void serialize(T object, File outputFile) throws FileNotFoundException {
        serialize(object, new Output(new FileOutputStream(outputFile)));
    }

    /**
     * 序列化
     *
     * @param object 对象
     * @param output 输出流
     * @param <T> 对象类型
     *
     * @since 1.1.7
     */
    public static <T> void serialize(T object, Output output) {
        Kryo kryo = new Kryo();
        kryo.register(object.getClass());
        try {
            kryo.writeObject(output, object);
        } finally {
            output.close();
        }
    }

    /**
     * 反序列化
     *
     * @param inputFile 输入文件
     * @param clazz 对象类
     * @param <T> 类型
     *
     * @return 对象
     *
     * @throws FileNotFoundException 异常
     * @since 1.1.7
     */
    public static <T> T deserialize(String inputFile, Class<T> clazz) throws FileNotFoundException {
        return deserialize(new File(inputFile), clazz);
    }

    /**
     * 反序列化
     *
     * @param inputFile 输入文件
     * @param clazz 对象类
     * @param <T> 类型
     *
     * @return 对象
     *
     * @throws FileNotFoundException 异常
     * @since 1.1.7
     */
    public static <T> T deserialize(File inputFile, Class<T> clazz) throws FileNotFoundException {
        return deserialize(new Input(new FileInputStream(inputFile)), clazz);
    }

    /**
     * 反序列化
     *
     * @param input 输入流
     * @param clazz 对象类
     * @param <T> 类型
     *
     * @return 对象
     *
     * @since 1.1.7
     */
    public static <T> T deserialize(Input input, Class<T> clazz) {
        Kryo kryo = new Kryo();
        kryo.register(clazz);
        try {
            return kryo.readObject(input, clazz);
        } finally {
            input.close();
        }
    }
}
