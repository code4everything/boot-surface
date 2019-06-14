package org.code4everything.boot.cache;

import cn.hutool.core.collection.CollUtil;
import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.cache.guava.GuavaCache;
import org.code4everything.boot.cache.guava.GuavaCacheManager;
import org.springframework.cache.Cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Spring Cache工具类
 *
 * @author pantao
 * @since 2019/6/13
 **/
public class CacheUtils {

    private CacheUtils() {}

    // ---------------------------------------Guava---------------------------------------------------------------------

    public static GuavaCacheManager newGuavaCacheManager(Map<String, CacheBuilder<Object, Object>> cacheBuilderMap) {
        return newGuavaCacheManager(cacheBuilderMap, null);
    }


    public static GuavaCacheManager newGuavaCacheManager(Map<String, CacheBuilder<Object, Object>> cacheBuilderMap,
                                                         CacheBuilder<Object, Object> defaultCacheBuilder) {
        Collection<Cache> caches = new ArrayList<>();
        cacheBuilderMap.forEach((k, v) -> caches.add(new GuavaCache(k, v.build())));
        return new GuavaCacheManager(caches, defaultCacheBuilder);
    }

    public static GuavaCacheManager newGuavaCacheManager(CacheBuilder<Object, Object> cacheBuilder, String... names) {
        return newGuavaCacheManager(cacheBuilder, Arrays.asList(names));
    }

    public static GuavaCacheManager newGuavaCacheManager(CacheBuilder<Object, Object> cacheBuilder,
                                                         Collection<String> names) {
        if (CollUtil.isEmpty(names)) {
            return new GuavaCacheManager(cacheBuilder);
        }
        Collection<Cache> caches = new ArrayList<>(names.size());
        for (String name : names) {
            caches.add(new GuavaCache(name, cacheBuilder.build()));
        }
        return new GuavaCacheManager(caches, cacheBuilder);
    }

    // --------------------------------------------Custom---------------------------------------------------------------

    public static GuavaCacheManager newCacheManager(Map<String, CacheCreator> cacheCreatorMap) {
        return newCacheManager(cacheCreatorMap, null);
    }

    public static GuavaCacheManager newCacheManager(Map<String, CacheCreator> cacheCreatorMap,
                                                    CacheCreator defaultCacheCreator) {
        Collection<Cache> caches = new ArrayList<>();
        cacheCreatorMap.forEach((k, v) -> caches.add(v.createCache(k)));
        return new GuavaCacheManager(caches, defaultCacheCreator);
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
