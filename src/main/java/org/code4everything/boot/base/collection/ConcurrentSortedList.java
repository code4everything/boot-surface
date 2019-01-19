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

    public ConcurrentSortedList(T list) {
        super(list);
    }

    public ConcurrentSortedList(T list, Comparator<E> comparator) {
        super(list, comparator);
    }

    @Override
    public void setList(T list) {
        reentrantLock.lock();
        super.setList(list);
        reentrantLock.unlock();
    }

    @Override
    public void setList(T list, Comparator<E> comparator) {
        reentrantLock.lock();
        super.setList(list, comparator);
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

    public boolean remove(Object o) {
        reentrantLock.lock();
        boolean res = list.remove(o);
        reentrantLock.unlock();
        return res;
    }

    public boolean removeAll(Collection<?> c) {
        reentrantLock.lock();
        boolean res = list.removeAll(c);
        reentrantLock.unlock();
        return res;
    }

    public boolean retainAll(Collection<?> c) {
        reentrantLock.lock();
        boolean res = list.retainAll(c);
        reentrantLock.unlock();
        return res;
    }

    public void clear() {
        reentrantLock.lock();
        list.clear();
        reentrantLock.unlock();
    }

    public E remove(int index) {
        reentrantLock.lock();
        E e = list.remove(index);
        reentrantLock.unlock();
        return e;
    }

    @Override
    public void addIgnoreNull(E e) {
        reentrantLock.lock();
        super.addIgnoreNull(e);
        reentrantLock.unlock();
    }
}
