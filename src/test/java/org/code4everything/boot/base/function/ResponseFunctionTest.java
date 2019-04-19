package org.code4everything.boot.base.function;

import cn.hutool.core.lang.Console;
import org.code4everything.boot.bean.LogBean;
import org.code4everything.boot.bean.Response;
import org.junit.Test;

public class ResponseFunctionTest {

    @Test
    public void test() {
        testCall(() -> new Response<>(new LogBean()));
    }

    private void testCall(ResponseFunction<LogBean> function) {
        Console.log(function.call());
    }
}