package org.code4everything.boot.message;

import javax.mail.MessagingException;

/**
 * @author pantao
 * @since 2019/3/22
 **/
public interface MailCallback {

    /**
     * 异步发送邮件的回调
     *
     * @param email 邮箱
     * @param subject 发送的主题
     * @param html 发送内容
     *
     * @since 1.0.9
     */
    default void handleSuccess(String email, String subject, String html) {}

    /**
     * 异步发送邮件的回调
     *
     * @param email 邮箱
     * @param subject 发送的主题
     * @param html 发送内容
     * @param exception 发生的异常
     *
     * @since 1.0.9
     */
    default void handleFailed(String email, String subject, String html, MessagingException exception) {}
}
