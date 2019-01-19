package org.code4everything.boot.base.collection;

import cn.hutool.core.comparator.ComparatorException;
import cn.hutool.core.util.ObjectUtil;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 排序集合
 *
 * @author pantao
 * @since 2019/1/17
 **/
@NotThreadSafe
public class SortedList<E, T extends List<E>> {

    /**
     * 数据源
     *
     * @since 1.0.6
     */
    protected T list;

    /**
     * 比较器
     *
     * @since 1.0.6
     */
    protected Comparator<E> comparator;

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
     * 添加所有数据，忽略空值
     *
     * @param iterable {@link Iterable}
     *
     * @since 1.0.6
     */
    public void addAll(Iterable<E> iterable) {
        Iterator<E> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            addIgnoreNull(iterator.next());
        }
    }

    /**
     * 添加元素
     *
     * @param e 数据
     *
     * @since 1.0.6
     */
    public void add(E e) {
        Objects.requireNonNull(e);
        addIgnoreNull(e);
    }

    /**
     * 添加元素，忽略空值
     *
     * @param e 数据
     *
     * @since 1.0.6
     */
    public void addIgnoreNull(E e) {
        if (ObjectUtil.isNotNull(e)) {
            throwComparatorExceptionIfNull();
            int idx = 0;
            for (int i = list.size() - 1; i >= 0; i--) {
                if (comparator.compare(list.get(i), e) >= 0) {
                    idx = i + 1;
                    break;
                }
            }
            list.add(idx, e);
        }
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
        if (Objects.isNull(list)) {
            throw new NullPointerException();
        }
        if (list.size() > 1) {
            // 与比较器相反
            throwComparatorExceptionIfNull();
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
    private void throwComparatorExceptionIfNull() {
        if (Objects.isNull(comparator)) {
            throw new ComparatorException("must set a comparator");
        }
    }
}
