package org.code4everything.boot.web.mvc;

import org.code4everything.boot.base.function.ResponseResultFunction;
import org.code4everything.boot.bean.ResponseResult;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;

public class BaseControllerTest extends BaseController {

    @Test
    public void testIfReturn() {
        ifReturn(false, errorResult("one"));
        ifReturn(false, errorResult("two"));
        ifReturn(false, errorResult("three"));
        elseReturn(new ResponseResultFunction() {
            @Override
            public <T extends Serializable> ResponseResult<T> get() {
                return parseCollection("error", Arrays.asList("a", "b"));
            }
        });
        System.out.println(getReturn());
    }
}
