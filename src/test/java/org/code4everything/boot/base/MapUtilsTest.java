package org.code4everything.boot.base;

import cn.hutool.core.util.RandomUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapUtilsTest {

    @Test
    public void sortByValue() {
        Map<String, Long> map = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put(RandomUtil.randomString(9), RandomUtil.randomLong(Integer.MAX_VALUE));
        }
        Map<String, Long> sortedMap = MapUtils.sortByValue(map, Long::compareTo);
        sortedMap.values().forEach(val -> System.out.println(val + ", "));
    }
}