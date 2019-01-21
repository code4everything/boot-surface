package org.code4everything.boot.bean;

import org.junit.Test;

public class ResponseTest {

    @Test
    public void testToString() {
        System.out.println(new Response(200, "test").toString());
    }
}
