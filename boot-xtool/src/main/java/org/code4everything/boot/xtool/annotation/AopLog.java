package org.code4everything.boot.xtool.annotation;

import java.lang.annotation.*;

/**
 * @author pantao
 * @since 2018/9/10
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AopLog {

    /**
     * 方法的描述
     *
     * @return {@link String}
     */
    String value() default "";
}
