package org.code4everything.boot.exception.template;

import org.code4everything.boot.exception.BootException;
import org.springframework.http.HttpStatus;

/**
 * @author pantao
 * @since 2019/3/20
 **/
public class EntityNotFoundException extends BootException {

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
        super.setCode(code);
        super.setMsg(msg);
        if (responseOk) {
            super.setStatus(HttpStatus.OK);
        } else {
            super.setStatus(HttpStatus.BAD_REQUEST);
        }
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
