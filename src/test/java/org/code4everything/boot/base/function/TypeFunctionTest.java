package org.code4everything.boot.base.function;

import cn.hutool.core.lang.Console;
import org.code4everything.boot.web.mvc.Response;
import org.junit.Test;

public class TypeFunctionTest {

    @Test
    public void test() {
        String str = ((TypeFunction<String>) () -> "test").call();
        Console.log(str);
        testCall(Response::new);
    }

    private void testCall(TypeFunction<Response> function) {
        Console.log(function.call());
    }
}