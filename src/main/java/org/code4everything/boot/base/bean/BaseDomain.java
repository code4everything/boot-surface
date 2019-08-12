package org.code4everything.boot.base.bean;

import java.io.Serializable;

/**
 * 表实体基类
 *
 * @author pantao
 * @since 2019/5/10
 */
public interface BaseDomain extends BaseBean, Serializable {

    /**
     * 获取表（文档）主键
     *
     * @return 主键编号
     *
     * @since 1.1.2
     */
    Serializable primaryKey();
}
