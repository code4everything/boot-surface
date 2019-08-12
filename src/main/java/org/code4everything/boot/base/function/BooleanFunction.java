package org.code4everything.boot.base.function;

/**
 * 布尔运算函数
 *
 * @author pantao
 * @since 2018/12/26
 */
@FunctionalInterface
public interface BooleanFunction {

    /**
     * 运算布尔值
     *
     * @return 布尔值
     *
     * @since 1.0.5
     */
    boolean call();
}
