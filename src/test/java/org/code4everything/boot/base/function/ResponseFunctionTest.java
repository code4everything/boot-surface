package org.code4everything.boot.base.function;

import cn.hutool.core.lang.Console;
import org.code4everything.boot.log.MethodLog;
import org.code4everything.boot.base.bean.Response;
import org.junit.Test;

public class ResponseFunctionTest {

    @Test
    public void test() {
        testCall(() -> new Response<>(new MethodLog()));
    }

    private void testCall(ResponseFunction<MethodLog> function) {
        Console.log(function.call());
    }
}