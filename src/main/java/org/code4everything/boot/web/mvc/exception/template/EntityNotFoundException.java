package org.code4everything.boot.web.mvc.exception.template;

import org.code4everything.boot.web.mvc.exception.HttpException;
import org.springframework.http.HttpStatus;

/**
 * @author pantao
 * @since 2019/3/20
 **/
public final class EntityNotFoundException extends HttpException {

    public EntityNotFoundException() {}

    /**
     * 普通构造函数
     *
     * @param code 错误码
     * @param msg 消息
     * @param responseOk 是否响应200OK
     *
     * @since 1.0.9
     */
    public EntityNotFoundException(int code, String msg, boolean responseOk) {
        super(code, responseOk ? HttpStatus.OK : HttpStatus.BAD_REQUEST, msg);
    }

    /**
     * 构造函数
     *
     * @param code 错误码
     * @param entityName 实体名称
     *
     * @since 1.0.9
     */
    public EntityNotFoundException(int code, String entityName) {
        this(code, "entity '" + entityName + "' not found", true);
    }
}
