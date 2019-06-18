package org.code4everything.boot.cache;

/**
 * 缓存列表移除接口
 *
 * @author pantao
 * @since 2019/6/18
 **/
@FunctionalInterface
public interface CacheRemovable<T> {

    /**
     * 是否从列表中移除该元素
     *
     * @param value 是否移除的值
     *
     * @return 是否移除
     *
     * @since 1.1.3
     */
    boolean shouldRemove(T value);
}
