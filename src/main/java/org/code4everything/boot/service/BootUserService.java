package org.code4everything.boot.service;

import java.util.Map;

/**
 * 用户服务
 *
 * @author pantao
 * @since 2018-12-01
 */
public interface BootUserService<T> {

    /**
     * 获取用户
     *
     * @param token Token
     *
     * @return 用户
     *
     * @since 1.0.4
     */
    T getUserByToken(String token);

    /**
     * 从缓存中移除
     *
     * @param token Token
     *
     * @since 1.1.0
     */
    default void removeFromCache(String token) {removeFromCache(token, null);}

    /**
     * 从缓存中移除
     *
     * @param token Token
     * @param user 用户
     *
     * @since 1.1.0
     */
    default void removeFromCache(String token, T user) {}

    /**
     * 存入缓存
     *
     * @param token Token
     * @param user 用户
     *
     * @since 1.1.0
     */
    default void put2cache(String token, T user) {}

    /**
     * 自定义查询
     *
     * @param params 参数集合
     *
     * @return 用户
     *
     * @since 1.1.0
     */
    default T getUserBy(Map<String, String> params) {return null;}

    /**
     * 自定义查询
     *
     * @param params 参数集合
     *
     * @return 用户
     *
     * @since 1.1.0
     */
    default T getUserBy(String... params) {return null;}
}
