package org.code4everything.boot.annotation;

import com.alibaba.fastjson.JSONObject;
import org.code4everything.boot.encoder.DefaultFieldEncoder;
import org.code4everything.boot.encoder.FieldEncoder;

import java.lang.annotation.*;
import java.lang.reflect.Field;

/**
 * 加密字段
 *
 * @author pantao
 * @since 2018/11/11
 **/
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sealed {

    /**
     * 加密方法，默认仅支持：md5 sha1 sha256，不支持的加密方法将用方法名覆盖字段值。 其他加密方法需要继承 {@link DefaultFieldEncoder} 后重写 {@link
     * DefaultFieldEncoder#encodeField(Field, Object, Sealed)} 方法或你自己实现 {@link FieldEncoder}接口
     *
     * @return 加密方法
     *
     * @since 1.0.0
     */
    String value() default "******";

    /**
     * 私钥密码
     *
     * @return 私钥密码
     *
     * @since 1.0.0
     */
    String privateKey() default "";

    /**
     * 公钥密码
     *
     * @return 公钥密码
     *
     * @since 1.0.0
     */
    String publicKey() default "";

    /**
     * 自定义参数，{@link JSONObject} 格式
     *
     * @return {@link JSONObject} 格式参数
     *
     * @since 1.0.0
     */
    String params() default "{}";
}
