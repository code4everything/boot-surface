package org.code4everything.boot.web.mvc.exception;

import org.code4everything.boot.base.bean.BaseBean;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 自定义异常信息
 *
 * @author pantao
 * @since 2018/11/15
 **/
public class ExceptionBread implements ExceptionBiscuit, BaseBean, Serializable {

    private static final long serialVersionUID = 1349750625515704986L;

    /**
     * 错误码
     *
     * @since 1.0.0
     */
    private int code;

    /**
     * 消息
     *
     * @since 1.0.0
     */
    private String msg;

    /**
     * 状态
     *
     * @since 1.0.0
     */
    private HttpStatus status;

    /**
     * 创建 {@link ExceptionBread}
     *
     * @return {@link ExceptionBread}
     *
     * @since 1.1.1
     */
    public static ExceptionBread create() {
        return new ExceptionBread();
    }

    @Override
    public int getCode() {
        return code;
    }

    /**
     * 设置错误码
     *
     * @param code 错误码
     *
     * @return {@link ExceptionBread}
     *
     * @since 1.0.0
     */
    public ExceptionBread setCode(int code) {
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
     * @return {@link ExceptionBread}
     *
     * @since 1.0.0
     */
    public ExceptionBread setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status {@link HttpStatus} 状态
     *
     * @return {@link ExceptionBread}
     *
     * @since 1.0.0
     */
    public ExceptionBread setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "ExceptionBread{" + "code=" + code + ", msg='" + msg + '\'' + ", status=" + status + '}';
    }
}
