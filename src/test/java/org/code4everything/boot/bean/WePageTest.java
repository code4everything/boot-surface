package org.code4everything.boot.bean;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import org.junit.Test;

public class WePageTest {

    @Test
    public void serialize() {
        WePage<String> wePage = new WePage(1, 20, 6);
        wePage.setContent(Lists.newArrayList("test", "page"));
        Console.log(ObjectUtil.unserialize(ObjectUtil.serialize(wePage)));
    }
}