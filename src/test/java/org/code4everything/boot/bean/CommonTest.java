package org.code4everything.boot.bean;

import org.junit.Test;

/**
 * @author pantao
 * @since 2018/11/11
 **/
public class CommonTest {

    @Test
    public void testClass() {
        Object integer = 8;
        assert integer.getClass() == Integer.class;
    }
}
