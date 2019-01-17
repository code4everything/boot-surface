package org.code4everything.boot.base.collection;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 排序集合
 *
 * @author pantao
 * @since 2019/1/17
 **/
public class SortedList<E extends Comparable, T extends List<E>> {

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
        if (Objects.isNull(e)) {
            return;
        }
        int idx = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            int compare;
            if (comparator == null) {
                compare = e.compareTo(list.get(i));
            } else {
                compare = comparator.compare(list.get(i), e);
            }
            if (compare >= 0) {
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
    @SuppressWarnings("unchecked")
    public void setList(T list) {
        if (list == null) {
            throw new NullPointerException();
        }
        if (list.size() > 1) {
            // 与比较器相反
            list.sort(comparator == null ? Comparator.naturalOrder() : comparator.reversed());
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
}
