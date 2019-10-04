package org.code4everything.boot.base;

import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void trim() {
        assert " b c dda".equals(StringUtils.trim("aa b c dda", "aa"));
        assert "".equals(StringUtils.trim("abcabcabcabc", "abc"));
    }

    @Test
    public void trimStart() {
        assert "kkgh".equals(StringUtils.trimStart("ghghkkgh", "gh"));
    }

    @Test
    public void trimEnd() {
        assert "aab".equals(StringUtils.trimEnd("aabuiuiuiuiui", "ui"));
        assert "".equals(StringUtils.trimEnd("123123123123123", "123"));
    }

    @Test
    public void testTrim() {
        String str = "0001001110011000";
        assert StringUtils.trim(str, "0").equals(StringUtils.trim(str, '0', StringUtils.BOTH_TRIM));
        assert StringUtils.trimStart(str, "0").equals(StringUtils.trim(str, '0', StringUtils.PREFIX_TRIM));
        assert StringUtils.trimEnd(str, "0").equals(StringUtils.trim(str, '0', StringUtils.SUFFIX_TRIM));
    }
}
