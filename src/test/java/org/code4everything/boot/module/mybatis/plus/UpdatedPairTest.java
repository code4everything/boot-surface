package org.code4everything.boot.module.mybatis.plus;

import cn.hutool.core.lang.Console;
import org.code4everything.boot.web.mvc.Response;
import org.junit.Test;

public class UpdatedPairTest {

    private UpdatedPair<Response> pair = new UpdatedPair<>(null, null);

    @Test
    public void old2JsonString() {
        Console.log(pair.old2JsonString());
    }

    @Test
    public void new2JsonString() {
        Console.log(pair.new2JsonString());
    }
}