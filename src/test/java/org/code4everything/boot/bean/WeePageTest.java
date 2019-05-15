package org.code4everything.boot.bean;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import org.code4everything.boot.base.bean.WeePage;
import org.junit.Test;

public class WeePageTest {

    @Test
    public void serialize() {
        WeePage<String> weePage = new WeePage<>(1, 20, 6);
        weePage.setContent(Lists.newArrayList("test", "page"));
        weePage = ObjectUtil.unserialize(ObjectUtil.serialize(weePage));
        Console.log(weePage.toJsonString());
    }
}