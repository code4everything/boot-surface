package org.code4everything.boot.base.collection;

import cn.hutool.core.util.RandomUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SortedCollectionTest {

    @Test
    public void getCollection() {
        SortedList<List<Integer>, Integer> sortedList = new SortedList<>(new ArrayList<>());
        for (int i = 0; i < 100; i++) {
            sortedList.add(RandomUtil.randomInt(999));
        }
        System.out.println(sortedList.getList());
    }
}
