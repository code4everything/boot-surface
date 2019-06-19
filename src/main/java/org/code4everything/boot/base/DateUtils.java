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

    // -------------------当天--------------------------------

    private static long startOfToday = 0;

    private static Date startOfTodayCopier = new Date(0);

    private static long endOfToday = 0;

    private static Date endOfTodayCopier = new Date(0);

    // ------------------当月-------------------------------------------

    private static long startOfThisMonth = 0;

    private static Date startOfThisMonthCopier = new Date(0);

    private static long endOfThisMonth = 0;

    private static Date endOfThisMonthCopier = new Date(0);

    private DateUtils() {}

    /**
     * 获取本月的开始时间点
     *
     * @return 本月的开始
     *
     * @since 1.1.3
     */
    public static Date getStartOfThisMonth() {
        return checkThisMonth(true, startOfThisMonthCopier);
    }

    /**
     * 获取本月的结束时间点
     *
     * @return 本月的结束
     *
     * @since 1.1.3
     */
    public static Date getEndOfThisMonth() {
        return checkThisMonth(false, endOfThisMonthCopier);
    }

    /**
     * 获取今天的开始时间点
     *
     * @return 今天的开始
     *
     * @since 1.0.9
     */
    public static Date getStartOfToday() {
        return checkToday(true, startOfTodayCopier);
    }

    /**
     * 获取今天的结束时间点
     *
     * @return 今天的结束
     *
     * @since 1.0.9
     */
    public static Date getEndOfToday() {
        return checkToday(false, endOfTodayCopier);
    }

    /**
     * 使用副本来防止原引用被篡改
     */
    private static Date checkThisMonth(boolean isStart, Date copier) {
        long curr = System.currentTimeMillis();
        if (curr > endOfThisMonth) {
            // 如果当前的时间戳超过了endOfThisMonth的时间戳，说明ThisMonth的时间戳已经过期，需重新设置
            Date date = new Date();
            startOfThisMonth = DateUtil.beginOfMonth(date).getTime();
            endOfThisMonth = DateUtil.endOfMonth(date).getTime();
        }
        return check(isStart ? startOfThisMonth : endOfThisMonth, copier);
    }

    /**
     * 使用副本来防止原引用被篡改
     */
    private static Date checkToday(boolean isStart, Date copier) {
        long curr = System.currentTimeMillis();
        if (curr > endOfToday) {
            // 如果当前的时间戳超过了endOfToday的时间戳，说明Today的时间戳已经过期，需重新设置
            Date date = new Date();
            startOfToday = DateUtil.beginOfDay(date).getTime();
            endOfToday = DateUtil.endOfDay(date).getTime();
        }
        return check(isStart ? startOfToday : endOfToday, copier);
    }

    private static Date check(long origin, Date copier) {
        if (copier.getTime() != origin) {
            // 如果副本被篡改，那么重置副本
            copier.setTime(origin);
        }
        return copier;
    }
}
