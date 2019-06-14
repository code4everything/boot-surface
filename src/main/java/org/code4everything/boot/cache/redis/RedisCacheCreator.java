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

    private final long timeout;

    private final Map<String, String> prefixMap;

    public RedisCacheCreator() {
        this(Long.MAX_VALUE, null);
    }

    public RedisCacheCreator(Map<String, String> prefixMap) {
        this(Long.MAX_VALUE, prefixMap);
    }

    public RedisCacheCreator(long timeout) {
        this(timeout, null);
    }

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
