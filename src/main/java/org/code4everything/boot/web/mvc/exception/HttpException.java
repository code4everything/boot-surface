package org.code4everything.boot.web.mvc.exception;

import org.code4everything.boot.base.bean.BaseBean;
import org.code4everything.boot.base.constant.MessageConsts;
import org.springframework.http.HttpStatus;

/**
 * 自定义异常
 *
 * @author pantao
 * @since 2018/11/30
 **/
public class HttpException extends RuntimeException implements ExceptionBiscuit, BaseBean {

    /**
     * 错误码
     *
     * @since 1.0.4
     */
    private int code = HttpStatus.INTERNAL_SERVER_ERROR.value();

    /**
     * 消息
     *
     * @since 1.0.4
     */
    private String msg = MessageConsts.UNKNOWN_ERROR_ZH;

    /**
     * HTTP状态
     *
     * @since 1.0.4
     */
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    /**
     * 无参构造函数
     *
     * @since 1.0.4
     */
    public HttpException() {}

    /**
     * 设置消息
     *
     * @param msg 消息
     *
     * @since 1.0.4
     */
    public HttpException(String msg) {
        super(msg);
        this.msg = msg;
    }

    /**
     * 设置消息和异常
     *
     * @param msg 消息
     * @param throwable 异常
     *
     * @since 1.0.4
     */
    public HttpException(String msg, Throwable throwable) {
        super(msg, throwable);
        this.msg = msg;
    }

    /**
     * 设置错误码和消息
     *
     * @param code 错误码
     * @param msg 消息
     *
     * @since 1.0.4
     */
    public HttpException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 设置错误码，消息和异常
     *
     * @param code 错误码
     * @param msg 消息
     * @param throwable 异常
     *
     * @since 1.0.4
     */
    public HttpException(int code, String msg, Throwable throwable) {
        super(msg, throwable);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 设置错误码和HTTP状态
     *
     * @param code 错误码
     * @param status HTTP状态
     *
     * @since 1.0.4
     */
    public HttpException(int code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }

    /**
     * 设置错误码，HTTP状态和异常
     *
     * @param code 错误码
     * @param status HTTP状态
     * @param throwable 异常
     *
     * @since 1.0.4
     */
    public HttpException(int code, HttpStatus status, Throwable throwable) {
        super(throwable);
        this.code = code;
        this.status = status;
    }

    /**
     * 设置错误码，HTTP状态和消息
     *
     * @param code 错误码
     * @param status HTTP状态
     * @param msg 消息
     *
     * @since 1.0.4
     */
    public HttpException(int code, HttpStatus status, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
        this.status = status;
    }

    /**
     * 设置错误码，HTTP状态，消息和异常
     *
     * @param code 错误码
     * @param status HTTP状态
     * @param msg 消息
     * @param throwable 异常
     *
     * @since 1.0.4
     */
    public HttpException(int code, HttpStatus status, String msg, Throwable throwable) {
        super(msg, throwable);
        this.msg = msg;
        this.code = code;
        this.status = status;
    }

    /**
     * 设置错误码，消息
     *
     * @param code 错误码
     * @param msg 消息
     * @param responseOk 适应响应成功
     *
     * @since 1.0.8
     */
    public HttpException(int code, String msg, boolean responseOk) {
        this.code = code;
        this.msg = msg;
        if (responseOk) {
            this.status = HttpStatus.OK;
        }
    }

    /**
     * 设置错误码，消息和异常
     *
     * @param code 错误码
     * @param msg 消息
     * @param responseOk 适应响应成功
     * @param throwable 异常
     *
     * @since 1.0.8
     */
    public HttpException(int code, String msg, boolean responseOk, Throwable throwable) {
        super(msg, throwable);
        this.code = code;
        this.msg = msg;
        if (responseOk) {
            this.status = HttpStatus.OK;
        }
    }

    /**
     * 响应成功状态码的异常
     *
     * @param code 错误码
     * @param msg 消息
     *
     * @return 响应成功状态码的异常
     *
     * @since 1.1.2
     */
    public static HttpException responseOk(int code, String msg) {
        return new HttpException(code, msg, true);
    }

    @Override
    public int getCode() {
        return code;
    }

    /**
     * 错误码
     *
     * @param code 错误码
     *
     * @return {@link HttpException}
     *
     * @since 1.0.4
     */
    public HttpException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    /**
     * 设置消息
     *
     * @param msg 消息
     *
     * @return {@link HttpException}
     *
     * @since 1.0.4
     */
    public HttpException setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * 设置HTTP状态
     *
     * @param status HTTP状态
     *
     * @return {@link HttpException}
     *
     * @since 1.0.4
     */
    public HttpException setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }
}