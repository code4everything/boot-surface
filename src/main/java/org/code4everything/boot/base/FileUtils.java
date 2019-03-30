package org.code4everything.boot.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.io.watch.watchers.DelayWatcher;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.constant.StringConsts;
import org.code4everything.boot.interfaces.FileWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;

/**
 * 文件工具类
 *
 * @author pantao
 * @since 2018/11/2
 **/
public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {}

    /**
     * 监听文件变化，并自动注入Bean类
     *
     * @param jsonFile JSON文件
     * @param config Bean类
     * @param clazz Bean类的类型
     * @param <T> 类型
     *
     * @since 1.0.4
     */
    public static <T> void watchFile(String jsonFile, T config, Class<T> clazz) {
        watchFile(jsonFile, new FileWatcher() {}, config, clazz, CharsetUtil.UTF_8);
    }

    /**
     * 监听文件变化，并自动注入Bean类
     *
     * @param jsonFile JSON文件
     * @param config Bean类
     * @param clazz Bean类的类型
     * @param charset 文件编码
     * @param <T> 类型
     *
     * @since 1.0.4
     */
    public static <T> void watchFile(String jsonFile, T config, Class<T> clazz, String charset) {
        watchFile(jsonFile, new FileWatcher() {}, config, clazz, charset);
    }

    /**
     * 监听文件变化，并自动注入Bean类
     *
     * @param jsonFile JSON文件
     * @param fileWatcher {@link FileWatcher}
     * @param config Bean类
     * @param clazz Bean类的类型
     * @param <T> 类型
     *
     * @since 1.0.4
     */
    public static <T> void watchFile(String jsonFile, FileWatcher fileWatcher, T config, Class<T> clazz) {
        watchFile(jsonFile, fileWatcher, config, clazz, CharsetUtil.UTF_8);
    }

    /**
     * 监听文件变化，并自动注入Bean类
     *
     * @param jsonFile JSON文件
     * @param fileWatcher {@link FileWatcher}
     * @param config Bean类
     * @param clazz Bean类的类型
     * @param charset 文件编码
     * @param <T> 类型
     *
     * @since 1.0.4
     */
    public static <T> void watchFile(String jsonFile, FileWatcher fileWatcher, T config, Class<T> clazz,
                                     String charset) {
        watchFile(jsonFile, new FileWatcher() {

            @Override
            public void doSomething() {
                // 解析JSON文件
                JSONObject root = JSONObject.parseObject(FileUtil.readString(jsonFile, charset));
                // 属性复制
                BeanUtil.copyProperties(root.toJavaObject(clazz), config);
                if (root.containsKey(StringConsts.BOOT)) {
                    // 解析BootSurface配置
                    BootConfig.parseJson(root.getJSONObject("boot"));
                }
                fileWatcher.doSomething();
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                fileWatcher.onModify(event, currentPath);
            }
        }, true);
    }

    /**
     * 监听文件
     *
     * @param file 待监听的文件
     * @param fileWatcher {@link FileWatcher}
     *
     * @since 1.0.0
     */
    public static void watchFile(String file, FileWatcher fileWatcher) {
        watchFile(file, fileWatcher, false);
    }

    /**
     * 监听文件
     *
     * @param file 待监听的文件
     * @param fileWatcher {@link FileWatcher}
     * @param shouldFirstExecute 加载时是否执行 {@link FileWatcher#doSomething()}
     *
     * @since 1.0.0
     */
    public static void watchFile(String file, FileWatcher fileWatcher, boolean shouldFirstExecute) {
        if (shouldFirstExecute) {
            fileWatcher.doSomething();
            if (BootConfig.isDebug()) {
                LOGGER.info("load file -> {}", file);
            }
        }
        SimpleWatcher simpleWatcher = new SimpleWatcher() {

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                fileWatcher.onModify(event, currentPath);
                fileWatcher.doSomething();
                if (BootConfig.isDebug()) {
                    LOGGER.info("load file -> {}", file);
                }
            }
        };
        // 文件发生变化时延迟一秒钟执行
        watchFile(file, new DelayWatcher(simpleWatcher, 1000));
    }

    /**
     * 监听文件
     *
     * @param file 待监听的文件
     * @param watcher 监听器 {@link Watcher}
     *
     * @since 1.0.0
     */
    public static void watchFile(String file, Watcher watcher) {
        WatchMonitor.createAll(file, watcher).start();
    }

    /**
     * 获取当前工作路径
     *
     * @return 当前工作路径
     *
     * @since 1.0.0
     */
    public static String currentWorkDir() {
        return Paths.get(StrUtil.DOT).toAbsolutePath().normalize().toString();
    }
}
