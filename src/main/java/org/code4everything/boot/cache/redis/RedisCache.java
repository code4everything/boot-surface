package org.code4everything.boot.cache.redis;

import org.code4everything.boot.cache.AbstractCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存
 *
 * @author pantao
 * @since 2019/6/14
 **/
public class RedisCache extends AbstractCache {

    /**
     * @since 1.1.3
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * @since 1.1.3
     */
    private final String keyPrefix;

    /**
     * @since 1.1.3
     */
    private final long timeout;

    /**
     * 构造函数
     *
     * @param name 缓存名
     * @param redisTemplate {@link RedisTemplate}
     *
     * @since 1.1.3
     */
    public RedisCache(String name, RedisTemplate<String, Object> redisTemplate) {
        this(name, redisTemplate, Long.MAX_VALUE, name);
    }

    /**
     * 构造函数
     *
     * @param name 缓存名
     * @param redisTemplate {@link RedisTemplate}
     * @param timeout 过期时长
     *
     * @since 1.1.3
     */
    public RedisCache(String name, RedisTemplate<String, Object> redisTemplate, long timeout) {
        this(name, redisTemplate, timeout, name);
    }

    /**
     * 构造函数
     *
     * @param name 缓存名
     * @param redisTemplate {@link RedisTemplate}
     * @param timeout 过期时长
     * @param keyPrefix 键前缀
     *
     * @since 1.1.3
     */
    public RedisCache(String name, RedisTemplate<String, Object> redisTemplate, long timeout, String keyPrefix) {
        super(name, redisTemplate);
        this.keyPrefix = keyPrefix + ":";
        this.timeout = timeout;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ValueWrapper get(Object key) {
        return wrapValueIfNotNull(redisTemplate.opsForValue().get(keyPrefix + key));
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return convert2(redisTemplate.opsForValue().get(keyPrefix + key), type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return convert2(redisTemplate.opsForValue().get(keyPrefix + key), valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        String newKey = keyPrefix + key;
        redisTemplate.opsForValue().set(newKey, value);
        redisTemplate.expire(newKey, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void evict(Object key) {
        redisTemplate.delete(keyPrefix + key);
    }

    @Override
    public void clear() {
        redisTemplate.delete(redisTemplate.keys(keyPrefix + "*"));
    }

    /**
     * 在某个时间点过期
     *
     * @param key 键
     * @param date 日期
     *
     * @since 1.1.3
     */
    public void expireAt(String key, Date date) {
        redisTemplate.expireAt(keyPrefix + key, date);
    }
}
