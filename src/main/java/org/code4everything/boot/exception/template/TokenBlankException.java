package org.code4everything.boot.exception.template;

/**
 * Token 为空异常
 *
 * @author pantao
 * @since 2018-12-01
 */
public class TokenBlankException extends UserUnloggedException {

    /**
     * 错误消息
     *
     * @since 1.0.4
     */
    private static final String MSG = "Token不能为空";

    /**
     * 无参构造函数
     *
     * @since 1.0.4
     */
    public TokenBlankException() {
        super();
        super.setMsg(MSG);
    }
}
