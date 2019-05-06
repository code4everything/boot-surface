package org.code4everything.boot.bean;

import cn.hutool.core.util.ObjectUtil;
import org.code4everything.boot.web.mvc.Response;
import org.code4everything.boot.web.mvc.FilterPath;
import org.junit.Test;

public class ResponseTest {

    @Test
    public void testToString() {
        System.out.println(new Response(200, "test").toString());
    }

    @Test
    public void serialize() {
        Response<FilterPath> response = new Response<>();
        System.out.println(response);
        response.setCode(404);
        response.setMsg("test");
        response.setTimestamp();
        FilterPath bean = new FilterPath();
        bean.setWhitePrefixes(new String[]{"white"});
        bean.setBlackPrefixes(new String[]{"black"});
        bean.setInterceptPrefixes(new String[]{"intercept"});
        response.setData(bean);
        System.out.println(response);
        response = ObjectUtil.unserialize(ObjectUtil.serialize(response));
        System.out.println(response);
    }
}
