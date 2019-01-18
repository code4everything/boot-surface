package org.code4everything.boot.base.collection;

import cn.hutool.core.comparator.ComparatorException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 排序集合
 *
 * @author pantao
 * @since 2019/1/17
 **/
public class SortedList<E, T extends List<E>> {

    /**
     * 数据源
     *
     * @since 1.0.6
     */
    private T list;

    /**
     * 比较器
     *
     * @since 1.0.6
     */
    private Comparator<E> comparator;

    /**
     * 构造函数
     *
     * @param list 数据源
     *
     * @since 1.0.6
     */
    public SortedList(T list) {
        setList(list);
    }

    /**
     * 构造函数
     *
     * @param list 数据源
     * @param comparator 比较器
     *
     * @since 1.0.6
     */
    public SortedList(T list, Comparator<E> comparator) {
        setList(list, comparator);
    }

    /**
     * 添加元素，忽略空值
     *
     * @param e 数据
     *
     * @since 1.0.6
     */
    public void add(E e) {
        Objects.requireNonNull(e);
        throwComparatorException();
        int idx = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (comparator.compare(list.get(i), e) >= 0) {
                idx = i + 1;
                break;
            }
        }
        list.add(idx, e);
    }

    /**
     * 获取数据源
     *
     * @return {@link T}
     *
     * @since 1.0.6
     */
    public T getList() {
        return list;
    }

    /**
     * 设置数据源
     *
     * @param list 数据源
     *
     * @since 1.0.6
     */
    public void setList(T list) {
        if (list == null) {
            throw new NullPointerException();
        }
        if (list.size() > 1) {
            // 与比较器相反
            throwComparatorException();
            list.sort(comparator.reversed());
        }
        this.list = list;
    }

    /**
     * 设置数据源和比较器
     *
     * @param list 数据源
     * @param comparator 比较器
     *
     * @since 1.0.6
     */
    public void setList(T list, Comparator<E> comparator) {
        this.comparator = comparator;
        setList(list);
    }

    /**
     * 抛出比较器异常
     *
     * @since 1.0.6
     */
    private void throwComparatorException() {
        if (comparator == null) {
            throw new ComparatorException("must set a comparator");
        }
    }
}
