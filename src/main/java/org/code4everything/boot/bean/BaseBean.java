package org.code4everything.boot.bean;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 基类
 *
 * @author pantao
 * @since 2019/1/11
 **/
public interface BaseBean extends Serializable {

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
        BeanUtils.copyProperties(this, target);
        return target;
    }

    /**
     * 属性复制
     *
     * @param source 源对象
     *
     * @return {@link BaseBean}
     *
     * @since 1.0.6
     */
    default BaseBean copyFrom(Object source) {
        BeanUtils.copyProperties(source, this);
        return this;
    }
}
