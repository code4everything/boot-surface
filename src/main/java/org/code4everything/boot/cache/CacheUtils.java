package org.code4everything.boot.cache;

import cn.hutool.core.collection.CollUtil;
import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.cache.guava.GuavaCacheCreator;
import org.code4everything.boot.cache.guava.GuavaCacheManager;
import org.springframework.cache.Cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Spring Cache工具类
 *
 * @author pantao
 * @since 2019/6/13
 **/
public class CacheUtils {

    private CacheUtils() {}

    public static GuavaCacheManager newGuavaCacheManager(CacheBuilder<Object, Object> cacheBuilder, String... names) {
        return newGuavaCacheManager(cacheBuilder, Arrays.asList(names));
    }

    public static GuavaCacheManager newGuavaCacheManager(CacheBuilder<Object, Object> cacheBuilder,
                                                         Collection<String> names) {
        CacheCreator cacheCreator = new GuavaCacheCreator(cacheBuilder);
        if (CollUtil.isEmpty(names)) {
            return new GuavaCacheManager(cacheCreator);
        }
        Collection<Cache> caches = new ArrayList<>(names.size());
        for (String name : names) {
            caches.add(cacheCreator.createCache(name));
        }
        return new GuavaCacheManager(caches, cacheCreator);
    }

    public static BootCacheManager newCacheManager(CacheCreator cacheCreator, String... names) {
        return newCacheManager(cacheCreator, Arrays.asList(names));
    }

    public static BootCacheManager newCacheManager(CacheCreator cacheCreator, Collection<String> names) {
        if (CollUtil.isEmpty(names)) {
            return new BootCacheManager(cacheCreator);
        }
        Collection<Cache> caches = new ArrayList<>(names.size());
        for (String name : names) {
            caches.add(cacheCreator.createCache(name));
        }
        return new BootCacheManager(caches, cacheCreator);
    }
}
