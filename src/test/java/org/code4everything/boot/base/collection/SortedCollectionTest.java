package org.code4everything.boot.base.collection;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Test;

import java.util.*;

public class SortedCollectionTest {

    @Test
    public void getCollection() {
        // 降序排列
        SortedList<Integer, List<Integer>> sortedList = SortedList.of(new ArrayList<>(), (i1, i2) -> i2 - i1);
        for (int i = 0; i < 1000; i++) {
            sortedList.add(RandomUtil.randomInt(999));
        }
        System.out.println(sortedList.getList());
        // 升序排列
        sortedList.setList(new ArrayList<>(), Comparator.comparingInt(i -> i));
        for (int i = 0; i < 1000; i++) {
            sortedList.add(RandomUtil.randomInt(999));
        }
        System.out.println(sortedList.getList());
    }

    @Test
    public void threadSafe() {
        SortedList<Integer, List<Integer>> sortedList = SortedList.ofConcurrent(new ArrayList<>(), (i1, i2) -> i2 - i1);
        for (int j = 0; j < 3; j++) {
            ThreadUtil.execute(() -> {
                for (int i = 0; i < 1000; i++) {
                    sortedList.addIgnoreNull(RandomUtil.randomInt(999));
                }
            });
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(sortedList.getList().size());
            if (i == 1) {
                ThreadUtil.execute(sortedList::clear);
            }
            ThreadUtil.sleep(1);
        }
        System.out.println(sortedList.getList());
    }

    @Test
    public void sortTo() {
        long start = System.currentTimeMillis();
        Queue<Integer> queue = new PriorityQueue<>();
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(7);
        list.add(2);
        queue.offer(1);
        queue.offer(9);
        queue.offer(6);
        System.out.println(SortedList.sortTo(queue, list, null));
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        SortedList<Integer, List<Integer>> sortedList = SortedList.of(new ArrayList<>(), Integer::compareTo);
        sortedList.addIgnoreNull(9);
        sortedList.addIgnoreNull(6);
        sortedList.addIgnoreNull(1);
        System.out.println(sortedList.getList());
        System.out.println(System.currentTimeMillis() - start);
    }
}
