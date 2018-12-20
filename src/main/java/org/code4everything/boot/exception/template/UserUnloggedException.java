package org.code4everything.boot.exception.template;

import cn.hutool.http.HttpStatus;
import org.code4everything.boot.constant.MessageConsts;
import org.code4everything.boot.exception.BootException;

/**
 * 用户未登录异常
 *
 * @author pantao
 * @since 2018-12-01
 */
public class UserUnloggedException extends BootException {

    /**
     * 错误消息
     *
     * @since 1.0.4
     */
    private static final String MSG = MessageConsts.USER_UNLOGGED_ERROR_ZH;

    /**
     * 无参构造函数
     *
     * @since 1.0.4
     */
    public UserUnloggedException() {
        super(HttpStatus.HTTP_UNAUTHORIZED, org.springframework.http.HttpStatus.UNAUTHORIZED, MSG);
    }
}
