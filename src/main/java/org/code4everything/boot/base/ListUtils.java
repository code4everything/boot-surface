package org.code4everything.boot.base;

import java.util.List;

/**
 * 列表工具类
 *
 * @author pantao
 * @since 2019/5/6
 */
public final class ListUtils {

    private ListUtils() {}

    /**
     * 交换值
     *
     * @param list 列表
     * @param idx1 索引
     * @param idx2 索引
     * @param <T> 值类型
     *
     * @since 1.1.2
     */
    public static <T> void swap(List<T> list, int idx1, int idx2) {
        T tmp = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, tmp);
    }
}
