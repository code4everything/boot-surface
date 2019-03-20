package org.code4everything.boot.bean;

import java.io.Serializable;

/**
 * 日志信息临时存储类
 *
 * @param <T> 日志表
 *
 * @author pantao
 * @since 2018/12/03
 */
public class LogExBean<T> implements BaseBean, Serializable {

    /**
     * 可能抛出的异常
     *
     * @since 1.0.4
     */
    private Throwable throwable;

    /**
     * 日志
     *
     * @since 1.0.4
     */
    private T log;

    /**
     * 执行结果
     *
     * @since 1.0.4
     */
    private Object result;

    /**
     * 设置内容
     *
     * @param log 日志
     * @param throwable 可能抛出的异常
     * @param result 执行结果
     *
     * @since 1.0.4
     */
    public LogExBean(T log, Throwable throwable, Object result) {
        this.log = log;
        this.throwable = throwable;
        this.result = result;
    }

    /**
     * 获取抛出的异常
     *
     * @return 抛出的异常
     *
     * @since 1.0.4
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * 获取日志信息
     *
     * @return 日志信息
     *
     * @since 1.0.4
     */
    public T getLog() {
        return log;
    }

    /**
     * 获取执行结果
     *
     * @return 执行结果
     *
     * @since 1.0.4
     */
    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "LogExBean{" + "throwable=" + throwable + ", log=" + log + ", result=" + result + '}';
    }
}
