package org.code4everything.boot.base;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类
 *
 * @author pantao
 * @since 2019/8/21
 */
public class StringUtils {

    private StringUtils() {}

    /**
     * 裁剪字符串
     *
     * @param string 原字符串
     * @param trimChar 需裁剪的字符
     * @param mode 如果 {@code mode&3==1}，则裁剪尾部，{@code ==2}裁剪首部，{@code ==3}首尾均裁剪
     *
     * @return 裁剪后的字符串
     *
     * @since 1.1.6
     */
    public static String trim(String string, char trimChar, int mode) {
        int start = 0;
        int end = string.length();
        boolean trimStart = (mode & 2) == 2;
        if (trimStart) {
            for (; start < end; start++) {
                if (string.charAt(start) != trimChar) {
                    break;
                }
            }
        }
        boolean trimEnd = (mode & 1) == 1;
        if (trimEnd) {
            for (; end > start; end--) {
                if (string.charAt(end - 1) != trimChar) {
                    break;
                }
            }
        }
        return string.substring(start, end);
    }

    /**
     * 裁剪字符串
     *
     * @param string 原字符串
     * @param trimStr 需裁剪的字符串
     *
     * @return 裁剪后的字符串
     *
     * @since 1.1.6
     */
    public static String trim(String string, String trimStr) {
        if (StrUtil.isEmpty(trimStr)) {
            return string;
        }
        int start = trimHelper(string, trimStr, 0, string.length(), trimStr.length());
        int end = trimHelper(string, trimStr, start, string.length(), -trimStr.length());
        return string.substring(start, end);
    }

    /**
     * 裁剪字符串
     *
     * @param string 原字符串
     * @param trimStr 需裁剪的字符串
     *
     * @return 裁剪后的字符串
     *
     * @since 1.1.6
     */
    public static String trimStart(String string, String trimStr) {
        if (StrUtil.isEmpty(trimStr)) {
            return string;
        }
        return string.substring(trimHelper(string, trimStr, 0, string.length(), trimStr.length()));
    }

    /**
     * 裁剪字符串
     *
     * @param string 原字符串
     * @param trimStr 需裁剪的字符串
     *
     * @return 裁剪后的字符串
     *
     * @since 1.1.6
     */
    public static String trimEnd(String string, String trimStr) {
        if (StrUtil.isEmpty(trimStr)) {
            return string;
        }
        return string.substring(0, trimHelper(string, trimStr, 0, string.length(), -trimStr.length()));
    }

    private static int trimHelper(String str, String trim, int start, int end, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step must not be zero");
        }
        if (step > 0) {
            // 裁剪前面
            if (shouldTrim(str, start, end, trim)) {
                return trimHelper(str, trim, start + step, end, step);
            }
            return start;
        } else {
            // 裁剪后面
            int newEnd = end + step;
            if (shouldTrim(str, Math.max(start, newEnd), end, trim)) {
                return trimHelper(str, trim, start, newEnd, step);
            }
            return end;
        }
    }

    private static boolean shouldTrim(String str, int start, int end, String trim) {
        if (end - start < trim.length()) {
            return false;
        }
        for (int i = 0; i < trim.length(); i++, start++) {
            if (str.charAt(start) != trim.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
