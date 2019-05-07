package org.code4everything.boot.message;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import java.util.Objects;

/**
 * 邮件发送
 *
 * @author pantao
 * @since 2019/3/18
 **/
public final class MailUtils {

    /**
     * 邮件发送器
     *
     * @since 1.0.9
     */
    private static JavaMailSender mailSender;

    /**
     * 发件箱
     *
     * @since 1.0.9
     */
    private static String outbox;

    private MailUtils() {}

    /**
     * 设置邮件发送器
     *
     * @param outbox 发件箱
     * @param mailSender {@link JavaMailSender}
     *
     * @since 1.0.9
     */
    public static void setMailSender(String outbox, JavaMailSender mailSender) {
        MailUtils.mailSender = mailSender;
        MailUtils.outbox = outbox;
    }

    /**
     * 发送文本邮件
     *
     * @param email 邮箱
     * @param subject 主题
     * @param html 邮件内容
     *
     * @since 1.0.9
     */
    public static void sendAsync(String email, String subject, String html) {
        sendAsync(email, subject, html, buildDefaultMessageHelper(), null);
    }


    /**
     * 发送文本邮件
     *
     * @param email 邮箱
     * @param subject 主题
     * @param html 邮件内容
     * @param callback 回调函数
     *
     * @since 1.0.9
     */
    public static void sendAsync(String email, String subject, String html, MailCallback callback) {
        sendAsync(email, subject, html, buildDefaultMessageHelper(), callback);
    }

    /**
     * 发送文本邮件
     *
     * @param email 邮箱
     * @param subject 主题
     * @param html 邮件内容
     * @param helper {@link MimeMessageHelper}
     * @param callback 回调函数
     *
     * @since 1.0.9
     */
    public static void sendAsync(String email, String subject, String html, MimeMessageHelper helper,
                                 MailCallback callback) {
        // 异步执行，并进行相应的回调
        ThreadUtil.execute(() -> {
            try {
                send(email, subject, html, helper);
                if (ObjectUtil.isNotNull(callback)) {
                    callback.handleSuccess(email, subject, html);
                }
            } catch (MessagingException e) {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.handleFailed(email, subject, html, e);
                }
            }
        });
    }

    /**
     * 发送文本邮件
     *
     * @param email 邮箱
     * @param subject 主题
     * @param html 邮件内容
     *
     * @throws MessagingException 异常
     * @since 1.0.9
     */
    public static void send(String email, String subject, String html) throws MessagingException {
        send(email, subject, html, buildDefaultMessageHelper());
    }

    /**
     * 发送文本邮件
     *
     * @param email 邮箱
     * @param subject 主题
     * @param html 邮件内容
     * @param helper {@link MimeMessageHelper}
     *
     * @throws MessagingException 异常
     * @since 1.0.9
     */
    public static void send(String email, String subject, String html, MimeMessageHelper helper) throws MessagingException {
        // 条件校验
        Objects.requireNonNull(mailSender, "please set a java mail sender");
        Objects.requireNonNull(outbox, "please set a outbox");
        Objects.requireNonNull(helper);
        // 设置邮件信息
        helper.setFrom(outbox);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(html, true);
        // 发送邮件
        mailSender.send(helper.getMimeMessage());
    }

    /**
     * 创建一个{@link MimeMessageHelper}
     *
     * @return {@link MimeMessageHelper}
     *
     * @since 1.0.9
     */
    public static MimeMessageHelper buildDefaultMessageHelper() {
        Objects.requireNonNull(mailSender, "please set a java mail sender");
        return new MimeMessageHelper(mailSender.createMimeMessage());
    }
}
