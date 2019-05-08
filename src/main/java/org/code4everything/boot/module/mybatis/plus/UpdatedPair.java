package org.code4everything.boot.module.mybatis.plus;

import org.code4everything.boot.base.bean.BaseBean;

/**
 * 更新后的新旧值对
 *
 * @author pantao
 * @since 2019/5/8
 **/
public class UpdatedPair<T> implements BaseBean {

    private T oldValue;

    private T newValue;

    UpdatedPair(T oldValue, T newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * 获取旧值
     *
     * @return 旧值
     *
     * @since 1.1.2
     */
    public T getOldValue() {
        return oldValue;
    }

    /**
     * 获取新值
     *
     * @return 新值
     *
     * @since 1.1.2
     */
    public T getNewValue() {
        return newValue;
    }
}
