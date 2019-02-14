package org.code4everything.boot.base.collection;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author pantao
 * @since 2019/2/14
 **/
public class JumpyQueue<E> extends AbstractQueue<E> implements Serializable {

    private LinkedList<E> nodes;

    public JumpyQueue() {
        nodes = new LinkedList<>();
    }

    public JumpyQueue(Collection<? extends E> c) {
        nodes = new LinkedList<>();
        nodes.addAll(c);
    }

    public JumpyQueue(boolean synchronize) {
        nodes = (LinkedList<E>) Collections.synchronizedList(new LinkedList<>());
    }

    public JumpyQueue(Collection<? extends E> c, boolean synchronize) {
        nodes = (LinkedList<E>) Collections.synchronizedList(new LinkedList<>());
        nodes.addAll(c);
    }

    @Override
    public Iterator<E> iterator() {
        return nodes.iterator();
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return nodes.removeIf(filter);
    }

    @Override
    public Spliterator<E> spliterator() {
        return nodes.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return nodes.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return nodes.parallelStream();
    }

    @Override
    public int size() {
        return nodes.size();
    }

    public void jumpFirst(E e) {
        if (nodes.remove(e)) {
            nodes.addFirst(e);
        }
    }

    public void jumpFirst(int index) {
        nodes.addFirst(nodes.remove(index));
    }

    public void jumpLast(E e) {
        if (nodes.remove(e)) {
            nodes.addLast(e);
        }
    }

    public void jumpLast(int index) {
        nodes.addLast(nodes.remove(index));
    }

    public void jump(E source, int targetIndex) {
        if (nodes.remove(source)) {
            nodes.add(targetIndex, source);
        }
    }

    public void jump(E source, E target) {
        jump(source, nodes.indexOf(target));
    }

    public void jump(int sourceIndex, E target) {
        nodes.add(nodes.indexOf(target), nodes.remove(sourceIndex));
    }

    public void jump(int sourceIndex, int targetIndex) {
        nodes.add(targetIndex, nodes.remove(sourceIndex));
    }

    @Override
    public boolean offer(E e) {
        return nodes.offerLast(e);
    }

    @Override
    public E poll() {
        return nodes.pollFirst();
    }

    @Override
    public E peek() {
        return nodes.peekFirst();
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}
