package org.code4everything.boot.log;

import org.code4everything.boot.base.bean.BaseBean;

import java.io.Serializable;

/**
 * 日志信息
 *
 * @author pantao
 * @since 2018/11/3
 */
public class MethodLog implements BaseBean, Serializable {

    private static final long serialVersionUID = -5160585058519076100L;

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
     * 参数集合
     *
     * @since 1.0.0
     */
    private String args;

    /**
     * 方法描述
     *
     * @since 1.0.0
     */
    private String description;

    /**
     * 方法执行时长（毫秒）
     *
     * @since 1.0.4
     */
    private Long executedTime;

    /**
     * 获取方法执行时长（毫秒）
     *
     * @return 发放执行时长（毫秒）
     *
     * @since 1.0.4
     */
    public Long getExecutedTime() {
        return executedTime;
    }

    /**
     * 设置方法执行时长（毫秒）
     *
     * @param executedTime 发放执行时长
     *
     * @return {@link MethodLog}
     *
     * @since 1.0.4
     */
    public MethodLog setExecutedTime(Long executedTime) {
        this.executedTime = executedTime;
        return this;
    }

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
     * @return {@link MethodLog}
     *
     * @since 1.0.0
     */
    public MethodLog setClassName(String className) {
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
     * @return {@link MethodLog}
     *
     * @since 1.0.0
     */
    public MethodLog setMethodName(String methodName) {
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
    public String getArgs() {
        return args;
    }

    /**
     * 设置参数
     *
     * @param args 参数
     *
     * @return {@link MethodLog}
     *
     * @since 1.0.0
     */
    public MethodLog setArgs(String args) {
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
     * @return {@link MethodLog}
     *
     * @since 1.0.0
     */
    public MethodLog setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return "MethodLog{" + "className='" + className + '\'' + ", methodName='" + methodName + '\'' + ", args='" + args + '\'' + ", description='" + description + '\'' + ", executedTime=" + executedTime + '}';
    }
}
