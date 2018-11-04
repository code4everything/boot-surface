package org.code4everything.boot.xtool.bean;

import org.junit.Test;

public class ResponseResultTest {

    @Test
    public void testToString() {
        System.out.println(new ResponseResult(200, "test").toString());
    }
}
