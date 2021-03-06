package org.code4everything.boot.log;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author pantao
 * @since 2018/10/30
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMethod {

    /**
     * 方法的描述
     *
     * @return {@link String}
     *
     * @since 1.0.0
     */
    String value() default "";
}
