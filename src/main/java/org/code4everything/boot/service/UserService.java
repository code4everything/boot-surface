package org.code4everything.boot.service;

/**
 * 用户服务
 *
 * @author pantao
 * @since 2018-12-01
 */
public interface UserService<T> {

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
}
