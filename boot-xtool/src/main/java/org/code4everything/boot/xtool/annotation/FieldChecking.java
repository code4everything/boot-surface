package org.code4everything.boot.xtool.annotation;

import org.code4everything.boot.xtool.constant.DefaultValueConsts;

import java.lang.annotation.*;

/**
 * @author pantao
 * @since 2018/7/17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface FieldChecking {

    /**
     * 错误码
     *
     * @return {@link Integer}
     */
    int code() default 400;

    /**
     * 提示消息
     *
     * @return {@link String}
     */
    String message() default "{} is required, or format failed";

    /**
     * 验证状态
     *
     * @return {@link String}
     */
    String status() default DefaultValueConsts.ERROR_EN;

    /**
     * 正则表达式
     *
     * @return {@link String}
     */
    String expression() default "";

    /**
     * 默认值（仅针对字段值为NULL时）
     *
     * @return {@link String}
     */
    String defaultValue() default "";

    /**
     * 是否递归验证
     *
     * @return {@link Boolean}
     */
    boolean recursive() default false;
}
