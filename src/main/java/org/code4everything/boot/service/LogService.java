package org.code4everything.boot.service;

import org.code4everything.boot.bean.LogBean;

/**
 * 日志服务
 *
 * @author pantao
 * @since 2018/11/10
 */
public interface LogService<T> {

    /**
     * 保存日志信息
     *
     * @param log {@link T}
     *
     * @return {@link T}
     *
     * @since 1.0.0
     */
    T save(T log);

    /**
     * 保存异常信息
     *
     * @param log {@link T}
     * @param throwable 异常信息
     *
     * @return {@link T}
     *
     * @since 1.0.0
     */
    T saveException(T log, Throwable throwable);

    /**
     * 获取日志信息
     *
     * @param logBean {@link LogBean}
     *
     * @return 日志信息
     *
     * @since 1.0.0[
     */
    T getLog(LogBean logBean);
}
