package org.code4everything.boot.cache.redis;

import cn.hutool.core.util.ObjectUtil;
import org.code4everything.boot.cache.BootCacheManager;
import org.springframework.cache.Cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Redis缓存管理器
 *
 * @author pantao
 * @since 2019/6/14
 **/
public class RedisCacheManager extends BootCacheManager {

    public RedisCacheManager(RedisCacheCreator redisCacheCreator) {
        this(redisCacheCreator, 16);
    }

    public RedisCacheManager(RedisCacheCreator redisCacheCreator, int capacity) {
        super(redisCacheCreator, capacity);
    }

    public RedisCacheManager(Collection<RedisCache> caches, RedisCacheCreator redisCacheCreator) {
        super(new ArrayList<>(caches), redisCacheCreator);
    }

    public RedisCacheManager(Collection<RedisCache> caches) {
        this(caches, null);
    }

    /**
     * 在某个时间点过期
     *
     * @param cacheName 缓存名
     * @param key 键
     * @param date 日期
     *
     * @since 1.1.3
     */
    public void expireAt(String cacheName, String key, Date date) {
        Cache cache = getCache(cacheName);
        if (ObjectUtil.isNotNull(cache)) {
            ((RedisCache) cache).expireAt(key, date);
        }
    }
}
