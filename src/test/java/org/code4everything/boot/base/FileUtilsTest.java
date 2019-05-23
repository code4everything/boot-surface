package org.code4everything.boot.base;

import org.junit.Test;

public class FileUtilsTest {

    @Test
    public void formatSize() {
        assert "0 B".equals(FileUtils.formatSize(0, 0));
        assert "567 B".equals(FileUtils.formatSize(567, 1));
        assert "748.48 KB".equals(FileUtils.formatSize(748_489, 2));
        assert "748.4 KB".equals(FileUtils.formatSize(748_489, 1));
        assert "699 MB".equals(FileUtils.formatSize(699_781_896, 0));
        assert "699.781 MB".equals(FileUtils.formatSize(699_781_896, 3));
    }
}