package org.code4everything.boot.exception;

import org.code4everything.boot.base.function.BooleanFunction;

/**
 * 异常抛出类
 *
 * @author pantao
 * @since 2018-12-25
 */
public class ExceptionThrower {

    private static final ExceptionThrower THROWER = new ExceptionThrower();

    private ExceptionThrower() {}

    /**
     * 创建对象
     *
     * @return {@link ExceptionThrower}
     *
     * @since 1.0.5
     */
    public static ExceptionThrower create() {
        return new ExceptionThrower();
    }

    /**
     * 获取单例对象
     *
     * @return {@link ExceptionThrower}
     *
     * @since 1.0.5
     */
    public static ExceptionThrower getInstance() {
        return THROWER;
    }

    /**
     * 抛出异常
     *
     * @param function 布尔函数
     * @param exception 异常
     *
     * @return {@link ExceptionThrower}
     *
     * @since 1.0.5
     */
    public ExceptionThrower throwIf(BooleanFunction function, RuntimeException exception) {
        return throwIf(function.call(), exception);
    }

    /**
     * 抛出异常
     *
     * @param shouldThrow 是否抛出异常
     * @param exception 想要抛出的异常
     *
     * @return {@link ExceptionThrower}
     *
     * @since 1.0.5
     */
    public ExceptionThrower throwIf(boolean shouldThrow, RuntimeException exception) {
        if (shouldThrow) {
            throw exception;
        }
        return this;
    }
}
