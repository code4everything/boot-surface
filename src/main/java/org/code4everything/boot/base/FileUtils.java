package org.code4everything.boot.base;

import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.io.watch.watchers.DelayWatcher;
import cn.hutool.core.util.StrUtil;
import org.apache.log4j.Logger;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.interfaces.FileWatcher;

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

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class);

    private FileUtils() {}

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
                LOGGER.info("load file -> " + file);
            }
        }
        SimpleWatcher simpleWatcher = new SimpleWatcher() {
            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                fileWatcher.onModify(event, currentPath);
                fileWatcher.doSomething();
                if (BootConfig.isDebug()) {
                    LOGGER.info("load file -> " + file);
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
