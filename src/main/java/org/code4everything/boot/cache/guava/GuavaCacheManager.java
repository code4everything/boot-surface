package org.code4everything.boot.cache.guava;

import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.cache.BootCacheManager;

import java.util.ArrayList;
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

    public GuavaCacheManager(GuavaCacheCreator guavaCacheCreator) {
        this(guavaCacheCreator, 16);
    }

    public GuavaCacheManager(GuavaCacheCreator guavaCacheCreator, int capacity) {
        super(guavaCacheCreator, capacity);
    }

    public GuavaCacheManager(Collection<GuavaCache> caches, GuavaCacheCreator guavaCacheCreator) {
        super(new ArrayList<>(caches), guavaCacheCreator);
    }

    public GuavaCacheManager(Collection<GuavaCache> caches, CacheBuilder<Object, Object> cacheBuilder) {
        super(new ArrayList<>(caches), new GuavaCacheCreator(cacheBuilder));
    }

    public GuavaCacheManager(Collection<GuavaCache> caches) {
        super(new ArrayList<>(caches));
    }
}
