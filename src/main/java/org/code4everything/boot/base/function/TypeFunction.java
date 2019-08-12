package org.code4everything.boot.base.function;

/**
 * 类型运算函数
 *
 * @author pantao
 * @since 2018/12/29
 */
@FunctionalInterface
public interface TypeFunction<T> {

    /**
     * 运算类型
     *
     * @return 类型
     *
     * @since 1.0.5
     */
    T call();
}
