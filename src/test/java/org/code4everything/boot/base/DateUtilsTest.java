package org.code4everything.boot.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import org.junit.Test;

import java.util.Date;

public class DateUtilsTest {

    @Test
    public void getStartOfToday() {
        Date date = DateUtils.getStartOfToday();
        Console.log(DateUtil.formatDateTime(date));
        date.setTime(System.currentTimeMillis());
        Console.log(DateUtil.formatDateTime(date));
        Console.log(DateUtil.formatDateTime(DateUtils.getStartOfToday()));
    }

    @Test
    public void getEndOfToday() {
        Date date = DateUtils.getEndOfToday();
        Console.log(DateUtil.formatDateTime(date));
        date.setTime(System.currentTimeMillis());
        Console.log(DateUtil.formatDateTime(date));
        Console.log(DateUtil.formatDateTime(DateUtils.getEndOfToday()));
    }
}