package org.code4everything.boot.base.collection;

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
    private Comparator<E> comparator;

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
            int size = list.size();
            // 从最后一个元素开始将List中数据清空，并插入到有序的队列中
            while (size > 0) {
                queue.offer(list.remove(--size));
            }
            int len = queue.size();
            // 从有序队列中取出元素放入List中
            while (len-- > 0) {
                list.add(queue.poll());
            }
        } else {
            // 不是有序队列时，使用排序方法
            SortedList.of(list, comparator).addAll(queue);
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
        // 遍历并将所有元素有序地加入到List中
        for (E e : iterable) {
            addIgnoreNull(e);
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
    public boolean remove(E o) {
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
    public boolean removeAll(Collection<E> c) {
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
    public boolean retainAll(Collection<E> c) {
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
            add(e, 0);
        }
    }

    /**
     * 新增元素
     *
     * @param e 数据
     * @param start 开始搜索的索引位置
     *
     * @return 添加后的数索引位置
     *
     * @since 1.0.6
     */
    private int add(E e, int start) {
        // 使用二分策略查找元素应插入的位置
        int end = list.size() - 1;
        while (start <= end) {
            int mid = start + ((end - start) >> 1);
            if (comparator.compare(e, list.get(mid)) >= 0) {
                // 向右边找
                start = mid + 1;
            } else {
                // 向左边找
                end = mid - 1;
            }
        }
        list.add(start, e);
        return start;
    }

    /**
     * 添加一个已排序的列表
     *
     * @param iterable 迭代器
     *
     * @since 1.0.6
     */
    public void addSorted(Iterable<E> iterable) {
        Iterator<E> iterator = iterable.iterator();
        int start = 0;
        while (iterator.hasNext()) {
            // 由于集合是有序的，所以当前元素插入的位置一定是下一个元素的开始位置，从而缩小查找范围
            start = add(iterator.next(), start) + 1;
        }
    }

    /**
     * 获取数据源，你最好只进行读操作，写操作极可能导致脏数据
     *
     * @return {@link T}
     *
     * @since 1.0.7
     */
    public T getOriginalList() {
        return list;
    }

    /**
     * 获取数据源（不可变集合）
     *
     * @return {@link T}
     *
     * @since 1.0.6
     */
    @SuppressWarnings("unchecked")
    public T getList() {
        return (T) Collections.unmodifiableList(list);
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
            // 对List进行排序
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
            throw new ComparatorException("you must set a comparator");
        }
    }
}
