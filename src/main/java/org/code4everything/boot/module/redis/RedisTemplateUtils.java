package org.code4everything.boot.module.redis;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.code4everything.boot.config.BootConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;

/**
 * Redis 缓存工具类
 *
 * @author pantao
 * @since 2018/11/16
 **/
public class RedisTemplateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTemplateUtils.class);

    private static RedisConnectionFactory redisConnectionFactory = null;

    private RedisTemplateUtils() {}

    /**
     * 获取连接池
     *
     * @return 连接池
     *
     * @since 1.0.0
     */
    public static RedisConnectionFactory getRedisConnectionFactory() {
        return getRedisConnectionFactory(null, null, null);
    }

    /**
     * 设置连接池
     *
     * @param redisConnectionFactory 连接池
     *
     * @since 1.0.0
     */
    public static void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplateUtils.redisConnectionFactory = redisConnectionFactory;
    }

    /**
     * 获取连接池
     *
     * @param hostName 主机
     * @param port 端口
     * @param database 数据库
     *
     * @return 连接池
     *
     * @since 1.0.0
     */
    public static RedisConnectionFactory getRedisConnectionFactory(String hostName, Integer port, Integer database) {
        // 连接池在启动项目时就该配置好了，故不考虑并发情况
        if (Objects.isNull(redisConnectionFactory)) {
            initRedisConnectionFactory(hostName, port, database);
        }
        return redisConnectionFactory;
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
        initRedisConnectionFactory(hostName, port, null);
    }

    /**
     * 初始化 Redis 连接池
     *
     * @param hostName 主机
     *
     * @since 1.0.0
     */
    public static void initRedisConnectionFactory(String hostName) {
        initRedisConnectionFactory(hostName, null, null);
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
        // 连接配置
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        // 日志消息
        StringBuilder message = new StringBuilder();
        String sep = "";
        if (Validator.isNotEmpty(hostName)) {
            // 设置主机地址
            configuration.setHostName(hostName);
            message.append(sep).append(" host -> ").append(hostName);
            sep = ", ";
        }
        if (ObjectUtil.isNotNull(port)) {
            // 设置端口
            configuration.setPort(port);
            message.append(sep).append(" port -> ").append(port);
            sep = ", ";
        }
        if (ObjectUtil.isNotNull(database)) {
            // 设置要连接的数据库
            configuration.setDatabase(database);
            message.append(sep).append(" database -> ").append(database);
        }
        // 生成连接工厂
        redisConnectionFactory = new JedisConnectionFactory(configuration);
        if (BootConfig.isDebug() && message.length() != 0) {
            LOGGER.info("connect to redis server by {}", message);
        }
    }

    /**
     * 获取模板
     *
     * @param <V> 值类型
     * @param type 值类型
     *
     * @return {@link RedisTemplate}
     *
     * @since 1.0.0
     */
    public static <V> RedisTemplate<String, V> newTemplate(Class<V> type) {
        return newTemplate(String.class, type);
    }

    /**
     * 获取模板
     *
     * @param <K> 键类型
     * @param <V> 值类型
     * @param keyType 值类型
     * @param valueType 值类型
     *
     * @return {@link RedisTemplate}
     *
     * @since 1.0.0
     */
    public static <K, V> RedisTemplate<K, V> newTemplate(Class<K> keyType, Class<V> valueType) {
        // 生成一个RedisTemplate
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(getRedisConnectionFactory());
        if (ObjectUtil.isNotNull(keyType)) {
            // 选择键序列化的方式
            if (keyType == String.class || ClassUtil.isBasicType(keyType)) {
                // 使用String序列化
                redisTemplate.setKeySerializer(new StringRedisSerializer());
            } else {
                // 使用FastJson序列化
                redisTemplate.setKeySerializer(new FastJsonRedisSerializer<>(keyType));
            }
        }
        if (ObjectUtil.isNotNull(valueType)) {
            // 对值进行FastJson序列化
            redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(valueType));
        }
        return redisTemplate;
    }


    /**
     * 获取模板
     *
     * @param <V> 值类型
     *
     * @return {@link RedisTemplate}
     *
     * @since 1.0.0
     */
    public static <V> RedisTemplate<String, V> newTemplate() {
        return newTemplate(null);
    }
}
