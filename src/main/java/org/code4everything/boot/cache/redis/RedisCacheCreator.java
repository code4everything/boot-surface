package org.code4everything.boot.cache.redis;

import org.code4everything.boot.cache.CacheCreator;
import org.code4everything.boot.module.redis.RedisTemplateUtils;

import java.util.Map;
import java.util.Objects;

/**
 * Redis缓存创建者
 *
 * @author pantao
 * @since 2019/6/14
 **/
public class RedisCacheCreator implements CacheCreator {

    /**
     * @since 1.1.3
     */
    protected final long timeout;

    /**
     * @since 1.1.3
     */
    protected final Map<String, String> prefixMap;

    /**
     * 构造函数
     *
     * @since 1.1.3
     */
    public RedisCacheCreator() {
        this(Long.MAX_VALUE, null);
    }

    /**
     * 构造函数
     *
     * @param prefixMap 缓存名与缓存键前缀的映射（key is the name of the cache, value is cache's key prefix）
     *
     * @since 1.1.3
     */
    public RedisCacheCreator(Map<String, String> prefixMap) {
        this(Long.MAX_VALUE, prefixMap);
    }

    /**
     * 构造函数
     *
     * @param timeout 过期时长
     *
     * @since 1.1.3
     */
    public RedisCacheCreator(long timeout) {
        this(timeout, null);
    }

    /**
     * 构造函数
     *
     * @param timeout 过期时长
     * @param prefixMap 缓存名与缓存键前缀的映射（key is the name of the cache, value is cache's key prefix）
     *
     * @since 1.1.3
     */
    public RedisCacheCreator(long timeout, Map<String, String> prefixMap) {
        this.timeout = timeout;
        this.prefixMap = prefixMap;
    }

    @Override
    public RedisCache createCache(String cacheName) {
        String prefix = Objects.isNull(prefixMap) ? cacheName : prefixMap.getOrDefault(cacheName, cacheName);
        return new RedisCache(cacheName, RedisTemplateUtils.newTemplate(), timeout, prefix);
    }
}
