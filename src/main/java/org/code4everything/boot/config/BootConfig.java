package org.code4everything.boot.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import org.code4everything.boot.base.FileUtils;
import org.code4everything.boot.base.FileWatcher;
import org.code4everything.boot.bean.ConfigBean;
import org.code4everything.boot.bean.Response;
import org.code4everything.boot.encoder.DefaultFieldEncoder;
import org.code4everything.boot.encoder.FieldEncoder;
import org.code4everything.boot.log.AopLogUtils;
import org.code4everything.boot.message.MailUtils;
import org.code4everything.boot.module.redis.RedisTemplateUtils;
import org.code4everything.boot.web.mvc.DefaultWebInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

/**
 * 工具配置类
 *
 * @author pantao
 * @since 2018/11/10
 */
public class BootConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootConfig.class);

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
    private static FieldEncoder fieldEncoder;

    private BootConfig() {}

    /**
     * 设置邮件发送器
     *
     * @param outbox 发件箱
     * @param mailSender {@link JavaMailSender}
     *
     * @since 1.0.9
     */
    public static void setMailSender(String outbox, JavaMailSender mailSender) {
        MailUtils.setMailSender(outbox, mailSender);
    }

    /**
     * 设置请求检测的频率，单位：毫秒
     *
     * @param frequency 频率
     *
     * @since 1.1.0
     */
    public static void setFrequency(Integer frequency) {
        DefaultWebInterceptor.setFrequency(frequency);
    }

    /**
     * 监听工作路径下的 boot-config.json 配置文件
     *
     * @since 1.0.6
     */
    public static void watchBootConfig() {
        watchBootConfig(FileUtils.currentWorkDir("boot-config.json"));
    }

    /**
     * 监听配置文件
     *
     * @param bootConfigPath 配置文件路径
     *
     * @since 1.0.6
     */
    public static void watchBootConfig(String bootConfigPath) {
        FileUtils.watchFile(bootConfigPath, new FileWatcher() {

            @Override
            public void doSomething() {
                if (FileUtil.exist(bootConfigPath)) {
                    parseJson(JSONObject.parseObject(FileUtil.readString(bootConfigPath, CharsetUtil.UTF_8)));
                } else {
                    LOGGER.warn("boot config file [{}] is not found", bootConfigPath);
                }
            }
        }, true);
    }

    /**
     * 解析JSON配置
     *
     * @param boot JSON 配置
     *
     * @since 1.0.6
     */
    public static void parseJson(JSONObject boot) {
        if (ObjectUtil.isNotNull(boot)) {
            // 最大文件上传大小
            setMaxUploadFileSize(boot.getLong("maxUploadFileSize"));
            // 是否开启debug模式
            setDebug(boot.getBoolean("debug"));
            // 是否对响应字段加密
            setSealed(boot.getBoolean("sealed"));
            // 请求成功的响应码
            setOkCode(boot.getInteger("okCode"));
            // 设置请求检测频率
            setFrequency(boot.getInteger("frequency"));
            LOGGER.info("boot config is changed >>> {}", boot);
        }
    }

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
     * 设置正确码
     *
     * @param okCode 正确码
     *
     * @since 1.0.5
     */
    public static void setOkCode(Integer okCode) {
        Response.setOkCode(okCode);
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
        if (Objects.isNull(fieldEncoder)) {
            fieldEncoder = new DefaultFieldEncoder();
        }
        return fieldEncoder;
    }

    /**
     * 设置字段加密器
     *
     * @param fieldEncoder 字段加密器
     *
     * @since 1.0.0
     */
    public static void setFieldEncoder(FieldEncoder fieldEncoder) {
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
    public static void setSealed(Boolean sealed) {
        if (ObjectUtil.isNotNull(sealed)) {
            BootConfig.sealed = sealed;
        }
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
    public static void setDebug(Boolean debug) {
        if (ObjectUtil.isNotNull(debug)) {
            BootConfig.debug = debug;
        }
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
    public static void setMaxUploadFileSize(Long maxUploadFileSize) {
        if (ObjectUtil.isNotNull(maxUploadFileSize)) {
            BootConfig.maxUploadFileSize = maxUploadFileSize;
        }
    }
}
