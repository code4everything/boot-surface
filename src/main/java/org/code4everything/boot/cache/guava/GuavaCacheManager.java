package org.code4everything.boot.cache.guava;

import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.cache.BootCacheManager;
import org.code4everything.boot.cache.CacheCreator;
import org.springframework.cache.Cache;

import java.util.Collection;

/**
 * 谷歌Guava缓存管理器
 *
 * @author pantao
 * @since 2019/6/13
 **/
public class GuavaCacheManager extends BootCacheManager {

    public GuavaCacheManager(CacheBuilder<Object, Object> cacheBuilder) {
        this(cacheBuilder, 16);
    }

    public GuavaCacheManager(CacheBuilder<Object, Object> cacheBuilder, int capacity) {
        this(new GuavaCacheCreator(cacheBuilder), capacity);
    }

    public GuavaCacheManager(CacheCreator cacheCreator) {
        this(cacheCreator, 16);
    }

    public GuavaCacheManager(CacheCreator cacheCreator, int capacity) {
        super(cacheCreator, capacity);
    }

    public GuavaCacheManager(Collection<Cache> caches, CacheCreator cacheCreator) {
        super(caches, cacheCreator);
    }

    public GuavaCacheManager(Collection<Cache> caches, CacheBuilder<Object, Object> cacheBuilder) {
        super(caches, new GuavaCacheCreator(cacheBuilder));
    }

    public GuavaCacheManager(Collection<Cache> caches) {
        super(caches);
    }
}
