package org.code4everything.boot.bean;

import cn.hutool.core.util.ObjectUtil;
import org.junit.Test;

/**
 * @author pantao
 * @since 2018/11/11
 **/
public class CommonTest {

    @Test
    public void isPrimitive() {
        assert ObjectUtil.isBasicType(Boolean.TRUE);
        assert ObjectUtil.isBasicType(new Integer(3));
        assert ObjectUtil.isBasicType(new Character('c'));
    }
}
