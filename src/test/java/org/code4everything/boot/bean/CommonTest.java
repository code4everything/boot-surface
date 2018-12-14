package org.code4everything.boot.bean;

import cn.hutool.core.bean.BeanUtil;
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

    @Test
    public void testCopyProperties() {
        ResponseResult result1 = new ResponseResult().setCode(200);
        ResponseResult result2 = new ResponseResult().setCode(400);
        BeanUtil.copyProperties(result1, result2);
        System.out.println(result1);
        System.out.println(result2);
    }
}
