package org.code4everything.boot.starter.bean;

import java.util.Arrays;
import java.util.List;

/**
 * 跨域配置
 *
 * @author pantao
 * @since 2018/10/30
 **/
public class CorsBean {

    private String path;

    private List<String> origins;

    private List<String> headers;

    private List<String> methods;

    private Boolean credential;

    /**
     * 设置请求源
     *
     * @param origins 请求源
     *
     * @return {@link CorsBean}
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
     */
    public CorsBean setMethods(List<String> methods) {
        this.methods = methods;
        return this;
    }

    /**
     * 获取路径模式
     *
     * @return 路径模式
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
     */
    public CorsBean setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * 获取请求源
     *
     * @return 请求源
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
     */
    public CorsBean setOrigins(String... origins) {
        this.origins = Arrays.asList(origins);
        return this;
    }

    /**
     * 获取请求头
     *
     * @return 请求头
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
     */
    public CorsBean setHeaders(String... headers) {
        this.headers = Arrays.asList(headers);
        return this;
    }

    /**
     * 获取请求方法
     *
     * @return 请求方法
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
     */
    public CorsBean setMethods(String... methods) {
        this.methods = Arrays.asList(methods);
        return this;
    }

    /**
     * 获取证书
     *
     * @return 证书
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
     */
    public CorsBean setCredential(Boolean credential) {
        this.credential = credential;
        return this;
    }
}
