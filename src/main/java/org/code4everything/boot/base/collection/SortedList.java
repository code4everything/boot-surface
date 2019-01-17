package org.code4everything.boot.base.collection;

import java.util.Comparator;
import java.util.List;

/**
 * 排序集合
 *
 * @author pantao
 * @since 2019/1/17
 **/
public class SortedList<T extends List<E>, E extends Comparable> {

    private T list;

    private Comparator<E> comparator;

    public SortedList(T list) {
        if (list == null) {
            throw new NullPointerException();
        }
        if (list.size() > 1) {
            list.forEach(this::sort);
        }
        this.list = list;
    }

    public void add(E e) {
        sort(e);
    }

    private void sort(E e) {
        if (this.list.isEmpty()) {
            list.add(e);
        } else {
            int idx = 0;
            for (int i = list.size() - 1; i >= 0; i--) {
                int compare = 0;
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
    }

    public T getList() {
        return list;
    }
}
