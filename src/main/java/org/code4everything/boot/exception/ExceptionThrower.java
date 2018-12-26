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
     * @param throwable 异常
     *
     * @return {@link ExceptionThrower}
     *
     * @throws Throwable 异常
     * @since 1.0.5
     */
    public ExceptionThrower throwIf(BooleanFunction function, Throwable throwable) throws Throwable {
        return throwIf(function.get(), throwable);
    }

    /**
     * 抛出异常
     *
     * @param shouldThrow 是否抛出异常
     * @param throwable 异常
     *
     * @return {@link ExceptionThrower}
     *
     * @throws Throwable 异常
     * @since 1.0.5
     */
    public ExceptionThrower throwIf(boolean shouldThrow, Throwable throwable) throws Throwable {
        if (shouldThrow) {
            throw throwable;
        }
        return this;
    }
}
