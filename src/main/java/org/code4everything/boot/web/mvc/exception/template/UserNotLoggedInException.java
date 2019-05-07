package org.code4everything.boot.web.mvc.exception.template;

import org.code4everything.boot.base.constant.MessageConsts;
import org.code4everything.boot.web.mvc.exception.HttpException;
import org.springframework.http.HttpStatus;

/**
 * 用户未登录异常
 *
 * @author pantao
 * @since 2018-12-01
 */
public final class UserNotLoggedInException extends HttpException {

    /**
     * 无参构造函数
     *
     * @since 1.0.4
     */
    public UserNotLoggedInException() {
        super(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, MessageConsts.USER_NOT_LOGGED_IN_ERROR_ZH);
    }
}
