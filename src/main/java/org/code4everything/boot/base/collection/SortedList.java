package org.code4everything.boot.base.collection;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.ComparatorException;
import cn.hutool.core.util.ObjectUtil;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

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
     * 无参构造函数
     *
     * @since 1.0.6
     */
    public SortedList() {}

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
     * 构造排序列表
     *
     * @param list 数据源
     * @param comparator 比较器
     * @param <E> 数据类型
     * @param <T> 数据源类型
     *
     * @return 排序列表
     *
     * @since 1.0.6
     */
    public static <E, T extends List<E>> SortedList<E, T> of(T list, Comparator<E> comparator) {
        return new SortedList<>(list, comparator);
    }

    /**
     * 构造线程安全的排序列表
     *
     * @param list 数据源
     * @param comparator 比较器
     * @param <E> 数据类型
     * @param <T> 数据源类型
     *
     * @return 线程安全的排序列表
     *
     * @since 1.0.6
     */
    public static <E, T extends List<E>> ConcurrentSortedList<E, T> ofConcurrent(T list, Comparator<E> comparator) {
        return new ConcurrentSortedList<>(list, comparator);
    }

    /**
     * 转换成排序列表
     *
     * @param queue {@link Queue}
     * @param list 列表
     * @param comparator 比较器，如果是 {@link PriorityQueue} 或 {@link PriorityBlockingQueue} 可为空值
     * @param <E> 数据类型
     * @param <T> 列表类型
     *
     * @return 列表
     *
     * @since 1.0.6
     */
    public static <E, T extends List<E>> T sortTo(Queue<E> queue, T list, Comparator<E> comparator) {
        if (queue instanceof PriorityQueue || queue instanceof PriorityBlockingQueue) {
            while (CollUtil.isNotEmpty(list)) {
                E e = list.remove(0);
                queue.offer(e);
            }
            int len = queue.size();
            for (int i = 0; i < len; i++) {
                list.add(queue.poll());
            }
        } else {
            SortedList<E, T> sortedList = SortedList.of(list, comparator);
            sortedList.addAll(queue);
        }
        return list;
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
     * 删除指定索引的数据
     *
     * @param o 数据
     *
     * @return 是否删除成功
     *
     * @since 1.0.6
     */
    public boolean remove(Object o) {
        return list.remove(o);
    }

    /**
     * 删除集合中存在的所有数据
     *
     * @param c 集合
     *
     * @return 是否删除成功
     *
     * @since 1.0.6
     */
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    /**
     * 删除集合中不存在的所有数据
     *
     * @param c 集合
     *
     * @return 是否删除成功
     *
     * @since 1.0.6
     */
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    /**
     * 清空所有数据
     *
     * @since 1.0.6
     */
    public void clear() {
        list.clear();
    }

    /**
     * 删除指定索引位置的数据
     *
     * @param index 索引
     *
     * @return 数据
     *
     * @since 1.0.6
     */
    public E remove(int index) {
        return list.remove(index);
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
            int start = 0;
            int end = list.size() - 1;
            while (start <= end) {
                int mid = start + ((end - start) >> 1);
                if (comparator.compare(e, list.get(mid)) >= 0) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
            list.add(start, e);
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
            throwComparatorExceptionIfNull();
            list.sort(comparator);
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
