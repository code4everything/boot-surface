package org.code4everything.boot.exception.template;

import org.code4everything.boot.constant.MessageConsts;

/**
 * Token 为空异常
 *
 * @author pantao
 * @since 2018-12-01
 */
public class TokenBlankException extends UserNotLoggedInException {

    /**
     * 全局唯一实例
     *
     * @since 1.0.9
     */
    private static final TokenBlankException TOKEN_BLANK_EXCEPTION = new TokenBlankException();

    /**
     * 无参构造函数
     *
     * @since 1.0.4
     */
    private TokenBlankException() {
        super();
        super.setMsg(MessageConsts.TOKEN_BLANK_ERROR_ZH);
    }

    /**
     * 获取实例
     *
     * @return {@link TokenBlankException}
     *
     * @since 1.0.0
     */
    public static TokenBlankException getInstance() {
        return TOKEN_BLANK_EXCEPTION;
    }
}
