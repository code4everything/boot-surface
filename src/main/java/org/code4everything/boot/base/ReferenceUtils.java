package org.code4everything.boot.base;

import java.lang.ref.Reference;
import java.util.Objects;

/**
 * 引用工具类
 *
 * @author pantao
 * @since 2019/6/26
 **/
public class ReferenceUtils {

    private ReferenceUtils() {}

    /**
     * 获取引用值
     *
     * @param reference 值的引用
     * @param <T> 值类型
     *
     * @return 值
     *
     * @since 1.1.5
     */
    public static <T> T safeUnwrap(Reference<T> reference) {
        return Objects.isNull(reference) ? null : reference.get();
    }
}
