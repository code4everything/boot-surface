package org.code4everything.boot.web.http;

import cn.hutool.core.lang.Console;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.web.mvc.Response;
import org.junit.Test;

public class RestUtilsTest {

    {
        BootConfig.setDebug(true);
        BootConfig.setRestServer("http://127.0.0.1:8088");
    }

    @Test
    public void getForObject() {
        Response response = RestUtils.getForObject("/common/current-time?ts={}", Response.class, 123);
        Console.log(response);
    }

    @Test
    public void postForObject() {
        Response response = RestUtils.postForObject("/common/test", new Response<>(), Response.class);
        Console.log(response);
    }
}