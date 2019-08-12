package org.code4everything.boot.cache;

import org.code4everything.boot.config.BootConfig;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author pantao
 * @since 2019/6/14
 */
public abstract class AbstractCache implements Cache {

    private final String name;

    private final Object nativeCache;

    public AbstractCache(String name, Object nativeCache) {
        this.name = name;
        this.nativeCache = nativeCache;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return nativeCache;
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper wrapper = get(key);
        if (Objects.isNull(wrapper)) {
            put(key, value);
        }
        return wrapper;
    }

    protected ValueWrapper wrapValueIfNotNull(Object value) {
        return Objects.isNull(value) ? null : new SimpleValueWrapper(value);
    }

    @SuppressWarnings("unchecked")
    protected <T> T convert2(Object value, Class<T> type) {
        return Objects.isNull(value) ? null : (T) value;
    }

    @SuppressWarnings("unchecked")
    protected <T> T convert2(Object value, Callable<T> valueLoader) {
        if (Objects.isNull(value)) {
            try {
                return valueLoader.call();
            } catch (Exception e) {
                if (BootConfig.isDebug()) {
                    throw new RuntimeException("value loader error, details: " + e.getMessage(), e);
                }
                return null;
            }
        }
        return (T) value;
    }
}
