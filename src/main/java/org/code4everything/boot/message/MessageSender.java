package org.code4everything.boot.message;

/**
 * @author pantao
 * @since 2019/4/11
 **/
@FunctionalInterface
public interface MessageSender {

    /**
     * 发送消息
     *
     * @param address 通常为邮箱地址或手机号
     * @param message 消息
     *
     * @return 是否发送成功
     *
     * @since 1.1.0
     */
    boolean sendMessage(String address, String message);
}
