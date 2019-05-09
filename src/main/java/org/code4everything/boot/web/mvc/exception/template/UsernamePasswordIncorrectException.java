package org.code4everything.boot.web.mvc.exception.template;

import org.code4everything.boot.base.constant.MessageConsts;
import org.code4everything.boot.web.mvc.exception.HttpException;

/**
 * @author pantao
 * @since 2019/5/9
 **/
public final class UsernamePasswordIncorrectException extends HttpException {

    public UsernamePasswordIncorrectException(int errCode) {
        super(errCode, MessageConsts.USERNAME_PASSWORD_INCORRECT_ZH, true);
    }
}
