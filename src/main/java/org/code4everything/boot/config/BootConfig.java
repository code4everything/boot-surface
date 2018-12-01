package org.code4everything.boot.config;

import com.google.common.cache.Cache;
import org.code4everything.boot.bean.ConfigBean;
import org.code4everything.boot.encoder.FieldEncoder;
import org.code4everything.boot.log.AopLogUtils;
import org.code4everything.boot.module.redis.RedisTemplateUtils;
import org.code4everything.boot.web.mvc.DefaultWebInterceptor;
import org.springframework.data.redis.connection.RedisConnectionFactory;

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
     * 是否对字段进行加密
     *
     * @since 1.0.0
     */
    private static boolean sealed = false;

    /**
     * 字段加密器
     *
     * @since 1.0.0
     */
    private static FieldEncoder fieldEncoder = new FieldEncoder();

    private BootConfig() {}

    /**
     * 设置指定日志缓存
     *
     * @param logCache 日志缓存
     *
     * @since 1.0.1
     */
    public static void setLogCache(Cache<String, ?> logCache) {
        AopLogUtils.setLogCache(logCache);
    }

    /**
     * 设置配置类
     *
     * @param configBean {@link ConfigBean}
     *
     * @since 1.0.2
     */
    public static void setConfigBean(ConfigBean configBean) {
        DefaultWebInterceptor.setConfigBean(configBean);
    }


    /**
     * 初始化 Redis 连接池
     *
     * @param hostName 主机
     * @param port 端口
     * @param database 数据库
     *
     * @since 1.0.0
     */
    public static void initRedisConnectionFactory(String hostName, Integer port, Integer database) {
        RedisTemplateUtils.initRedisConnectionFactory(hostName, port, database);
    }

    /**
     * 初始化 Redis 连接池
     *
     * @param hostName 主机
     * @param port 端口
     *
     * @since 1.0.0
     */
    public static void initRedisConnectionFactory(String hostName, Integer port) {
        RedisTemplateUtils.initRedisConnectionFactory(hostName, port);
    }

    /**
     * 初始化 Redis 连接池
     *
     * @param hostName 主机
     *
     * @since 1.0.0
     */
    public static void initRedisConnectionFactory(String hostName) {
        RedisTemplateUtils.initRedisConnectionFactory(hostName);
    }

    /**
     * 设置连接池
     *
     * @param redisConnectionFactory 连接池
     *
     * @since 1.0.4
     */
    public static void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplateUtils.setRedisConnectionFactory(redisConnectionFactory);
    }


    /**
     * 获取字段加密器
     *
     * @return 字段加密器
     *
     * @since 1.0.0
     */
    public static FieldEncoder getFieldEncoder() {
        return fieldEncoder;
    }

    /**
     * 设置字段加密器
     *
     * @param fieldEncoder 字段加密器
     *
     * @since 1.0.0
     */
    public static synchronized void setFieldEncoder(FieldEncoder fieldEncoder) {
        BootConfig.fieldEncoder = fieldEncoder;
    }

    /**
     * 是否对字段进行加密
     *
     * @return 是否对字段进行加密
     *
     * @since 1.0.0
     */
    public static boolean isSealed() {
        return sealed;
    }

    /**
     * 设置是否对字段进行加密
     *
     * @param sealed 是否对字段进行加密
     *
     * @since 1.0.0
     */
    public static synchronized void setSealed(boolean sealed) {
        BootConfig.sealed = sealed;
    }

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
    public static synchronized void setDebug(boolean debug) {
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
    public static synchronized void setMaxUploadFileSize(long maxUploadFileSize) {
        BootConfig.maxUploadFileSize = maxUploadFileSize;
    }
}
