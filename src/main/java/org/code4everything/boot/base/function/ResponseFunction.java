package org.code4everything.boot.base.function;

import org.code4everything.boot.base.bean.Response;

/**
 * 结果响应函数
 *
 * @author pantao
 * @since 2018-12-26
 */
@FunctionalInterface
public interface ResponseFunction<T> {

    /**
     * 返回响应结果函数
     *
     * @return 响应结果
     *
     * @since 1.0.5
     */
    Response<T> call();
}
