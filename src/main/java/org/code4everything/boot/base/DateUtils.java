package org.code4everything.boot.base;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * 日期工具库
 *
 * @author pantao
 * @since 2019/3/22
 **/
public final class DateUtils {

    private static Date startOfToday = new Date(0);

    private static Date copiedStartOfToday = new Date(0);

    private static Date endOfToday = new Date(0);

    private static Date copiedEndOfToday = new Date(0);

    private DateUtils() {}

    /**
     * 获取今天的开始时间点
     *
     * @return 今天的开始
     *
     * @since 1.0.9
     */
    public static Date getStartOfToday() {
        return checkToday(startOfToday, copiedStartOfToday);
    }

    /**
     * 获取今天的结束时间点
     *
     * @return 今天的结束
     *
     * @since 1.0.9
     */
    public static Date getEndOfToday() {
        return checkToday(endOfToday, copiedEndOfToday);
    }

    /**
     * 使用副本来防止原引用被篡改
     */
    private static Date checkToday(Date origin, Date copied) {
        long curr = System.currentTimeMillis();
        if (curr > endOfToday.getTime()) {
            // 如果当前的时间戳超过了endOfToday的时间戳，说明Today的时间戳已经过期，需重新设置
            startOfToday.setTime(DateUtil.beginOfDay(new Date(curr)).getTime());
            endOfToday.setTime(DateUtil.endOfDay(startOfToday).getTime());
        }
        if (!origin.equals(copied)) {
            // 如果副本被篡改，那么重置副本
            copied.setTime(origin.getTime());
        }
        return copied;
    }
}
