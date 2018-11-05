package org.code4everything.boot.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 跨域配置
 *
 * @author pantao
 * @since 2018/10/30
 **/
public class CorsBean implements Serializable {

    /**
     * 路径模式
     *
     * @since 1.0.0
     */
    private String path;

    /**
     * 请求源
     *
     * @since 1.0.0
     */
    private List<String> origins;

    /**
     * 请求头
     *
     * @since 1.0.0
     */
    private List<String> headers;

    /**
     * 请求方法
     *
     * @since 1.0.0
     */
    private List<String> methods;

    /**
     * 证书
     *
     * @since 1.0.0
     */
    private Boolean credential;

    /**
     * 设置请求源
     *
     * @param origins 请求源
     *
     * @return {@link CorsBean}
     *
     * @since 1.0.0
     */
    public CorsBean setOrigins(List<String> origins) {
        this.origins = origins;
        return this;
    }

    /**
     * 设置请求头
     *
     * @param headers 请求头
     *
     * @return {@link CorsBean}
     *
     * @since 1.0.0
     */
    public CorsBean setHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 设置请求方法
     *
     * @param methods 方法
     *
     * @return {@link CorsBean}
     *
     * @since 1.0.0
     */
    public CorsBean setMethods(List<String> methods) {
        this.methods = methods;
        return this;
    }

    /**
     * 获取路径模式
     *
     * @return 路径模式
     *
     * @since 1.0.0
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置路径模式
     *
     * @param path 路径模式
     *
     * @return {@link CorsBean}
     *
     * @since 1.0.0
     */
    public CorsBean setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * 获取请求源
     *
     * @return 请求源
     *
     * @since 1.0.0
     */
    public List<String> getOrigins() {
        return origins;
    }

    /**
     * 设置请求源
     *
     * @param origins 请求源
     *
     * @return {@link CorsBean}
     *
     * @since 1.0.0
     */
    public CorsBean setOrigins(String... origins) {
        this.origins = Arrays.asList(origins);
        return this;
    }

    /**
     * 获取请求头
     *
     * @return 请求头
     *
     * @since 1.0.0
     */
    public List<String> getHeaders() {
        return headers;
    }

    /**
     * 设置请求头
     *
     * @param headers 请求头
     *
     * @return {@link CorsBean}
     *
     * @since 1.0.0
     */
    public CorsBean setHeaders(String... headers) {
        this.headers = Arrays.asList(headers);
        return this;
    }

    /**
     * 获取请求方法
     *
     * @return 请求方法
     *
     * @since 1.0.0
     */
    public List<String> getMethods() {
        return methods;
    }

    /**
     * 设置请求方法
     *
     * @param methods 请求方法
     *
     * @return {@link CorsBean}
     *
     * @since 1.0.0
     */
    public CorsBean setMethods(String... methods) {
        this.methods = Arrays.asList(methods);
        return this;
    }

    /**
     * 获取证书
     *
     * @return 证书
     *
     * @since 1.0.0
     */
    public Boolean getCredential() {
        return credential;
    }

    /**
     * 设置证书
     *
     * @param credential 证书
     *
     * @return {@link CorsBean}
     *
     * @since 1.0.0
     */
    public CorsBean setCredential(Boolean credential) {
        this.credential = credential;
        return this;
    }
}
