package org.code4everything.boot.constant;

/**
 * 整数常量类
 *
 * @author pantao
 * @since 2018/11/2
 **/
public class IntegerConsts {

    public static final int ONE_THOUSAND_AND_TWENTY_FOUR = 1024;

    public static final int EIGHT = 8;

    private IntegerConsts() {}

    public static class FileSize {

        public static final long KB = ONE_THOUSAND_AND_TWENTY_FOUR;

        public static final long MB = KB * ONE_THOUSAND_AND_TWENTY_FOUR;

        public static final long GB = MB * ONE_THOUSAND_AND_TWENTY_FOUR;

        public static final long TB = GB * ONE_THOUSAND_AND_TWENTY_FOUR;

        private FileSize() {}
    }
}
