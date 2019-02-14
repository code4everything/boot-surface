package org.code4everything.boot.base.collection;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * @author pantao
 * @since 2019/2/14
 **/
public class JumpyQueue<E> extends AbstractQueue<E> implements Serializable {

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }
}
