package org.code4everything.boot.base;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.mail.MessagingException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码工具类
 *
 * @author pantao
 * @since 2019/3/19
 **/
public class VerifyCodeUtils {


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
     * 移除用户的验证码
     *
     * @param key 邮箱或手机号
     *
     * @since 1.0.9
     */
    public static void removeVerifyCode(String key) {
        codeCache.asMap().remove(key);
        frequentlyCache.asMap().remove(key);
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
    public static boolean validateVerifyCode(String key, String code) {
        return StrUtil.isNotEmpty(code) && code.equals(codeCache.getIfPresent(key));
    }

    /**
     * 校验验证码并删除
     *
     * @param key 邮箱或手机号
     * @param code 验证码
     *
     * @return 验证码是否正确
     *
     * @since 1.0.9
     */
    public static boolean validateVerifyCodeAndRemove(String key, String code) {
        boolean result = validateVerifyCode(key, code);
        removeVerifyCode(key);
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
        EmailUtils.sendTextEmail(email, subject, StrUtil.format(String.format(textTemplate, code), code));
        codeCache.put(email, code);
        frequentlyCache.put(email, code);
        return code;
    }
}
