package org.code4everything.boot.base;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 文件监听器
 *
 * @author pantao
 * @since 2018/11/10
 */
public interface FileWatcher {

    /**
     * 执行一些业务代码，最后调用
     *
     * @since 1.0.0
     */
    default void doSomething() {}

    /**
     * 文件发生变化，最先调用
     *
     * @param event 时间
     * @param currentPath 当前路径
     *
     * @since 1.0.0
     */
    default void onModify(WatchEvent<?> event, Path currentPath) {}
}
