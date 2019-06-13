package org.code4everything.boot.cache;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存管理器
 *
 * @author pantao
 * @since 2019/6/13
 **/
public class BootCacheManager implements CacheManager {

    protected final Map<String, Cache> cacheMap;

    protected final Set<String> cacheNames;

    protected final CacheCreator cacheCreator;

    protected final boolean dynamic;

    public BootCacheManager(CacheCreator cacheCreator) {
        this(cacheCreator, 16);
    }

    public BootCacheManager(CacheCreator cacheCreator, int capacity) {
        Objects.requireNonNull(cacheCreator, "cache creator must not be null");
        this.dynamic = true;
        this.cacheCreator = cacheCreator;
        cacheMap = new ConcurrentHashMap<>(capacity);
        cacheNames = new HashSet<>(capacity);
    }

    public BootCacheManager(Collection<Cache> caches, CacheCreator cacheCreator) {
        Objects.requireNonNull(caches, "cache list must not null null");
        this.dynamic = ObjectUtil.isNotNull(cacheCreator);
        this.cacheCreator = cacheCreator;
        int capacity = caches.size() * 4 / 3 + 1;
        cacheMap = new ConcurrentHashMap<>(capacity);
        cacheNames = new HashSet<>(capacity);
        caches.forEach(cache -> {
            cacheMap.put(cache.getName(), cache);
            cacheNames.add(cache.getName());
        });
    }

    public BootCacheManager(Collection<Cache> caches) {
        this(caches, null);
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if (cache == null && dynamic) {
            synchronized (cacheMap) {
                cache = cacheMap.get(name);
                if (cache == null) {
                    cache = cacheCreator.createCache(name);
                    cacheNames.add(name);
                }
            }
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheNames;
    }
}
