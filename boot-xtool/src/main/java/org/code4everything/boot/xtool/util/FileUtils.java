package org.code4everything.boot.xtool.util;

import org.code4everything.boot.xtool.constant.SymbolConsts;

/**
 * 文件工具类
 *
 * @author pantao
 * @since 2018/11/2
 **/
public class FileUtils {

    /**
     * 获取文件后缀（包括点号）
     *
     * @param filename 文件名
     *
     * @return 文件后缀
     *
     * @since 1.0.0
     */
    public static String getSuffix(String filename) {
        return getSuffix(filename, true);
    }

    /**
     * 获取文件后缀
     *
     * @param filename 文件名
     * @param withDot 是否包括点号
     *
     * @return 文件后缀
     *
     * @since 1.0.0
     */
    public static String getSuffix(String filename, boolean withDot) {
        return filename.substring(filename.lastIndexOf(SymbolConsts.DOT) + (withDot ? 0 : 1));
    }
}
