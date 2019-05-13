package org.code4everything.boot.base;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import org.code4everything.boot.base.constant.IntegerConsts;

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
     * 替换键
     *
     * @param map 集合
     * @param oldKey 旧的键
     * @param newKey 新的键
     * @param <K> 键类型
     * @param <V> 值类型
     *
     * @since 1.1.2
     */
    public static <K, V> void replaceKey(Map<K, V> map, K oldKey, K newKey) {
        replaceKey(map, oldKey, newKey, true);
    }

    /**
     * 替换键
     *
     * @param map 集合
     * @param oldKey 旧的键
     * @param newKey 新的键
     * @param required 是否不允许键的值为空指针
     * @param <K> 键类型
     * @param <V> 值类型
     *
     * @since 1.1.2
     */
    public static <K, V> void replaceKey(Map<K, V> map, K oldKey, K newKey, boolean required) {
        V value = map.get(oldKey);
        if (required) {
            Objects.requireNonNull(value, "map key must has value");
        }
        map.remove(oldKey);
        map.put(newKey, value);
    }

    /**
     * 新建 HashMap
     *
     * @param kvs 键值对
     * @param <K> 键类型
     * @param <V> 值类型
     *
     * @return HashMap
     *
     * @since 1.1.2
     */
    public static <K, V> HashMap<K, V> newHashMap(Object... kvs) {
        return newHashMap(16, kvs);
    }

    /**
     * 新建 HashMap
     *
     * @param capacity 初始化大小
     * @param kvs 键值对
     * @param <K> 键类型
     * @param <V> 值类型
     *
     * @return HashMap
     *
     * @since 1.1.2
     */
    public static <K, V> HashMap<K, V> newHashMap(int capacity, Object... kvs) {
        HashMap<K, V> map = new HashMap<>(capacity);
        putKeyValue(map, kvs);
        return map;
    }

    /**
     * 添加键值对
     *
     * @param map 集合
     * @param kvs 键值对
     * @param <K> 键类型
     * @param <V> 值类型
     *
     * @since 1.1.2
     */
    @SuppressWarnings("unchecked")
    public static <K, V> void putKeyValue(Map<K, V> map, Object... kvs) {
        if (ArrayUtil.isEmpty(kvs)) {
            return;
        }
        if ((kvs.length & 1) == 1) {
            throw new IllegalArgumentException("kvs length must be even(key value pair)");
        }
        for (int i = 0; i < kvs.length; i += IntegerConsts.TWO) {
            map.put((K) kvs[i], (V) kvs[i + 1]);
        }
    }

    /**
     * 值替换
     *
     * @param map 集合
     * @param regex 正则匹配
     * @param replacement 替换内容
     *
     * @return 替换后的集合
     *
     * @since 1.1.2
     */
    public static <T> Map<T, String> valueReplaceByRegex(Map<T, String> map, String regex, String replacement) {
        if (MapUtil.isEmpty(map)) {
            return new HashMap<>(8);
        }
        Map<T, String> tmp = new HashMap<>(map.size());
        map.forEach((k, v) -> tmp.put(k, v.replaceAll(regex, replacement)));
        return tmp;
    }

    /**
     * 值替换
     *
     * @param map 集合
     * @param target 目标内容
     * @param replacement 替换内容
     *
     * @return 替换后的集合
     *
     * @since 1.1.2
     */
    public static <T> Map<T, String> valueReplace(Map<T, String> map, CharSequence target, CharSequence replacement) {
        if (MapUtil.isEmpty(map)) {
            return new HashMap<>(8);
        }
        Map<T, String> tmp = new HashMap<>(map.size());
        map.forEach((k, v) -> tmp.put(k, v.replace(target, replacement)));
        return tmp;
    }

    /**
     * 键替换
     *
     * @param map 集合
     * @param regex 正则匹配
     * @param replacement 替换内容
     *
     * @return 替换后的集合
     *
     * @since 1.1.2
     */
    public static <T> Map<String, T> keyReplaceByRegex(Map<String, T> map, String regex, String replacement) {
        if (MapUtil.isEmpty(map)) {
            return new HashMap<>(8);
        }
        Map<String, T> tmp = new HashMap<>(map.size());
        map.forEach((k, v) -> tmp.put(k.replaceAll(regex, replacement), v));
        return tmp;
    }

    /**
     * 键替换
     *
     * @param map 集合
     * @param target 目标内容
     * @param replacement 替换内容
     *
     * @return 替换后的集合
     *
     * @since 1.1.2
     */
    public static <T> Map<String, T> keyReplace(Map<String, T> map, CharSequence target, CharSequence replacement) {
        if (MapUtil.isEmpty(map)) {
            return new HashMap<>(8);
        }
        Map<String, T> tmp = new HashMap<>(map.size());
        map.forEach((k, v) -> tmp.put(k.replace(target, replacement), v));
        return tmp;
    }

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
