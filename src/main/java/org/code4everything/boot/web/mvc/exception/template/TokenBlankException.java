package org.code4everything.boot.web.mvc.exception.template;

import org.code4everything.boot.base.constant.MessageConsts;
import org.code4everything.boot.web.mvc.exception.HttpException;
import org.springframework.http.HttpStatus;

/**
 * Token 为空异常
 *
 * @author pantao
 * @since 2018-12-01
 */
public final class TokenBlankException extends HttpException {

    /**
     * 无参构造函数
     *
     * @since 1.0.4
     */
    public TokenBlankException() {
        super(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, MessageConsts.TOKEN_BLANK_ERROR_ZH);
    }
}
