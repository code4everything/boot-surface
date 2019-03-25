package org.code4everything.boot.exception;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.exception.template.EntityNotFoundException;
import org.code4everything.boot.exception.template.TokenBlankException;
import org.code4everything.boot.exception.template.UserNotLoggedInException;

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
     * 全局唯一实例
     *
     * @since 1.0.9
     */
    private static final TokenBlankException TOKEN_BLANK_EXCEPTION = new TokenBlankException();

    /**
     * 全局唯一实例
     *
     * @since 1.0.9
     */
    private static final UserNotLoggedInException USER_NOT_LOGGED_IN_EXCEPTION = new UserNotLoggedInException();


    /**
     * 已创建对象的存储器，有效期一天
     *
     * @since 1.0.9
     */
    private static Cache<String, EntityNotFoundException> cache = CacheBuilder.newBuilder().expireAfterWrite(24,
                                                                                                             TimeUnit.HOURS).build();


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
        EntityNotFoundException entityNotFoundException = cache.getIfPresent(entityName);
        if (Objects.isNull(entityNotFoundException)) {
            // 此处不考虑并发，最坏也就创建多个相同对象，多余的让垃圾回收器收集吧
            entityNotFoundException = new EntityNotFoundException(code, entityName);
            cache.put(entityName, entityNotFoundException);
        }
        return entityNotFoundException;
    }

    /**
     * 获取 {@link TokenBlankException}
     *
     * @return {@link TokenBlankException}
     *
     * @since 1.0.9
     */
    public static TokenBlankException tokenBlank() {
        return TOKEN_BLANK_EXCEPTION;
    }

    /**
     * 获取 {@link UserNotLoggedInException}
     *
     * @return {@link UserNotLoggedInException}
     *
     * @since 1.0.9
     */
    public static UserNotLoggedInException userNotLoggedIn() {
        return USER_NOT_LOGGED_IN_EXCEPTION;
    }
}
