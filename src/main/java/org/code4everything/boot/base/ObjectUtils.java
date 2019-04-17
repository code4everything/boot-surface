package org.code4everything.boot.base;

import cn.hutool.core.util.ArrayUtil;

import java.util.Objects;

/**
 * 判断工具类
 *
 * @author pantao
 * @since 2019/4/17
 **/
public class ObjectUtils {

    private ObjectUtils() {}

    /**
     * 所有对象都不为NULL
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
}
