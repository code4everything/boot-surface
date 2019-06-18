package org.code4everything.boot.cache;

import cn.hutool.core.collection.CollUtil;
import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.cache.guava.GuavaCache;
import org.code4everything.boot.cache.guava.GuavaCacheManager;
import org.code4everything.boot.cache.redis.RedisCache;
import org.code4everything.boot.cache.redis.RedisCacheCreator;
import org.code4everything.boot.cache.redis.RedisCacheManager;
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

    // -------------------------------------------Redis-----------------------------------------------------------------

    /**
     * 新建缓存管理器
     *
     * @param redisCacheCreatorMap 缓存名与缓存创建者的映射
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static RedisCacheManager newRedisCacheManager(Map<String, RedisCacheCreator> redisCacheCreatorMap) {
        return newRedisCacheManager(redisCacheCreatorMap, null);
    }

    /**
     * 新建缓存管理器
     *
     * @param redisCacheCreatorMap 缓存名与缓存创建者的映射
     * @param defaultRedisCacheCreator 缓存创建者
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static RedisCacheManager newRedisCacheManager(Map<String, RedisCacheCreator> redisCacheCreatorMap,
                                                         RedisCacheCreator defaultRedisCacheCreator) {
        Collection<RedisCache> caches = new ArrayList<>();
        redisCacheCreatorMap.forEach((k, v) -> caches.add(v.createCache(k)));
        return new RedisCacheManager(caches, defaultRedisCacheCreator);
    }

    /**
     * 新建缓存管理器
     *
     * @param redisCacheCreator 缓存创建者
     * @param names 缓存名集合
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static RedisCacheManager newRedisCacheManager(RedisCacheCreator redisCacheCreator, String... names) {
        return newRedisCacheManager(redisCacheCreator, Arrays.asList(names));
    }

    /**
     * 新建缓存管理器
     *
     * @param redisCacheCreator 缓存创建者
     * @param names 缓存名集合
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static RedisCacheManager newRedisCacheManager(RedisCacheCreator redisCacheCreator,
                                                         Collection<String> names) {
        if (CollUtil.isEmpty(names)) {
            return new RedisCacheManager(redisCacheCreator);
        }
        Collection<RedisCache> caches = new ArrayList<>(names.size());
        names.forEach(name -> caches.add(redisCacheCreator.createCache(name)));
        return new RedisCacheManager(caches, redisCacheCreator);
    }

    // ---------------------------------------Guava---------------------------------------------------------------------

    /**
     * 新建缓存管理器
     *
     * @param cacheBuilderMap 缓存名与缓存创建者的映射
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static GuavaCacheManager newGuavaCacheManager(Map<String, CacheBuilder<Object, Object>> cacheBuilderMap) {
        return newGuavaCacheManager(cacheBuilderMap, null);
    }

    /**
     * 新建缓存管理器
     *
     * @param cacheBuilderMap 缓存名与缓存创建者的映射
     * @param defaultCacheBuilder 缓存创建者
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static GuavaCacheManager newGuavaCacheManager(Map<String, CacheBuilder<Object, Object>> cacheBuilderMap,
                                                         CacheBuilder<Object, Object> defaultCacheBuilder) {
        Collection<GuavaCache> caches = new ArrayList<>();
        cacheBuilderMap.forEach((k, v) -> caches.add(new GuavaCache(k, v.build())));
        return new GuavaCacheManager(caches, defaultCacheBuilder);
    }

    /**
     * 新建缓存管理器
     *
     * @param cacheBuilder 缓存创建者
     * @param names 缓存名集合
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static GuavaCacheManager newGuavaCacheManager(CacheBuilder<Object, Object> cacheBuilder, String... names) {
        return newGuavaCacheManager(cacheBuilder, Arrays.asList(names));
    }

    /**
     * 新建缓存管理器
     *
     * @param cacheBuilder 缓存创建者
     * @param names 缓存名集合
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static GuavaCacheManager newGuavaCacheManager(CacheBuilder<Object, Object> cacheBuilder,
                                                         Collection<String> names) {
        if (CollUtil.isEmpty(names)) {
            return new GuavaCacheManager(cacheBuilder);
        }
        Collection<GuavaCache> caches = new ArrayList<>(names.size());
        names.forEach(name -> caches.add(new GuavaCache(name, cacheBuilder.build())));
        return new GuavaCacheManager(caches, cacheBuilder);
    }

    // --------------------------------------------Custom---------------------------------------------------------------

    /**
     * 新建缓存管理器
     *
     * @param cacheCreatorMap 缓存名与缓存创建者的映射
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static BootCacheManager newCacheManager(Map<String, CacheCreator> cacheCreatorMap) {
        return newCacheManager(cacheCreatorMap, null);
    }

    /**
     * 新建缓存管理器
     *
     * @param cacheCreatorMap 缓存名与缓存创建者的映射
     * @param defaultCacheCreator 缓存创建者
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static BootCacheManager newCacheManager(Map<String, CacheCreator> cacheCreatorMap,
                                                   CacheCreator defaultCacheCreator) {
        Collection<Cache> caches = new ArrayList<>();
        cacheCreatorMap.forEach((k, v) -> caches.add(v.createCache(k)));
        return new BootCacheManager(caches, defaultCacheCreator);
    }

    /**
     * 新建缓存管理器
     *
     * @param cacheCreator 缓存创建者
     * @param names 缓存名集合
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static BootCacheManager newCacheManager(CacheCreator cacheCreator, String... names) {
        return newCacheManager(cacheCreator, Arrays.asList(names));
    }

    /**
     * 新建缓存管理器
     *
     * @param cacheCreator 缓存创建者
     * @param names 缓存名集合
     *
     * @return 缓存管理器
     *
     * @since 1.1.3
     */
    public static BootCacheManager newCacheManager(CacheCreator cacheCreator, Collection<String> names) {
        if (CollUtil.isEmpty(names)) {
            return new BootCacheManager(cacheCreator);
        }
        Collection<Cache> caches = new ArrayList<>(names.size());
        names.forEach(name -> caches.add(cacheCreator.createCache(name)));
        return new BootCacheManager(caches, cacheCreator);
    }
}
