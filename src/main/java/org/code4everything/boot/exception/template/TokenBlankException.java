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
     * 无参构造函数
     *
     * @since 1.0.4
     */
    public TokenBlankException() {
        super();
        super.setMsg(MessageConsts.TOKEN_BLANK_ERROR_ZH);
    }
}
