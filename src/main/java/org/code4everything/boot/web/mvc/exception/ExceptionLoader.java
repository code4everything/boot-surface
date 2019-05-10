package org.code4everything.boot.web.mvc.exception;

import org.springframework.http.HttpStatus;

/**
 * @author pantao
 * @since 2019/5/10
 **/
@FunctionalInterface
public interface ExceptionLoader<T extends HttpException> {

    /**
     * 构造异常
     *
     * @param code 错误码
     * @param msg 消息
     * @param status 响应状态码
     *
     * @return 异常
     *
     * @since 1.1.2
     */
    T load(int code, HttpStatus status, String msg);
}
