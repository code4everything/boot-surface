package org.code4everything.boot.web.mvc;

import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.base.function.BooleanFunction;
import org.code4everything.boot.web.mvc.exception.ExceptionFactory;
import org.code4everything.boot.web.mvc.exception.ExceptionThrower;

import java.util.Objects;

/**
 * 断言封装类
 *
 * @author pantao
 * @since 2018-12-01
 */
public final class AssertUtils {

    private static final ExceptionThrower THROWER = ExceptionThrower.getInstance();

    private AssertUtils() {}

    /**
     * 断言Token不为空
     *
     * @param token Token
     *
     * @return Token
     *
     * @since 1.0.4
     */
    public static String assertTokenNotBlank(String token) {
        THROWER.throwIf(StrUtil.isBlank(token), ExceptionFactory.tokenBlank());
        return token;
    }

    /**
     * 断言用户已登录
     *
     * @param user 用户
     * @param <T> 用户
     *
     * @return 用户
     *
     * @since 1.0.4
     */
    public static <T> T assertUserLoggedIn(T user) {
        THROWER.throwIf(Objects.isNull(user), ExceptionFactory.userNotLoggedIn());
        return user;
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
    public static ExceptionThrower throwIf(boolean shouldThrow, RuntimeException exception) {
        return THROWER.throwIf(shouldThrow, exception);
    }

    /**
     * 抛出异常
     *
     * @param function 布尔函数
     * @param exception 想要抛出的异常
     *
     * @return {@link ExceptionThrower}
     *
     * @since 1.0.5
     */
    public static ExceptionThrower throwIf(BooleanFunction function, RuntimeException exception) {
        return THROWER.throwIf(function, exception);
    }
}
