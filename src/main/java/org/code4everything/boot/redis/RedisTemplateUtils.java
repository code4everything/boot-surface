package org.code4everything.boot.redis;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
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

    private static JedisConnectionFactory jedisConnectionFactory = null;

    private RedisTemplateUtils() {}

    /**
     * 获取连接池
     *
     * @return 连接池
     *
     * @since 1.0.0
     */
    public static JedisConnectionFactory getJedisConnectionFactory() {
        return getJedisConnectionFactory(null, null, null);
    }

    /**
     * 设置连接池
     *
     * @param jedisConnectionFactory 连接池
     *
     * @since 1.0.0
     */
    public static void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplateUtils.jedisConnectionFactory = jedisConnectionFactory;
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
    public static JedisConnectionFactory getJedisConnectionFactory(String hostName, Integer port, Integer database) {
        // 连接池在启动项目时就配置好了，故不考虑并发情况
        if (Objects.isNull(jedisConnectionFactory)) {
            initJedisConnectionFactory(hostName, port, database);
        }
        return jedisConnectionFactory;
    }

    /**
     * 初始化 Redis 连接池
     *
     * @param hostName 主机
     * @param port 端口
     *
     * @since 1.0.0
     */
    public static void initJedisConnectionFactory(String hostName, Integer port) {
        initJedisConnectionFactory(hostName, port, null);
    }

    /**
     * 初始化 Redis 连接池
     *
     * @param hostName 主机
     *
     * @since 1.0.0
     */
    public static void initJedisConnectionFactory(String hostName) {
        initJedisConnectionFactory(hostName, null, null);
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
    public static void initJedisConnectionFactory(String hostName, Integer port, Integer database) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        if (Validator.isNotEmpty(hostName)) {
            configuration.setHostName(hostName);
        }
        if (ObjectUtil.isNotNull(port)) {
            configuration.setPort(port);
        }
        if (ObjectUtil.isNotNull(database)) {
            configuration.setDatabase(database);
        }
        jedisConnectionFactory = new JedisConnectionFactory(configuration);
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
     * @param valueType 值类型
     *
     * @return {@link RedisTemplate}
     *
     * @since 1.0.0
     */
    public static <K, V> RedisTemplate<K, V> newTemplate(Class<K> keyType, Class<V> valueType) {
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(getJedisConnectionFactory());
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
