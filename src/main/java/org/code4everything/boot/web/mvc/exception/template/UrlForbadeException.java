package org.code4everything.boot.web.mvc.exception.template;

import org.code4everything.boot.base.constant.MessageConsts;
import org.code4everything.boot.web.mvc.exception.HttpException;
import org.springframework.http.HttpStatus;

/**
 * @author pantao
 * @since 2019/5/9
 */
public final class UrlForbadeException extends HttpException {

    public UrlForbadeException() {
        super(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, MessageConsts.URL_FORBADE_ZH);
    }
}
