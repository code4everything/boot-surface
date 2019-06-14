package org.code4everything.boot.cache;

import org.springframework.cache.Cache;

/**
 * 缓存创建者
 *
 * @author pantao
 * @since 2019/6/13
 **/
@FunctionalInterface
public interface CacheCreator {

    /**
     * 创建缓存
     *
     * @param cacheName 缓存名
     *
     * @return 缓存
     *
     * @since 1.1.3
     */
    Cache createCache(String cacheName);
}
