package org.code4everything.boot.web.mvc;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.base.function.BooleanFunction;
import org.code4everything.boot.web.mvc.exception.ExceptionBiscuit;
import org.code4everything.boot.web.mvc.exception.ExceptionFactory;
import org.code4everything.boot.web.mvc.exception.HttpException;

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
        if (StrUtil.isBlank(token)) {
            throw ExceptionFactory.tokenBlank();
        }
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
        if (Objects.isNull(user)) {
            throw ExceptionFactory.userNotLoggedIn();
        }
        return user;
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
    public static void throwIf(Optional<?> optional, HttpException exception) {
        throwIf(!optional.isPresent(), exception);
    }

    /**
     * 抛出异常
     *
     * @param shouldThrow 是否抛出异常
     * @param exception 想要抛出的异常
     *
     * @since 1.1.2
     */
    public static void throwIf(boolean shouldThrow, HttpException exception) {
        if (shouldThrow) {
            throw exception;
        }
    }

    /**
     * 从异常缓存中获取并抛出
     *
     * @param optional {@link Optional}
     * @param biscuit {@link ExceptionBiscuit}
     * @param params 消息格式化参数
     *
     * @since 1.1.2
     */
    public static void throwIf(Optional<?> optional, ExceptionBiscuit biscuit, Object... params) {
        throwIf(!optional.isPresent(), biscuit, params);
    }

    /**
     * 从异常缓存中获取并抛出
     *
     * @param shouldThrow 是否抛出异常
     * @param biscuit {@link ExceptionBiscuit}
     * @param params 消息格式化参数
     *
     * @since 1.1.2
     */
    public static void throwIf(boolean shouldThrow, ExceptionBiscuit biscuit, Object... params) {
        if (shouldThrow) {
            throw ExceptionFactory.exception(biscuit, params);
        }
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
     * 抛出异常
     *
     * @param function 布尔函数
     * @param exception 想要抛出的异常
     *
     * @since 1.1.2
     */
    public static void throwIf(BooleanFunction function, HttpException exception) {
        throwIf(function.call(), exception);
    }

    /**
     * 从异常缓存中获取并抛出
     *
     * @param function 布尔函数
     * @param biscuit {@link ExceptionBiscuit}
     * @param params 消息格式化参数
     *
     * @since 1.1.2
     */
    public static void throwIf(BooleanFunction function, ExceptionBiscuit biscuit, Object... params) {
        throwIf(function.call(), biscuit, params);
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
     * 如果对象空指针抛出异常
     *
     * @param object 对象
     * @param exception 想要抛出的异常
     *
     * @since 1.1.2
     */
    public static void throwIfNull(Object object, HttpException exception) {
        throwIf(Objects.isNull(object), exception);
    }

    /**
     * 从异常缓存中获取并抛出
     *
     * @param object 对象
     * @param biscuit {@link ExceptionBiscuit}
     * @param params 消息格式化参数
     *
     * @since 1.1.2
     */
    public static void throwIfNull(Object object, ExceptionBiscuit biscuit, Object... params) {
        throwIf(Objects.isNull(object), biscuit, params);
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

    /**
     * 如果对象不为空抛出异常
     *
     * @param object 对象
     * @param exception 想要抛出的异常
     *
     * @since 1.1.2
     */
    public static void throwIfNotNull(Object object, HttpException exception) {
        throwIf(ObjectUtil.isNotNull(object), exception);
    }

    /**
     * 从异常缓存中获取并抛出
     *
     * @param object 对象
     * @param biscuit {@link ExceptionBiscuit}
     * @param params 消息格式化参数
     *
     * @since 1.1.2
     */
    public static void throwIfNotNull(Object object, ExceptionBiscuit biscuit, Object... params) {
        throwIf(ObjectUtil.isNotNull(object), biscuit, params);
    }
}
