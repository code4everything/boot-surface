package org.code4everything.boot.base.collection;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程安全的排序集合
 *
 * @author pantao
 * @since 2019-01-19
 */
@ThreadSafe
public class ConcurrentSortedList<E, T extends List<E>> extends SortedList<E, T> {

    /**
     * 重入锁
     *
     * @since 1.0.6
     */
    private final ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * 无参构造函数
     *
     * @since 1.0.6
     */
    public ConcurrentSortedList() {}

    /**
     * 构造函数
     *
     * @param list 数据源
     *
     * @since 1.0.6
     */
    public ConcurrentSortedList(T list) {
        super.setList(list);
    }

    /**
     * 构造函数
     *
     * @param list 数据源
     * @param comparator 比较器
     *
     * @since 1.0.6
     */
    public ConcurrentSortedList(T list, Comparator<E> comparator) {
        super.setList(list, comparator);
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
    public static <E, T extends List<E>> ConcurrentSortedList<E, T> of(T list, Comparator<E> comparator) {
        return new ConcurrentSortedList<>(list, comparator);
    }

    @Override
    public void setList(T list, Comparator<E> comparator) {
        reentrantLock.lock();
        super.setList(list, comparator);
        reentrantLock.unlock();
    }

    @Override
    public T getList() {
        reentrantLock.lock();
        T t = super.getList();
        reentrantLock.unlock();
        return t;
    }

    @Override
    public void setList(T list) {
        reentrantLock.lock();
        super.setList(list);
        reentrantLock.unlock();
    }

    @Override
    public void addAll(Iterable<E> iterable) {
        reentrantLock.lock();
        super.addAll(iterable);
        reentrantLock.unlock();
    }

    @Override
    public void add(E e) {
        reentrantLock.lock();
        super.add(e);
        reentrantLock.unlock();
    }

    @Override
    public boolean remove(Object o) {
        reentrantLock.lock();
        boolean res = list.remove(o);
        reentrantLock.unlock();
        return res;
    }

    @Override
    public boolean removeAll(Collection<E> c) {
        reentrantLock.lock();
        boolean res = list.removeAll(c);
        reentrantLock.unlock();
        return res;
    }

    @Override
    public boolean retainAll(Collection<E> c) {
        reentrantLock.lock();
        boolean res = list.retainAll(c);
        reentrantLock.unlock();
        return res;
    }

    @Override
    public void clear() {
        reentrantLock.lock();
        list.clear();
        reentrantLock.unlock();
    }

    @Override
    public E remove(int index) {
        reentrantLock.lock();
        E e = list.remove(index);
        reentrantLock.unlock();
        return e;
    }

    @Override
    public void addSorted(Iterable<E> iterable) {
        reentrantLock.lock();
        super.addSorted(iterable);
        reentrantLock.unlock();
    }

    @Override
    public void addIgnoreNull(E e) {
        reentrantLock.lock();
        super.addIgnoreNull(e);
        reentrantLock.unlock();
    }

    /**
     * 在多线程环境下，请不要调用此方法（极可能出错）
     *
     * @return {@link T}
     *
     * @since 1.0.7
     */
    @Override
    @Deprecated
    public T getOriginalList() {
        return null;
    }
}
