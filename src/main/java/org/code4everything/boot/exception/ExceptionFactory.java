package org.code4everything.boot.exception;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.exception.template.EntityNotFoundException;
import org.code4everything.boot.exception.template.RequestFrequentlyException;
import org.code4everything.boot.exception.template.TokenBlankException;
import org.code4everything.boot.exception.template.UserNotLoggedInException;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 异常工厂，异常类复用，减少对象的创建
 *
 * @author pantao
 * @since 2019/3/25
 **/
public class ExceptionFactory {

    /**
     * 已创建对象的存储器，有效期一天
     *
     * @since 1.0.9
     */
    private static Cache<String, BootException> cache = CacheBuilder.newBuilder()
            // 一天后缓存失效
            .expireAfterWrite(24, TimeUnit.HOURS).build();

    private ExceptionFactory() {}

    /**
     * 获取 {@link EntityNotFoundException}
     *
     * @param code 错误码
     * @param entityName 实体名称
     *
     * @return {@link EntityNotFoundException}
     *
     * @since 1.0.9
     */
    public static EntityNotFoundException entityNotFound(int code, String entityName) {
        Objects.requireNonNull(entityName, "entity name must not be null");
        BootException exception = cache.getIfPresent(entityName);
        if (Objects.isNull(exception)) {
            // 此处不考虑并发，最坏也就创建多个相同对象，多余的让垃圾回收器收集吧
            exception = new EntityNotFoundException(code, entityName);
            cache.put(entityName, exception);
        }
        return (EntityNotFoundException) exception;
    }

    /**
     * 获取异常
     *
     * @param code 错误码
     * @param msg 消息
     * @param clazz 类
     * @param <T> 异常
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    public static <T extends BootException> T exception(int code, String msg, Class<T> clazz) {
        return exception(code, msg, HttpStatus.BAD_REQUEST, clazz);
    }

    /**
     * 获取异常
     *
     * @param msg 消息
     * @param status 响应状态
     * @param clazz 类
     * @param <T> 异常
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    public static <T extends BootException> T exception(String msg, HttpStatus status, Class<T> clazz) {
        return exception(status.value(), msg, status, clazz);
    }

    /**
     * 获取异常
     *
     * @param code 错误码
     * @param msg 消息
     * @param status 响应状态
     * @param clazz 类
     * @param <T> 异常
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    public static <T extends BootException> T exception(int code, String msg, HttpStatus status, Class<T> clazz) {
        String key = msg + status.toString() + code;
        if (!cache.asMap().containsKey(key)) {
            T exception = exception(key, clazz);
            exception.setMsg(msg);
            exception.setCode(code);
            exception.setStatus(status);
            return exception;
        }
        return exception(key, clazz);
    }

    /**
     * 获取异常
     *
     * @param clazz 类
     * @param <T> 异常
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    public static <T extends BootException> T exception(Class<T> clazz) {
        return exception(clazz.getSimpleName(), clazz);
    }

    /**
     * 获取异常
     *
     * @param key 键
     * @param clazz 类
     * @param <T> 异常
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    @SuppressWarnings("unchecked")
    public static <T extends BootException> T exception(String key, Class<T> clazz) {
        BootException exception = cache.getIfPresent(key);
        if (Objects.isNull(exception)) {
            try {
                exception = clazz.newInstance();
                cache.put(key, exception);
            } catch (Exception e) {
                throw new BootException("new class " + clazz.getName() + " error, must set a default constructor");
            }
        }
        return (T) exception;
    }

    /**
     * 获取 {@link TokenBlankException}
     *
     * @return {@link TokenBlankException}
     *
     * @since 1.0.9
     */
    public static TokenBlankException tokenBlank() {
        return exception(TokenBlankException.class);
    }

    /**
     * 获取 {@link UserNotLoggedInException}
     *
     * @return {@link UserNotLoggedInException}
     *
     * @since 1.0.9
     */
    public static UserNotLoggedInException userNotLoggedIn() {
        return exception(UserNotLoggedInException.class);
    }

    /**
     * 获取 {@link RequestFrequentlyException}
     *
     * @return {@link RequestFrequentlyException}
     *
     * @since 1.1.0
     */
    public static RequestFrequentlyException requestFrequently() {
        return exception(RequestFrequentlyException.class);
    }
}
