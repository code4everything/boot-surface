package org.code4everything.boot.base;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 邮件发送
 *
 * @author pantao
 * @since 2019/3/18
 **/
public class EmailUtils {

    static final Cache<String, String> CACHE = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build();

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
     * 移除用户的验证码
     *
     * @param email 邮箱
     *
     * @since 1.0.9
     */
    public static void removeVerifyCode(String email) {
        CACHE.asMap().remove(email);
    }

    /**
     * 校验验证码
     *
     * @param email 邮箱
     * @param code 验证码
     *
     * @return 验证码是否正确
     *
     * @since 1.0.9
     */
    public static boolean verifyCode(String email, String code) {
        return StrUtil.isNotEmpty(code) && code.equals(CACHE.getIfPresent(email));
    }

    /**
     * 校验验证码并删除
     *
     * @param email 邮箱
     * @param code 验证码
     *
     * @return 验证码是否正确
     *
     * @since 1.0.9
     */
    public static boolean verifyCodeAndRemove(String email, String code) {
        boolean result = verifyCode(email, code);
        removeVerifyCode(email);
        return result;
    }

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @param subject 主题
     * @param textTemplate 内容模板
     *
     * @return 验证码
     *
     * @throws MessagingException 异常
     * @since 1.0.9
     */
    public static String sendVerifyCodeByEmail(String email, String subject, String textTemplate) throws MessagingException {
        return sendVerifyCodeByEmail(email, subject, textTemplate, 6);
    }

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @param subject 主题
     * @param textTemplate 内容模板
     * @param codeLen 验证码长度
     *
     * @return 验证码
     *
     * @throws MessagingException 异常
     * @since 1.0.9
     */
    public static String sendVerifyCodeByEmail(String email, String subject, String textTemplate, int codeLen) throws MessagingException {
        String code = RandomUtil.randomNumbers(codeLen);
        sendTextEmail(email, subject, StrUtil.format(String.format(textTemplate, code), code));
        CACHE.put(email, code);
        return code;
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
