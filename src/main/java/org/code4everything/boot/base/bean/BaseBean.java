package org.code4everything.boot.base.bean;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
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
     * 获取主键编号，如需用到时应重写此方法
     *
     * @return 主键编号
     *
     * @since 1.1.2
     */
    default Serializable getId() {return null;}

    /**
     * 对象内是否有必要的属性值，大多数情况下都需要重写此方法
     *
     * @return 是否有必要的属性值
     *
     * @since 1.1.2
     */
    default boolean hasValue() {
        return false;
    }

    /**
     * 转换成JSON字符串
     *
     * @param pretty 是否进行格式化输出
     *
     * @return JSON字符串
     *
     * @since 1.1.2
     */
    default String toJsonString(boolean pretty) {
        return JSONObject.toJSONString(this, pretty);
    }

    /**
     * 转换成JSON字符串
     *
     * @return JSON字符串
     *
     * @since 1.1.1
     */
    default String toJsonString() {
        return toJsonString(false);
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
