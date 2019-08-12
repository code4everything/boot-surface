package org.code4everything.boot.base;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.Objects;

/**
 * 判断工具类
 *
 * @author pantao
 * @since 2019/4/17
 */
public final class ObjectUtils {

    private ObjectUtils() {}

    /**
     * 是否所有对象都不为NULL
     *
     * @param objects 对象数组
     *
     * @return 所有对象都不为NULL
     *
     * @since 1.1.0
     */
    public static boolean isNotNull(Object... objects) {
        if (ArrayUtil.isNotEmpty(objects)) {
            for (Object object : objects) {
                if (Objects.isNull(object)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 是否所有对象均为NULL
     *
     * @param objects 对象数组
     *
     * @return 是否所有对象均为NULL
     *
     * @since 1.1.5
     */
    public static boolean isNull(Object... objects) {
        if (ArrayUtil.isNotEmpty(objects)) {
            for (Object object : objects) {
                if (ObjectUtil.isNotNull(object)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 是否有一个为NULL的对象
     *
     * @param objects 对象数组
     *
     * @return 是否有一个为NULL的对象
     *
     * @since 1.1.5
     */
    public static boolean isNullAny(Object... objects) {
        if (ArrayUtil.isNotEmpty(objects)) {
            for (Object object : objects) {
                if (Objects.isNull(object)) {
                    return true;
                }
            }
        }
        return false;
    }
}
