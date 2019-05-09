package org.code4everything.boot.web.mvc;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.base.function.BooleanFunction;
import org.code4everything.boot.web.mvc.exception.ExceptionFactory;

import java.util.Objects;
import java.util.Optional;

/**
 * 断言封装类
 *
 * @author pantao
 * @since 2018-12-01
 */
public final class AssertUtils {

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
        throwIf(StrUtil.isBlank(token), ExceptionFactory.tokenBlank());
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
        throwIf(Objects.isNull(user), ExceptionFactory.userNotLoggedIn());
        return user;
    }

    /**
     * 抛出异常
     *
     * @param shouldThrow 是否抛出异常
     * @param exception 想要抛出的异常
     *
     * @since 1.0.5
     */
    public static void throwIf(boolean shouldThrow, RuntimeException exception) {
        if (shouldThrow) {
            throw exception;
        }
    }

    /**
     * 如果对象为空抛出异常
     *
     * @param optional {@link Optional}
     * @param exception 想要抛出的异常
     *
     * @since 1.1.2
     */
    public static void throwIf(Optional<?> optional, RuntimeException exception) {
        throwIf(!optional.isPresent(), exception);
    }

    /**
     * 抛出异常
     *
     * @param function 布尔函数
     * @param exception 想要抛出的异常
     *
     * @since 1.0.5
     */
    public static void throwIf(BooleanFunction function, RuntimeException exception) {
        throwIf(function.call(), exception);
    }

    /**
     * 如果对象空指针抛出异常
     *
     * @param object 对象
     * @param exception 想要抛出的异常
     *
     * @since 1.1.2
     */
    public static void throwIfNull(Object object, RuntimeException exception) {
        throwIf(Objects.isNull(object), exception);
    }

    /**
     * 如果对象不为空抛出异常
     *
     * @param object 对象
     * @param exception 想要抛出的异常
     *
     * @since 1.1.2
     */
    public static void throwIfNotNull(Object object, RuntimeException exception) {
        throwIf(ObjectUtil.isNotNull(object), exception);
    }
}
