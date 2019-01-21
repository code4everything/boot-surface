package org.code4everything.boot.web.mvc;

import org.code4everything.boot.base.function.ResponseFunction;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BaseControllerTest extends BaseController {

    @Test
    public void testIfReturn() {
        ifReturn(false, errorResult("one"));
        ifReturn(false, errorResult("two"));
        ifReturn(false, errorResult("three"));
        elseReturn((ResponseFunction<List<String>>) () -> parseCollection("error", Arrays.asList("a", "b")));
        System.out.println(getReturn());
    }
}
