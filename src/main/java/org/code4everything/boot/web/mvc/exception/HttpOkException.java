package org.code4everything.boot.web.mvc.exception;

import org.springframework.http.HttpStatus;

/**
 * 响应成功状态码的异常
 *
 * @author pantao
 * @since 2019/4/30
 **/
public class HttpOkException extends BootException {

    /**
     * 响应成功状态码的异常
     *
     * @param code 错误码
     * @param msg 消息
     *
     * @since 1.1.1
     */
    public HttpOkException(int code, String msg) {
        super(code, msg, true);
    }

    /**
     * 响应自定义状态码的异常
     *
     * @param code 错误码
     * @param msg 消息
     * @param status 状态码
     *
     * @since 1.1.1
     */
    public HttpOkException(int code, String msg, HttpStatus status) {
        super(code, status, msg);
    }
}
