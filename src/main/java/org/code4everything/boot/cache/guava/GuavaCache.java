package org.code4everything.boot.cache.guava;

import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.cache.AbstractCache;

import java.util.concurrent.Callable;

/**
 * 谷歌Guava缓存
 *
 * @author pantao
 * @since 2019/6/13
 **/
public class GuavaCache extends AbstractCache {

    /**
     * @since 1.1.3
     */
    private final com.google.common.cache.Cache<Object, Object> cache;

    /**
     * 构造函数
     *
     * @param name 缓存名
     * @param cache {@link com.google.common.cache.Cache}
     *
     * @since 1.1.3
     */
    public GuavaCache(String name, com.google.common.cache.Cache<Object, Object> cache) {
        super(name, cache);
        this.cache = cache;
    }

    /**
     * 构造函数
     *
     * @param name 缓存名
     * @param cacheBuilder {@link CacheBuilder}
     *
     * @since 1.1.3
     */
    public GuavaCache(String name, CacheBuilder<Object, Object> cacheBuilder) {
        this(name, cacheBuilder.build());
    }

    @Override
    public ValueWrapper get(Object key) {
        return wrapValueIfNotNull(cache.getIfPresent(key));
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return convert2(cache.getIfPresent(key), type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return convert2(cache.getIfPresent(key), valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }
}
