package org.code4everything.boot.web.mvc.exception;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.web.mvc.exception.template.*;
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
     * 默认异常加载器
     *
     * @since 1.1.2
     */
    private static final ExceptionLoader<HttpException> EXCEPTION_LOADER = HttpException::new;

    /**
     * 已创建对象的存储器，有效期一天
     *
     * @since 1.0.9
     */
    private static Cache<String, HttpException> cache = CacheBuilder.newBuilder()
            // 一天后缓存失效，并使用虚引用策略（当 JVM GC 时回收）
            .expireAfterWrite(24, TimeUnit.HOURS).weakKeys().weakValues().build();

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
        HttpException exception = cache.getIfPresent(entityName);
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
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    public static HttpException exception(int code, String msg) {
        return exception(code, msg, EXCEPTION_LOADER);
    }

    /**
     * 获取异常
     *
     * @param code 错误码
     * @param msg 消息
     * @param loader 异常构造类
     * @param <T> 异常
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    public static <T extends HttpException> T exception(int code, String msg, ExceptionLoader<T> loader) {
        return exception(code, HttpStatus.BAD_REQUEST, msg, loader);
    }

    /**
     * 获取异常
     *
     * @param code 错误码
     * @param msg 消息
     * @param status 响应状态
     * @param loader 异常构造类
     * @param <T> 异常
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    @SuppressWarnings("unchecked")
    public static <T extends HttpException> T exception(int code, HttpStatus status, String msg,
                                                        ExceptionLoader<T> loader) {
        String key = code + " " + status.toString() + " " + msg;
        if (cache.asMap().containsKey(key)) {
            return (T) cache.getIfPresent(key);
        }
        T exception = loader.load(code, status, msg);
        cache.put(key, exception);
        return exception;
    }

    /**
     * 获取异常
     *
     * @param status 响应状态
     *
     * @return 异常
     *
     * @since 1.1.2
     */
    public static HttpException exception(HttpStatus status) {
        return exception(status, status.getReasonPhrase(), EXCEPTION_LOADER);
    }

    /**
     * 获取异常
     *
     * @param msg 消息
     * @param status 响应状态
     * @param loader 异常构造类
     * @param <T> 异常
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    public static <T extends HttpException> T exception(HttpStatus status, String msg, ExceptionLoader<T> loader) {
        return exception(status.value(), status, msg, loader);
    }

    /**
     * 获取异常
     *
     * @param msg 消息
     * @param status 响应状态
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    public static HttpException exception(HttpStatus status, String msg) {
        return exception(status, msg, EXCEPTION_LOADER);
    }

    /**
     * 获取异常
     *
     * @param code 错误码
     * @param msg 消息
     * @param status 响应状态
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    public static HttpException exception(int code, HttpStatus status, String msg) {
        return exception(code, status, msg, EXCEPTION_LOADER);
    }

    /**
     * 获取异常，通常使用一个实现 {@link ExceptionBiscuit} 接口的枚举类来定义系统异常情况
     *
     * @param biscuit {@link ExceptionBiscuit}
     * @param params 消息格式化参数
     *
     * @return 异常
     *
     * @since 1.1.2
     */
    public static HttpException exception(ExceptionBiscuit biscuit, Object... params) {
        return exception(biscuit, EXCEPTION_LOADER, params);
    }

    /**
     * 获取异常，通常使用一个实现 {@link ExceptionBiscuit} 接口的枚举类来定义系统异常情况
     *
     * @param biscuit {@link ExceptionBiscuit}
     * @param loader 异常构造类
     * @param params 消息格式化参数
     * @param <T> 异常类型
     *
     * @return 异常
     *
     * @since 1.1.2
     */
    public static <T extends HttpException> T exception(ExceptionBiscuit biscuit, ExceptionLoader<T> loader,
                                                        Object... params) {
        String msg = ArrayUtil.isEmpty(params) ? biscuit.getMsg() : biscuit.getMsgs(params);
        return exception(biscuit.getCode(), biscuit.getStatus(), msg, loader);
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
     * 获取异常
     *
     * @param clazz 类
     * @param <T> 异常
     *
     * @return 异常
     *
     * @since 1.1.0
     */
    public static <T extends HttpException> T exception(Class<T> clazz) {
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
    public static <T extends HttpException> T exception(String key, Class<T> clazz) {
        if (cache.asMap().containsKey(key)) {
            return (T) cache.getIfPresent(key);
        }
        try {
            T exception = clazz.newInstance();
            cache.put(key, exception);
            return exception;
        } catch (Exception e) {
            throw new HttpException("new class " + clazz.getName() + " error, must set a default constructor");
        }
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

    /**
     * 获取 {@link UrlForbadeException}
     *
     * @return {@link UrlForbadeException}
     *
     * @since 1.1.2
     */
    public static UrlForbadeException urlForbade() {
        return exception(UrlForbadeException.class);
    }
}
