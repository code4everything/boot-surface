package org.code4everything.boot.base;

import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.exception.template.TokenBlankException;
import org.code4everything.boot.exception.template.UserUnloggedException;

import java.io.Serializable;
import java.util.Objects;

/**
 * 断言封装类
 *
 * @author pantao
 * @since 2018-12-01
 */
public class AssertUtils {

    private AssertUtils() {}

    /**
     * 断言Token不为空
     *
     * @param token Token
     *
     * @return Token
     *
     * @throws TokenBlankException Token 为空异常
     * @since 1.0.4
     */
    public static String assertTokenNotBlank(String token) throws TokenBlankException {
        if (StrUtil.isBlank(token)) {
            throw new TokenBlankException();
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
     * @throws UserUnloggedException 未登录异常
     * @since 1.0.4
     */
    public static <T extends Serializable> T assertUserLoggedIn(T user) throws UserUnloggedException {
        if (Objects.isNull(user)) {
            throw new UserUnloggedException();
        }
        return user;
    }
}
