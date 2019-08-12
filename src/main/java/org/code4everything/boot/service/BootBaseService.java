package org.code4everything.boot.service;

/**
 * @author pantao
 * @since 2019/6/11
 */
public interface BootBaseService {

    /**
     * 获取令牌，抛出未登录异常
     *
     * @return 令牌
     *
     * @since 1.1.3
     */
    String getToken();

    /**
     * 获取令牌
     *
     * @param require 是否抛出未登录异常
     *
     * @return 令牌
     *
     * @since 1.1.3
     */
    String getToken(boolean require);

    /**
     * 获取用户，当用户未登陆时抛出异常
     *
     * @return 用户
     *
     * @since 1.1.3
     */
    Object getUser();

    /**
     * 获取用户
     *
     * @param require 是否抛出用户未登录异常
     *
     * @return 用户
     *
     * @since 1.1.3
     */
    Object getUser(boolean require);
}
