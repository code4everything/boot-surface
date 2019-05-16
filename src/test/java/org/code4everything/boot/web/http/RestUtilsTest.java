package org.code4everything.boot.web.http;

import org.code4everything.boot.web.mvc.Response;
import org.junit.Test;

public class RestUtilsTest {

    @Test
    public void getTemplate() {
        Response response = RestUtils.getTemplate().getForObject("http://127.0.0.1:8088/common/current-time",
                                                                 Response.class);
        System.out.println(response);
    }
}