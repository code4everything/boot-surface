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

    private final RedisTemplate<String, Object> redisTemplate;

    private final String keyPrefix;

    private final long timeout;

    public RedisCache(String name, RedisTemplate<String, Object> redisTemplate) {
        this(name, redisTemplate, Long.MAX_VALUE, name);
    }

    public RedisCache(String name, RedisTemplate<String, Object> redisTemplate, long timeout) {
        this(name, redisTemplate, timeout, name);
    }

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

    public void expireAt(String key, Date date) {
        redisTemplate.expireAt(keyPrefix + key, date);
    }
}
