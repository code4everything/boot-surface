package org.code4everything.boot.bean;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 配置信息
 *
 * @author pantao
 * @since 2018/11/4
 */
public class ConfigBean implements BaseBean, Serializable {

    private static final long serialVersionUID = -7136966024637207548L;

    /**
     * 黑名单前缀
     *
     * @since 1.0.0
     */
    private String[] blackPrefixes;

    /**
     * 白名单前缀
     *
     * @since 1.0.0
     */
    private String[] whitePrefixes;

    /**
     * 拦截名单前缀
     *
     * @since 1.0.0
     */
    private String[] interceptPrefixes;

    /**
     * 获取黑名单前缀
     *
     * @return 黑名单前缀
     */
    public String[] getBlackPrefixes() {
        return blackPrefixes;
    }

    /**
     * 设置黑名单前缀
     *
     * @param blackPrefixes 黑名单前缀
     *
     * @return {@link ConfigBean}
     *
     * @since 1.0.0
     */
    public ConfigBean setBlackPrefixes(String[] blackPrefixes) {
        this.blackPrefixes = blackPrefixes;
        return this;
    }

    /**
     * 获取白名单前缀
     *
     * @return 白名单前缀
     *
     * @since 1.0.0
     */
    public String[] getWhitePrefixes() {
        return whitePrefixes;
    }

    /**
     * 设置白名单前缀
     *
     * @param whitePrefixes 白名单前缀
     *
     * @return {@link ConfigBean}
     *
     * @since 1.0.0
     */
    public ConfigBean setWhitePrefixes(String[] whitePrefixes) {
        this.whitePrefixes = whitePrefixes;
        return this;
    }

    /**
     * 获取拦截名单前缀
     *
     * @return 拦截名单前缀
     *
     * @since 1.0.0
     */
    public String[] getInterceptPrefixes() {
        return interceptPrefixes;
    }

    /**
     * 设置拦截名单前缀
     *
     * @param interceptPrefixes 拦截名单前缀
     *
     * @return {@link ConfigBean}
     *
     * @since 1.0.0
     */
    public ConfigBean setInterceptPrefixes(String[] interceptPrefixes) {
        this.interceptPrefixes = interceptPrefixes;
        return this;
    }

    @Override
    public String toString() {
        return "ConfigBean{" + "blackPrefixes=" + Arrays.toString(blackPrefixes) + ", whitePrefixes=" + Arrays.toString(whitePrefixes) + ", interceptPrefixes=" + Arrays.toString(interceptPrefixes) + '}';
    }
}
