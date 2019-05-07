package org.code4everything.boot.base;

import java.util.*;

/**
 * 集合工具类
 *
 * @author pantao
 * @since 2019/4/19
 **/
public final class MapUtils {

    private MapUtils() {}

    /**
     * 通过值排序
     *
     * @param map 集合
     * @param <K> 键类型
     * @param <V> 值类型
     *
     * @return 排序后的 {@link LinkedHashMap}
     *
     * @since 1.1.0
     */
    public static <K, V extends Comparable<V>> LinkedHashMap<K, V> sortByValue(Map<K, V> map) {
        return sortByValue(map, Comparable::compareTo);
    }

    /**
     * 通过值排序
     *
     * @param map 集合
     * @param comparator 比较器
     * @param <K> 键类型
     * @param <V> 值类型
     *
     * @return 排序后的 {@link LinkedHashMap}
     *
     * @since 1.1.0
     */
    public static <K, V> LinkedHashMap<K, V> sortByValue(Map<K, V> map, Comparator<V> comparator) {
        return sortByKeyAndValue(map, (entry1, entry2) -> comparator.compare(entry1.getValue(), entry2.getValue()));
    }

    /**
     * 通过值排序
     *
     * @param map 集合
     * @param comparator 比较器
     * @param <K> 键类型
     * @param <V> 值类型
     *
     * @return 排序后的 {@link LinkedHashMap}
     *
     * @since 1.1.0
     */
    public static <K, V> LinkedHashMap<K, V> sortByKeyAndValue(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(comparator);
        LinkedHashMap<K, V> sortedMap = new LinkedHashMap<>(list.size());
        list.forEach(entry -> sortedMap.put(entry.getKey(), entry.getValue()));
        return sortedMap;
    }
}
