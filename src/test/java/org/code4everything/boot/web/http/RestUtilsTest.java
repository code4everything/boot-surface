package org.code4everything.boot.web.http;

import cn.hutool.core.lang.Console;
import org.code4everything.boot.web.mvc.Response;
import org.junit.Test;

public class RestUtilsTest {

    {
        RestUtils.setServerAddress("http://127.0.0.1:8088");
    }

    @Test
    public void getForObject() {
        Response response = RestUtils.getForObject("/common/current-time?ts={}", Response.class, 123);
        Console.log(response);
    }
}