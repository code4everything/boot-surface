package org.code4everything.boot.cache.guava;

import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.web.mvc.exception.ExceptionFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * 谷歌Guava缓存
 *
 * @author pantao
 * @since 2019/6/13
 **/
public class GuavaCache implements Cache {

    private final com.google.common.cache.Cache<Object, Object> cache;

    private final String name;

    public GuavaCache(String name, com.google.common.cache.Cache<Object, Object> cache) {
        this.name = name;
        this.cache = cache;
    }

    public GuavaCache(String name, CacheBuilder<Object, Object> cacheBuilder) {
        this(name, cacheBuilder.build());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = cache.getIfPresent(key);
        return Objects.isNull(value) ? null : new SimpleValueWrapper(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        return (T) cache.getIfPresent(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = cache.getIfPresent(key);
        if (Objects.isNull(value)) {
            try {
                return valueLoader.call();
            } catch (Exception e) {
                if (BootConfig.isDebug()) {
                    throw ExceptionFactory.exception(HttpStatus.INTERNAL_SERVER_ERROR, "value loader error");
                }
                return null;
            }
        }
        return (T) value;
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object val = cache.getIfPresent(key);
        if (Objects.isNull(val)) {
            put(key, value);
        }
        return new SimpleValueWrapper(val);
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
