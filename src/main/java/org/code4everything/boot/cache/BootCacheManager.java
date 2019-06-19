package org.code4everything.boot.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.base.constant.StringConsts;
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

    /**
     * 缓存名与缓存的映射
     *
     * @since 1.1.3
     */
    protected final Map<String, Cache> cacheMap;

    /**
     * 缓存名集合
     *
     * @since 1.1.3
     */
    protected final Set<String> cacheNames;

    /**
     * 缓存创建者
     *
     * @since 1.1.3
     */
    protected final CacheCreator cacheCreator;

    /**
     * 是否可以动态的创建缓存
     *
     * @since 1.1.3
     */
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
                    cacheMap.put(name, cache);
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

    /**
     * 请求一个缓存必须存在
     *
     * @param cacheName 缓存名
     *
     * @return 对应的缓存
     *
     * @since 1.1.3
     */
    public Cache requireCache(String cacheName) {
        Cache cache = getCache(cacheName);
        Objects.requireNonNull(cache, "cache '" + cacheName + "' not exists");
        return cache;
    }

    /**
     * 新增对象缓存
     *
     * @param cacheName 缓存名
     * @param key 键
     * @param value 值
     *
     * @since 1.1.3
     */
    public void putVal(String cacheName, String key, Object value) {
        Cache cache = getCache(cacheName);
        if (ObjectUtil.isNotNull(cache)) {
            cache.put(key, value);
        }
    }

    /**
     * 添加一个对象缓存，如果缓存是列表的话
     *
     * @param cacheName 缓存名
     * @param key 键
     * @param value 值
     *
     * @since 1.1.3
     */
    @SuppressWarnings("unchecked")
    public void addVal(String cacheName, String key, Object value) {
        Cache cache = getCache(cacheName);
        if (ObjectUtil.isNotNull(cache)) {
            Object object = cache.get(key);
            if (object instanceof Collection) {
                ((Collection) object).add(value);
            }
        }
    }

    /**
     * 删除一个对象缓存，works on all caches if cacheName==null || cacheName=="" || cacheName=="*"
     *
     * @param cacheName 缓存名
     * @param key 键
     *
     * @since 1.1.3
     */
    public void delVal(String cacheName, String key) {
        if (StrUtil.isEmpty(cacheName) || StringConsts.Sign.STAR.equals(cacheName)) {
            cacheMap.values().forEach(cache -> cache.evict(key));
            return;
        }
        Cache cache = getCache(cacheName);
        if (ObjectUtil.isNotNull(cache)) {
            cache.evict(key);
        }
    }

    /**
     * 从缓存列表中移除缓存
     *
     * @param cacheName 缓存名称
     * @param key 键
     * @param removable 移除条件的回调
     * @param <T> 值类型
     *
     * @since 1.1.3
     */
    public <T> void delVal(String cacheName, String key, CacheRemovable<T> removable) {
        Collection<T> collection = getValByType(cacheName, key);
        if (CollUtil.isEmpty(collection)) {
            return;
        }
        collection.removeIf(removable::shouldRemove);
    }

    /**
     * 删除所有对象缓存，works on all caches if cacheName==null || cacheName=="" || cacheName=="*"
     *
     * @param cacheName 缓存名
     *
     * @since 1.1.3
     */
    public void removeAll(String cacheName) {
        if (StrUtil.isEmpty(cacheName) || StringConsts.Sign.STAR.equals(cacheName)) {
            cacheMap.values().forEach(Cache::clear);
            return;
        }
        Cache cache = getCache(cacheName);
        if (ObjectUtil.isNotNull(cache)) {
            cache.clear();
        }
    }

    /**
     * 获取缓存的对象
     *
     * @param cacheName 缓存名
     * @param key 键
     *
     * @return 缓存的对象
     *
     * @since 1.1.3
     */
    public Object getVal(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        if (ObjectUtil.isNotNull(cache)) {
            Cache.ValueWrapper wrapper = cache.get(key);
            return Objects.isNull(wrapper) ? null : wrapper.get();
        }
        return null;
    }

    /**
     * 获取缓存的对象，自动类型推断
     *
     * @param cacheName 缓存名
     * @param key 键
     * @param <T> 目标类型
     *
     * @return 缓存的对象
     *
     * @since 1.1.3
     */
    @SuppressWarnings("unchecked")
    public <T> T getValByType(String cacheName, String key) {
        Object value = getVal(cacheName, key);
        return Objects.isNull(value) ? null : (T) value;
    }
}
