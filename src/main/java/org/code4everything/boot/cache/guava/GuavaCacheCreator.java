package org.code4everything.boot.cache.guava;

import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.cache.CacheCreator;

import java.util.Objects;

/**
 * 谷歌Guava缓存创建者
 *
 * @author pantao
 * @since 2019/6/13
 **/
public class GuavaCacheCreator implements CacheCreator {

    /**
     * @since 1.1.3
     */
    protected final CacheBuilder<Object, Object> cacheBuilder;

    /**
     * 构造函数
     *
     * @param cacheBuilder {@link CacheBuilder}
     *
     * @since 1.1.3
     */
    public GuavaCacheCreator(CacheBuilder<Object, Object> cacheBuilder) {
        Objects.requireNonNull(cacheBuilder, "guava cache builder must not be null");
        this.cacheBuilder = cacheBuilder;
    }

    @Override
    public GuavaCache createCache(String cacheName) {
        return new GuavaCache(cacheName, cacheBuilder.build());
    }
}
