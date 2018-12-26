package org.code4everything.boot.web.mvc;

import org.code4everything.boot.base.function.ResponseResultFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseControllerTest extends BaseController {

    @Test
    public void testIfReturn() {
        ifReturn(false, errorResult("one"));
        ifReturn(false, errorResult("two"));
        ifReturn(false, errorResult("three"));
        elseReturn((ResponseResultFunction<ArrayList<String>>) () -> parseCollection("error", Arrays.asList("a", "b")));
        System.out.println(getReturn());
    }
}
