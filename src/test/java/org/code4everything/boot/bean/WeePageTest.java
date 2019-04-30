package org.code4everything.boot.bean;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import org.junit.Test;

public class WeePageTest {

    @Test
    public void serialize() {
        WeePage<String> weePage = new WeePage(1, 20, 6);
        weePage.setContent(Lists.newArrayList("test", "page"));
        Console.log(ObjectUtil.unserialize(ObjectUtil.serialize(weePage)));
    }
}