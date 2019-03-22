package org.code4everything.boot.message;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import org.code4everything.boot.interfaces.EmailCallable;
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
public class EmailUtils {

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

    private EmailUtils() {}

    /**
     * 设置邮件发送器
     *
     * @param outbox 发件箱
     * @param mailSender {@link JavaMailSender}
     *
     * @since 1.0.9
     */
    public static void setMailSender(String outbox, JavaMailSender mailSender) {
        EmailUtils.mailSender = mailSender;
        EmailUtils.outbox = outbox;
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
    public static void sendEmailAsync(String email, String subject, String html) {
        sendEmailAsync(email, subject, html, buildDefaultMessageHelper(), null);
    }


    /**
     * 发送文本邮件
     *
     * @param email 邮箱
     * @param subject 主题
     * @param html 邮件内容
     * @param callable 回调函数
     *
     * @since 1.0.9
     */
    public static void sendEmailAsync(String email, String subject, String html, EmailCallable callable) {
        sendEmailAsync(email, subject, html, buildDefaultMessageHelper(), callable);
    }

    /**
     * 发送文本邮件
     *
     * @param email 邮箱
     * @param subject 主题
     * @param html 邮件内容
     * @param helper {@link MimeMessageHelper}
     * @param callable 回调函数
     *
     * @since 1.0.9
     */
    public static void sendEmailAsync(String email, String subject, String html, MimeMessageHelper helper,
                                      EmailCallable callable) {
        ThreadUtil.execute(() -> {
            try {
                sendEmail(email, subject, html, helper);
                if (ObjectUtil.isNotNull(callable)) {
                    callable.handleSuccess(email, subject, html);
                }
            } catch (MessagingException e) {
                if (ObjectUtil.isNotNull(callable)) {
                    callable.handleFailed(email, subject, html, e);
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
    public static void sendEmail(String email, String subject, String html) throws MessagingException {
        sendEmail(email, subject, html, buildDefaultMessageHelper());
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
    public static void sendEmail(String email, String subject, String html, MimeMessageHelper helper) throws MessagingException {
        Objects.requireNonNull(mailSender, "please set a java mail sender");
        Objects.requireNonNull(outbox, "please set a outbox");
        Objects.requireNonNull(helper);
        helper.setFrom(outbox);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(helper.getMimeMessage());
    }

    public static MimeMessageHelper buildDefaultMessageHelper() {
        return new MimeMessageHelper(mailSender.createMimeMessage());
    }
}
