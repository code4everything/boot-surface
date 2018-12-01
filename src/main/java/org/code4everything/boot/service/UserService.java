package org.code4everything.boot.service;

import java.io.Serializable;

/**
 * 用户服务
 *
 * @author pantao
 * @since 2018-12-01
 */
public interface UserService<T extends Serializable> {

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
