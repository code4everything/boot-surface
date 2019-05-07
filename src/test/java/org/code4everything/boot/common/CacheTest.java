package org.code4everything.boot.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.Test;

/**
 * @author pantao
 * @since 2019-05-07
 */
public class CacheTest {

    @Test
    public void testCache() {
        Cache<String, String> cache = CacheBuilder.newBuilder().build();
        assert cache.getIfPresent("test") == null;
        cache.put("test", "test");
        assert "test".equals(cache.getIfPresent("test"));
    }
}
