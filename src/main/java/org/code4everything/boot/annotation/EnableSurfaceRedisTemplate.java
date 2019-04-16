package org.code4everything.boot.annotation;

import org.code4everything.boot.config.BootRedisConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author pantao
 * @since 2019/4/16
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({BootRedisConfiguration.class})
public @interface EnableSurfaceRedisTemplate {}
