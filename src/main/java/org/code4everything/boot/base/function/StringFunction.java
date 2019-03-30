package org.code4everything.boot.base.function;

/**
 * 字符串运算函数
 *
 * @author pantao
 * @since 2018/12/29
 **/
@FunctionalInterface
public interface StringFunction {

    /**
     * 运算字符串
     *
     * @return 字符串
     *
     * @since 1.0.5
     */
    String call();
}
