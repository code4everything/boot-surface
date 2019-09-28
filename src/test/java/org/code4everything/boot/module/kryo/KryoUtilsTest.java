package org.code4everything.boot.module.kryo;

import cn.hutool.core.util.RandomUtil;
import junit.framework.TestCase;
import org.code4everything.boot.base.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class KryoUtilsTest extends TestCase {

    @Test
    public void test() throws FileNotFoundException {
        Bean bean = new Bean(RandomUtil.randomString(1024));
        File serialized = new File(FileUtils.currentWorkDir("bean.bin"));
        KryoUtils.serialize(bean, serialized);
        assert bean.equals(KryoUtils.deserialize(serialized, Bean.class));
        serialized.delete();
    }

    static class Bean {

        String str;

        Bean() {}

        Bean(String str) {
            this.str = str;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Bean bean = (Bean) o;
            return Objects.equals(str, bean.str);
        }

        @Override
        public int hashCode() {
            return Objects.hash(str);
        }
    }
}
