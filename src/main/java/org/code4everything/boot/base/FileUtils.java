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
import org.code4everything.boot.base.constant.IntegerConsts;
import org.code4everything.boot.base.constant.StringConsts;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.config.BootConfigProperties;
import org.code4everything.boot.web.mvc.FilterPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.Objects;

/**
 * 文件工具类
 *
 * @author pantao
 * @since 2018/11/2
 **/
public final class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static final String[] SIZE = {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};

    private static final int KB = 1000;

    private FileUtils() {}

    /**
     * 格式化文件大小
     *
     * @param size 大小
     * @param scale 保留小数位数
     *
     * @return 文件大小
     *
     * @since 1.1.2
     */
    public static String formatSize(long size, int scale) {
        if (size < 0) {
            throw new IllegalArgumentException("size must not be negative");
        }
        if (scale < 0) {
            scale = 0;
        }
        if (scale > IntegerConsts.THREE) {
            scale = 3;
        }
        int idx = 0;
        long last = 0;
        while (size >= KB) {
            idx++;
            last = size;
            size /= KB;
        }
        if (scale == 0) {
            last = 0;
        } else {
            last %= KB;
            last = scale == 3 ? last : (scale == 1 ? last / 100 : last / 10);
        }
        return size + (last > 0 ? "." + StrUtil.padPre(String.valueOf(last), scale, "0") : "") + " " + SIZE[idx];
    }

    /**
     * 监听文件变化，并自动注入Bean类
     *
     * @param jsonFile JSON文件
     * @param config Bean类
     * @param <T> 类型
     *
     * @since 1.0.4
     */
    public static <T> void watchFile(String jsonFile, T config) {
        watchFile(jsonFile, new FileWatcher() {}, config, CharsetUtil.UTF_8);
    }

    /**
     * 监听文件变化，并自动注入Bean类
     *
     * @param jsonFile JSON文件
     * @param config Bean类
     * @param charset 文件编码
     * @param <T> 类型
     *
     * @since 1.0.4
     */
    public static <T> void watchFile(String jsonFile, T config, String charset) {
        watchFile(jsonFile, new FileWatcher() {}, config, charset);
    }

    /**
     * 监听文件变化，并自动注入Bean类
     *
     * @param jsonFile JSON文件
     * @param fileWatcher {@link FileWatcher}
     * @param config Bean类
     * @param <T> 类型
     *
     * @since 1.0.4
     */
    public static <T> void watchFile(String jsonFile, FileWatcher fileWatcher, T config) {
        watchFile(jsonFile, fileWatcher, config, CharsetUtil.UTF_8);
    }

    /**
     * 监听文件变化，并自动注入Bean类
     *
     * @param jsonFile JSON文件
     * @param fileWatcher {@link FileWatcher}
     * @param config Bean类
     * @param charset 文件编码
     * @param <T> 类型
     *
     * @since 1.0.4
     */
    public static <T> void watchFile(String jsonFile, FileWatcher fileWatcher, T config, String charset) {
        Objects.requireNonNull(config, "the object 'config' must not be null");
        watchFile(jsonFile, new FileWatcher() {

            // 是否设置访问拦截名单
            private boolean notSet = config instanceof FilterPath;

            @Override
            public void doSomething() {
                // 解析JSON文件
                JSONObject root = JSONObject.parseObject(FileUtil.readString(jsonFile, charset));
                // 属性复制
                BeanUtil.copyProperties(root.toJavaObject(config.getClass()), config);
                // 解析BootSurface配置
                if (root.containsKey(StringConsts.BOOT)) {
                    BootConfig.setConfig(root.getObject(StringConsts.BOOT, BootConfigProperties.class));
                }
                // 自动设置拦截名单
                if (notSet) {
                    BootConfig.setFilterPath((FilterPath) config);
                    notSet = false;
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
     * @param more 子路径
     *
     * @return 当前工作路径
     *
     * @since 1.0.0
     */
    public static String currentWorkDir(String... more) {
        return getPath(StringConsts.Sign.DOT, more);
    }

    /**
     * 获取路径
     *
     * @param path 父路径
     * @param more 子路径
     *
     * @return 路径
     *
     * @since 1.1.0
     */
    public static String getPath(String path, String... more) {
        return Paths.get(path, more).toAbsolutePath().normalize().toString();
    }
}
