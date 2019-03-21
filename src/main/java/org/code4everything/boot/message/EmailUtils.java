package org.code4everything.boot.message;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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
     * @param text 文本内容
     *
     * @throws MessagingException 异常
     * @since 1.0.9
     */
    public static void sendTextEmail(String email, String subject, String text) throws MessagingException {
        Objects.requireNonNull(mailSender, "please set a java mail sender");
        Objects.requireNonNull(outbox, "please set a outbox");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(outbox);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(text);
        mailSender.send(mimeMessage);
    }
}
