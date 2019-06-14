package org.code4everything.boot.cache;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
        cacheNames = new ConcurrentHashSet<>(capacity);
    }

    public BootCacheManager(Collection<Cache> caches, CacheCreator cacheCreator) {
        Objects.requireNonNull(caches, "cache list must not null null");
        this.dynamic = ObjectUtil.isNotNull(cacheCreator);
        this.cacheCreator = cacheCreator;
        int capacity = caches.size() * 4 / 3 + 1;
        cacheMap = new ConcurrentHashMap<>(capacity);
        cacheNames = new ConcurrentHashSet<>(capacity);
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

    public Cache requireCache(String cacheName) {
        Cache cache = getCache(cacheName);
        Objects.requireNonNull(cache, "cache '" + cacheName + "' has no config");
        return cache;
    }

    public void putVal(String cacheName, String key, Object value) {
        Cache cache = getCache(cacheName);
        if (ObjectUtil.isNotNull(cache)) {
            cache.put(key, value);
        }
    }

    public void delVal(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        if (ObjectUtil.isNotNull(cache)) {
            cache.evict(key);
        }
    }

    public void removeAll(String cacheName) {
        Cache cache = getCache(cacheName);
        if (ObjectUtil.isNotNull(cache)) {
            cache.clear();
        }
    }

    public Object getVal(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        if (ObjectUtil.isNotNull(cache)) {
            Cache.ValueWrapper wrapper = cache.get(key);
            return Objects.isNull(wrapper) ? null : wrapper.get();
        }
        return null;
    }
}
