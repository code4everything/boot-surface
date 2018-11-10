package org.code4everything.boot.config;

/**
 * 工具配置类
 *
 * @author pantao
 * @since 2018/11/10
 */
public class BootConfig {

    /**
     * 调试
     *
     * @since 1.0.0
     */
    private static boolean debug = false;

    /**
     * 最大文件上传大小
     *
     * @since 1.0.0
     */
    private static long maxUploadFileSize = Long.MAX_VALUE;

    /**
     * 是否调试
     *
     * @return 是否调试
     *
     * @since 1.0.0
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * 设置是否调试
     *
     * @param debug 是否调试
     *
     * @since 1.0.0
     */
    public static void setDebug(boolean debug) {
        BootConfig.debug = debug;
    }

    /**
     * 获取最大文件上传大小
     *
     * @return 最大文件上传大小
     *
     * @since 1.0.0
     */
    public static long getMaxUploadFileSize() {
        return maxUploadFileSize;
    }

    /**
     * 设置最大文件上传大小
     *
     * @param maxUploadFileSize 最大文件上传大小
     *
     * @since 1.0.0
     */
    public static void setMaxUploadFileSize(long maxUploadFileSize) {
        BootConfig.maxUploadFileSize = maxUploadFileSize;
    }
}
