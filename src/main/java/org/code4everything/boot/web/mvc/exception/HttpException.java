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
     * 默认消息
     *
     * @since 1.0.4
     */
    private static final String DEFAULT_MSG = MessageConsts.UNKNOWN_ERROR_ZH;

    /**
     * 默认状态码
     *
     * @since 1.1.2
     */
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    /**
     * HTTP状态
     *
     * @since 1.0.4
     */
    private final HttpStatus status;

    /**
     * 错误码
     *
     * @since 1.0.4
     */
    private final int code;

    /**
     * 无参构造函数
     *
     * @since 1.0.4
     */
    public HttpException() {
        this(DEFAULT_MSG);
    }

    /**
     * 设置消息
     *
     * @param msg 消息
     *
     * @since 1.0.4
     */
    public HttpException(String msg) {
        this(DEFAULT_STATUS, msg);
    }

    /**
     * 设置HTTP状态
     *
     * @param status HTTP状态
     *
     * @since 1.1.2
     */
    public HttpException(HttpStatus status) {
        this(status, DEFAULT_MSG);
    }

    /**
     * 设置HTTP状态和消息
     *
     * @param status HTTP状态
     * @param msg 消息
     *
     * @since 1.1.2
     */
    public HttpException(HttpStatus status, String msg) {
        this(status.value(), status, msg);
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
        this(code, DEFAULT_STATUS, msg);
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
        this(code, status, DEFAULT_MSG);
    }

    /**
     * 设置错误码，HTTP状态和消息，通常使用一个实现 {@link ExceptionBiscuit} 接口的枚举类来定义系统异常情况
     *
     * @param biscuit {@link ExceptionBiscuit}
     *
     * @since 1.1.2
     */
    public HttpException(ExceptionBiscuit biscuit) {
        this(biscuit.getCode(), biscuit.getStatus(), biscuit.getMsg());
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
        this(code, responseOk ? HttpStatus.OK : DEFAULT_STATUS, msg);
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

    @Override
    public String getMsg() {
        return getMessage();
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
