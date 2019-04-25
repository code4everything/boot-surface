package org.code4everything.boot.exception.template;

import org.code4everything.boot.constant.MessageConsts;
import org.code4everything.boot.exception.BootException;
import org.springframework.http.HttpStatus;

/**
 * @author pantao
 * @since 2019/4/8
 **/
public class RequestFrequentlyException extends BootException {

    public RequestFrequentlyException() {
        super(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, MessageConsts.REQUEST_FREQUENTLY_ZH);
    }
}
