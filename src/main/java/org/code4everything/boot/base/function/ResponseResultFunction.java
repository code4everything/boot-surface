package org.code4everything.boot.base.function;

import org.code4everything.boot.bean.ResponseResult;

import java.io.Serializable;

/**
 * 响应结果返回函数
 *
 * @author pantao
 * @since 2018-12-26
 */
@FunctionalInterface
public interface ResponseResultFunction<T extends Serializable> {

    /**
     * 返回响应结果函数
     *
     * @return 响应结果
     *
     * @since 1.0.5
     */
    ResponseResult<T> get();
}
