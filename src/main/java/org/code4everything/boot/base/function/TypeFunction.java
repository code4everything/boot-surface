package org.code4everything.boot.base.function;

/**
 * 类型运算函数
 *
 * @author pantao
 * @since 2018/12/29
 **/
@FunctionalInterface
public interface TypeFunction {

    /**
     * 运算类型
     *
     * @param <T> 数据类型
     *
     * @return 类型
     *
     * @since 1.0.5
     */
    <T> T call();
}
