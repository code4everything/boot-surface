package org.code4everything.boot.base.collection;

import cn.hutool.core.util.RandomUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortedCollectionTest {

    @Test
    public void getCollection() {
        // 升序排列
        SortedList<Integer, List<Integer>> sortedList = new SortedList<>(new ArrayList<>(), (i1, i2) -> i2 - i1);
        for (int i = 0; i < 1000; i++) {
            sortedList.add(RandomUtil.randomInt(999));
        }
        System.out.println(sortedList.getList());
        // 测试已有数据
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(RandomUtil.randomInt(999));
        }
        sortedList.setList(list);
        System.out.println(list);
        // 降序排列
        sortedList.setList(new ArrayList<>(), Comparator.comparingInt(i -> i));
        for (int i = 0; i < 1000; i++) {
            sortedList.add(RandomUtil.randomInt(999));
        }
        System.out.println(sortedList.getList());
        // 测试已有数据
        list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(RandomUtil.randomInt(999));
        }
        sortedList.setList(list, Comparator.comparingInt(o -> o));
        System.out.println(list);
    }
}
