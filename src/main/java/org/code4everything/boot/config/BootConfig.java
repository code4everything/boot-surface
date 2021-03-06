package org.code4everything.boot.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import org.code4everything.boot.base.FileUtils;
import org.code4everything.boot.base.FileWatcher;
import org.code4everything.boot.base.constant.StringConsts;
import org.code4everything.boot.base.encoder.DefaultFieldEncoder;
import org.code4everything.boot.base.encoder.FieldEncoder;
import org.code4everything.boot.log.AopLogUtils;
import org.code4everything.boot.message.MailUtils;
import org.code4everything.boot.module.redis.RedisTemplateUtils;
import org.code4everything.boot.web.http.RestUtils;
import org.code4everything.boot.web.mvc.DefaultWebInterceptor;
import org.code4everything.boot.web.mvc.FilterPath;
import org.code4everything.boot.web.mvc.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Objects;

/**
 * 工具配置类
 *
 * @author pantao
 * @since 2018/11/10
 */
public class BootConfig {

    /**
     * 默认响应错误码
     */
    public static final int DEFAULT_ERROR_CODE = -1;

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
     * 设置REST默认服务器
     *
     * @param restServer 服务器地址
     *
     * @since 1.1.2
     */
    public static void setRestServer(String restServer) {
        RestUtils.setRestServer(restServer);
    }

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
     * 设置邮件发送器
     *
     * @param sender {@link JavaMailSenderImpl}
     *
     * @since 1.1.3
     */
    public static void setMailSender(JavaMailSenderImpl sender) {
        MailUtils.setMailSender(sender);
    }

    /**
     * 初始化邮件发送器
     *
     * @param host 主机
     * @param protocol 协议
     * @param username 用户名（发件箱）
     * @param password 密码或授权密钥
     *
     * @since 1.1.3
     */
    public static void initMailSender(String host, String protocol, String username, String password) {
        MailUtils.initMailSender(host, null, protocol, username, password);
    }

    /**
     * 初始化邮件发送器
     *
     * @param host 主机
     * @param port 端口
     * @param protocol 协议
     * @param username 用户名（发件箱）
     * @param password 密码或授权密钥
     *
     * @since 1.1.3
     */
    public static void initMailSender(String host, Integer port, String protocol, String username, String password) {
        MailUtils.initMailSender(host, port, protocol, username, password);
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
                if (!FileUtil.exist(bootConfigPath)) {
                    LOGGER.warn("boot config file [{}] is not found", bootConfigPath);
                    return;
                }
                setConfig(JSONObject.parseObject(FileUtil.readUtf8String(bootConfigPath), BootConfigProperties.class));
            }
        }, true);
    }

    /**
     * 解析JSON配置
     *
     * @param root JSON 配置
     *
     * @since 1.0.6
     */
    public static void parseJson(JSONObject root) {
        if (root.containsKey(StringConsts.BOOT)) {
            setConfig(root.getObject(StringConsts.BOOT, BootConfigProperties.class));
        } else {
            setConfig(JSON.parseObject(root.toJSONString(), BootConfigProperties.class));
        }
    }

    /**
     * 设置
     *
     * @param properties 设置
     *
     * @since 1.1.0
     */
    public static void setConfig(BootConfigProperties properties) {
        if (Objects.isNull(properties)) {
            return;
        }
        setFrequency(properties.getFrequency());
        setOkCode(properties.getOkCode());
        setSealed(properties.getSealed());
        setDebug(properties.getDebug());
        setMaxUploadFileSize(properties.getMaxUploadFileSize());
        setVisitLog(properties.getVisitLog());
        setRestServer(properties.getRestServer());

        RedisConfigProperties redis = properties.getRedis();
        if (ObjectUtil.isNotNull(redis)) {
            initRedisConnectionFactory(redis.getHost(), redis.getPort(), redis.getDb());
        }

        MailConfigProperties mail = properties.getMail();
        if (ObjectUtil.isNotNull(mail)) {
            initMailSender(mail.getHost(), mail.getPort(), mail.getProtocol(), mail.getUsername(), mail.getPassword());
        }

        LOGGER.info("boot config is changed >>> {}", properties);
    }

    /**
     * 设置是否统计访问数据
     *
     * @param visitLog 是否统计访问数据
     *
     * @since 1.1.0
     */
    public static void setVisitLog(Boolean visitLog) {
        DefaultWebInterceptor.setVisitLog(visitLog);
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
     * 设置访问路径过滤配置
     *
     * @param filterPath {@link FilterPath}
     *
     * @since 1.0.2
     */
    public static void setFilterPath(FilterPath filterPath) {
        DefaultWebInterceptor.setFilterPath(filterPath);
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
