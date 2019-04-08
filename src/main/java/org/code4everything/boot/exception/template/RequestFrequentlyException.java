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
        super(cn.hutool.http.HttpStatus.HTTP_BAD_REQUEST, HttpStatus.BAD_REQUEST, MessageConsts.REQUEST_FREQUENTLY_ZH);
    }
}
