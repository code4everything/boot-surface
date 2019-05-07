package org.code4everything.boot.web.mvc.exception.template;

import org.code4everything.boot.base.constant.MessageConsts;
import org.code4everything.boot.web.mvc.exception.HttpException;
import org.springframework.http.HttpStatus;

/**
 * @author pantao
 * @since 2019/4/8
 **/
public final class RequestFrequentlyException extends HttpException {

    public RequestFrequentlyException() {
        super(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, MessageConsts.REQUEST_FREQUENTLY_ZH);
    }
}
