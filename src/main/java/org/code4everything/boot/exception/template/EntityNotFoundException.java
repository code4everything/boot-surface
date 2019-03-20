package org.code4everything.boot.exception.template;

import org.code4everything.boot.exception.BootException;
import org.springframework.http.HttpStatus;

/**
 * @author pantao
 * @since 2019/3/20
 **/
public class EntityNotFoundException extends BootException {

    public EntityNotFoundException(int code, String msg, boolean responseOk) {
        super();
        init(code, msg, responseOk);
    }

    public EntityNotFoundException(int code, String entityName) {
        super();
        init(code, "entity '" + entityName + "' not found", true);
    }

    private void init(int code, String msg, boolean responseOk) {
        super.setCode(code);
        super.setMsg(msg);
        if (responseOk) {
            super.setStatus(HttpStatus.OK);
        } else {
            super.setStatus(HttpStatus.BAD_REQUEST);
        }
    }
}
