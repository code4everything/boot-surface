package org.code4everything.boot.bean;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 基类
 *
 * @author pantao
 * @since 2019/1/11
 **/
public interface BaseBean {

    /**
     * 转换成JSON字符串
     *
     * @return JSON字符串
     *
     * @since 1.1.1
     */
    default String toPrettyJsonString() {
        return JSONObject.toJSONString(this, true);
    }

    /**
     * 转换成JSON字符串
     *
     * @return JSON字符串
     *
     * @since 1.1.1
     */
    default String toJsonString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 检测是否有NULL字段，如果有则抛出异常
     *
     * @since 1.1.1
     */
    default void requireNonNullProperty() {
        Field[] fields = ReflectUtil.getFields(this.getClass());
        for (Field field : fields) {
            if (ObjectUtil.isNotNull(field) && Objects.isNull(ReflectUtil.getFieldValue(this, field))) {
                throw new NullPointerException("the value of field '" + field.getName() + "' at class '" + this.getClass().getName() + "' must not be null");
            }
        }
    }

    /**
     * 属性复制
     *
     * @param target 目标对象
     * @param <T> 对象类型
     *
     * @return 目标对象
     *
     * @since 1.0.6
     */
    default <T> T copyInto(T target) {
        Objects.requireNonNull(target, "param must not be null");
        BeanUtil.copyProperties(this, target);
        return target;
    }

    /**
     * 属性复制
     *
     * @param source 源对象
     *
     * @since 1.0.6
     */
    default void copyFrom(Object source) {
        if (ObjectUtil.isNotNull(source)) {
            BeanUtil.copyProperties(source, this);
        }
    }
}
