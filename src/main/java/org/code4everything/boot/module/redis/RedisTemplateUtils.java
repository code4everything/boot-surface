package org.code4everything.boot.module.redis;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.apache.log4j.Logger;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.constant.StringConsts;
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

    private static final Logger LOGGER = Logger.getLogger(RedisTemplateUtils.class);

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
        // 连接池在启动项目时就配置好了，故不考虑并发情况
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
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        StringBuilder message = new StringBuilder("connect to redis server on");
        if (Validator.isNotEmpty(hostName)) {
            configuration.setHostName(hostName);
            message.append(" host -> ").append(hostName).append(" ,");
        }
        if (ObjectUtil.isNotNull(port)) {
            configuration.setPort(port);
            message.append(" port -> ").append(port).append(" ,");
        }
        if (ObjectUtil.isNotNull(database)) {
            configuration.setDatabase(database);
            message.append(" database -> ").append(database).append(" ,");
        }
        redisConnectionFactory = new JedisConnectionFactory(configuration);
        if (BootConfig.isDebug()) {
            String msg = message.toString();
            if (msg.endsWith(StringConsts.Sign.COMMA)) {
                LOGGER.info(msg.substring(0, msg.length() - 2));
            }
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
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(getRedisConnectionFactory());
        if (ObjectUtil.isNotNull(keyType)) {
            if (keyType == String.class) {
                redisTemplate.setKeySerializer(new StringRedisSerializer());
            } else {
                redisTemplate.setKeySerializer(new FastJsonRedisSerializer<>(keyType));
            }
        }
        if (ObjectUtil.isNotNull(valueType)) {
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
