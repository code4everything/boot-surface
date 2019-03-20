package org.code4everything.boot.bean;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 自定义异常信息
 *
 * @author pantao
 * @since 2018/11/15
 **/
public class ExceptionBean implements BaseBean, Serializable {

    /**
     * 错误码
     *
     * @since 1.0.0
     */
    private Integer code;

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
     * 获取错误码
     *
     * @return 错误码
     *
     * @since 1.0.0
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置错误码
     *
     * @param code 错误码
     *
     * @return {@link ExceptionBean}
     *
     * @since 1.0.0
     */
    public ExceptionBean setCode(Integer code) {
        this.code = code;
        return this;
    }

    /**
     * 获取消息
     *
     * @return 消息
     *
     * @since 1.0.0
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置消息
     *
     * @param msg 消息
     *
     * @return {@link ExceptionBean}
     *
     * @since 1.0.0
     */
    public ExceptionBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 获取状态
     *
     * @return 状态
     *
     * @since 1.0.0
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status {@link HttpStatus} 状态
     *
     * @return {@link ExceptionBean}
     *
     * @since 1.0.0
     */
    public ExceptionBean setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "ExceptionBean{" + "code=" + code + ", msg='" + msg + '\'' + ", status=" + status + '}';
    }
}
