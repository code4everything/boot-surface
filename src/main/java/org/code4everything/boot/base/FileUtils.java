package org.code4everything.boot.base;

import cn.hutool.core.util.StrUtil;

import java.nio.file.Paths;

/**
 * 文件工具类
 *
 * @author pantao
 * @since 2018/11/2
 **/
public class FileUtils {

    private FileUtils() {}

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
