package org.code4everything.boot.message;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.base.constant.StringConsts;
import org.code4everything.boot.config.BootConfig;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 验证码工具类
 *
 * @author pantao
 * @since 2019/3/19
 **/
public final class VerifyCodeUtils {

    private static final int DEFAULT_CODE_LENGTH = 6;

    /**
     * 验证码缓存
     *
     * @since 1.0.9
     */
    private static Cache<String, String> codeCache;

    /**
     * 发送频率检测
     *
     * @since 1.0.9
     */
    private static Cache<String, String> frequentlyCache;

    static {
        codeCache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build();
        frequentlyCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
    }

    private VerifyCodeUtils() {}

    /**
     * 检测是否频繁发送（一分钟）
     *
     * @param key 邮箱或手机号
     *
     * @return 是否频繁发送
     *
     * @since 1.0.9
     */
    public static boolean isFrequently(String key) {
        return frequentlyCache.asMap().containsKey(key);
    }

    /**
     * 从缓存中移除用户的验证码
     *
     * @param key 邮箱或手机号
     *
     * @since 1.0.9
     */
    public static void remove(String key) {
        codeCache.invalidate(key);
        frequentlyCache.invalidate(key);
    }

    /**
     * 校验验证码
     *
     * @param key 邮箱或手机号
     * @param code 验证码
     *
     * @return 验证码是否正确
     *
     * @since 1.0.9
     */
    public static boolean validate(String key, String code) {
        return StrUtil.isNotEmpty(code) && code.equals(codeCache.getIfPresent(key));
    }

    /**
     * 校验验证码
     *
     * @param key 邮箱或手机号
     * @param code 验证码
     * @param remove 验证通过后是否删除
     *
     * @return 验证码是否正确
     *
     * @since 1.0.9
     */
    public static boolean validate(String key, String code, boolean remove) {
        boolean result = validate(key, code);
        if (remove && result) {
            remove(key);
        }
        return result;
    }

    /**
     * 校验验证码，当验证码正确时从缓存中删除
     *
     * @param key 邮箱或手机号
     * @param code 验证码
     *
     * @return 验证码是否正确
     *
     * @since 1.0.9
     */
    public static boolean validateAndRemove(String key, String code) {
        return validate(key, code, true);
    }

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @param subject 主题
     * @param template 内容模板
     *
     * @return 验证码
     *
     * @since 1.0.9
     */
    public static String sendByMailAsync(String email, String subject, String template) {
        return sendByMailAsync(email, subject, template, null);
    }

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @param subject 主题
     * @param template 内容模板
     * @param callback 回调函数
     *
     * @return 验证码
     *
     * @since 1.0.9
     */
    public static String sendByMailAsync(String email, String subject, String template, MailCallback callback) {
        return sendByMailAsync(email, subject, template, DEFAULT_CODE_LENGTH, callback);
    }

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @param subject 主题
     * @param template 内容模板
     * @param codeLen 验证码长度
     * @param callback 回调函数
     *
     * @return 验证码
     *
     * @since 1.0.9
     */
    public static String sendByMailAsync(String email, String subject, String template, int codeLen,
                                         MailCallback callback) {
        // 生成验证码
        final String code = generateCode(email, codeLen);
        // 异步执行，并进行相应的回调
        ThreadUtil.execute(() -> {
            // 格式化内容
            String html = StrUtil.format(String.format(template, code), code);
            try {
                // 发送验证码
                MailUtils.send(email, subject, html);
                // 放入缓存
                put2cache(email, code);
                // 成功回调
                if (ObjectUtil.isNotNull(callback)) {
                    callback.handleSuccess(email, subject, html);
                }
            } catch (MessagingException e) {
                if (ObjectUtil.isNotNull(callback)) {
                    // 失败回调
                    callback.handleFailed(email, subject, html, e);
                }
            }
        });
        return code;
    }

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @param subject 主题
     * @param template 内容模板
     *
     * @return 验证码
     *
     * @throws MessagingException 异常
     * @since 1.0.9
     */
    public static String sendByMail(String email, String subject, String template) throws MessagingException {
        return sendByMail(email, subject, template, DEFAULT_CODE_LENGTH);
    }

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @param subject 主题
     * @param template 内容模板
     * @param codeLen 验证码长度
     *
     * @return 验证码
     *
     * @throws MessagingException 异常
     * @since 1.0.9
     */
    public static String sendByMail(String email, String subject, String template, int codeLen) throws MessagingException {
        final String code = generateCode(email, codeLen);
        MailUtils.send(email, subject, StrUtil.format(String.format(template, code), code));
        put2cache(email, code);
        return code;
    }

    /**
     * 发送验证码
     *
     * @param sender 发送器
     * @param address 通常为邮箱地址或手机号
     * @param template 内容模板
     *
     * @return 验证码
     *
     * @since 1.1.0
     */
    public static String sendBy(MessageSender sender, String address, String template) {
        return sendBy(sender, address, template, DEFAULT_CODE_LENGTH);
    }

    /**
     * 发送验证码
     *
     * @param sender 发送器
     * @param address 通常为邮箱地址或手机号
     * @param template 内容模板
     * @param codeLen 验证码长度
     *
     * @return 验证码
     *
     * @since 1.1.0
     */
    public static String sendBy(MessageSender sender, String address, String template, int codeLen) {
        final String code = generateCode(address, codeLen);
        boolean success = sender.sendMessage(address, StrUtil.format(String.format(template, code), code));
        if (success) {
            put2cache(address, code);
            return code;
        }
        return "";
    }

    /**
     * 生成验证码
     *
     * @param key 键
     * @param len 长度
     *
     * @return 验证码
     *
     * @since 1.1.0
     */
    private static String generateCode(String key, int len) {
        final String code = RandomUtil.randomNumbers(len);
        if (BootConfig.isDebug()) {
            String time = DateUtil.format(new Date(), StringConsts.DateFormat.DATE_TIME_MILLIS);
            String name = VerifyCodeUtils.class.getSimpleName();
            Console.log("{} {} - generate code '{}' for user '{}'", time, name, code, key);
        }
        return code;
    }

    /**
     * 缓存
     *
     * @param key 键
     * @param code 验证码
     *
     * @since 1.1.0
     */
    private static void put2cache(String key, String code) {
        codeCache.put(key, code);
        frequentlyCache.put(key, code);
    }
}
