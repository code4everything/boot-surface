package org.code4everything.boot.common;

import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author pantao
 * @since 2019/6/14
 **/
public class JsonTest {

    @Test
    public void testJson() {
        List<String> list = Lists.newArrayList("a", "b", "c");
        Console.log(JSONObject.parse(JSONObject.toJSONString(list)));
    }
}
