package org.code4everything.boot.bean;

import java.io.Serializable;

/**
 * 日志信息
 *
 * @author pantao
 * @since 2018/11/3
 */
public class LogBean implements Serializable {

    /**
     * 类名
     *
     * @since 1.0.0
     */
    private String className;

    /**
     * 方法名
     *
     * @since 1.0.0
     */
    private String methodName;

    /**
     * 参数
     *
     * @since 1.0.0
     */
    private Object[] args;

    /**
     * 方法描述
     *
     * @since 1.0.0
     */
    private String description;

    /**
     * 获取类名
     *
     * @return 类名
     *
     * @since 1.0.0
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置类名
     *
     * @param className 类名
     *
     * @return {@link LogBean}
     *
     * @since 1.0.0
     */
    public LogBean setClassName(String className) {
        this.className = className;
        return this;
    }

    /**
     * 获取方法名
     *
     * @return 方法名
     *
     * @since 1.0.0
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 设置方法名
     *
     * @param methodName 方法名
     *
     * @return {@link LogBean}
     *
     * @since 1.0.0
     */
    public LogBean setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    /**
     * 获取参数
     *
     * @return 参数
     *
     * @since 1.0.0
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * 设置参数
     *
     * @param args 参数
     *
     * @return {@link LogBean}
     *
     * @since 1.0.0
     */
    public LogBean setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    /**
     * 获取方法描述
     *
     * @return 方法描述
     *
     * @since 1.0.0
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置方法描述
     *
     * @param description 方法描述
     *
     * @return {@link LogBean}
     *
     * @since 1.0.0
     */
    public LogBean setDescription(String description) {
        this.description = description;
        return this;
    }
}
