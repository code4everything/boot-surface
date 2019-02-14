package org.code4everything.boot.base.collection;

import org.code4everything.boot.constant.IntegerConsts;

import java.io.Serializable;
import java.util.*;

/**
 * @author pantao
 * @since 2019/2/14
 **/
public class JumpyQueue<E> extends AbstractQueue<E> implements Serializable {

    private transient JumpyNode head;

    private transient JumpyNode tail;

    private int size = 0;

    private int modCount = 0;

    public JumpyQueue() {}

    @SuppressWarnings("unchecked")
    public JumpyQueue(Collection<? extends E> c) {
        E[] es = (E[]) c.toArray();
        for (int i = 0; i < es.length; i++) {
            offer(es[i]);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new JumpyQueueIterator();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean offer(E e) {
        modCount++;
        JumpyNode node = new JumpyNode(e);
        if (size++ == 0) {
            head = tail = node;
        } else {
            tail.next = tail = node;
        }
        return true;
    }

    @Override
    public E poll() {
        if (head == null) {
            return null;
        }
        modCount++;
        E e = head.node;
        head = head.next;
        size--;
        return e;
    }

    @Override
    public E peek() {
        return head == null ? null : head.node;
    }

    @Override
    public String toString() {
        if (head == null) {
            return "[]";
        }
        String nodeStr = head.node.toString();
        StringBuilder sb = new StringBuilder((nodeStr.length() + 2) * size);
        sb.append("[").append(nodeStr);
        JumpyNode current = head.next;
        while (current != null) {
            sb.append(", ").append(current.node.toString());
            current = current.next;
        }
        return sb.append("]").toString();
    }

    private class JumpyNode {

        private E node;

        private JumpyNode next;

        JumpyNode(E node) {
            this.node = node;
        }
    }

    private final class JumpyQueueIterator implements Iterator<E> {

        private int index = 0;

        private int hardSize = size;

        private int exceptionModCount = modCount;

        private JumpyNode hardHead = head;

        private JumpyNode previous;

        @Override
        public boolean hasNext() {
            return index < hardSize;
        }

        @Override
        public E next() {
            if (exceptionModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            index++;
            if (index == 1) {
                return head.node;
            } else if (index == IntegerConsts.TWO) {
                previous = hardHead;
                return previous.next.node;
            } else {
                previous = previous.next;
                return previous.next.node;
            }
        }

        @Override
        public void remove() {
            if (exceptionModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (head == null) {
                throw new IllegalStateException();
            }
            if (previous == null) {
                head = head.next;
            } else {
                previous.next = previous.next.next;
            }
            size--;
        }
    }
}
