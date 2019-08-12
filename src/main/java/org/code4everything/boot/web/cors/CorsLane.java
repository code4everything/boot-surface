package org.code4everything.boot.web.cors;

import org.code4everything.boot.base.bean.BaseBean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 跨域配置
 *
 * @author pantao
 * @since 2018/10/30
 */
public class CorsLane implements BaseBean, Serializable {

    private static final long serialVersionUID = -8878074589278671551L;

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
     * 创建 {@link CorsLane}
     *
     * @return {@link CorsLane}
     *
     * @since 1.1.1
     */
    public static CorsLane create() {
        return new CorsLane();
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
     * @return {@link CorsLane}
     *
     * @since 1.0.0
     */
    public CorsLane setPath(String path) {
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
     * @return {@link CorsLane}
     *
     * @since 1.0.0
     */
    public CorsLane setOrigins(List<String> origins) {
        this.origins = origins;
        return this;
    }

    /**
     * 设置请求源
     *
     * @param origins 请求源
     *
     * @return {@link CorsLane}
     *
     * @since 1.0.0
     */
    public CorsLane setOrigins(String... origins) {
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
     * @return {@link CorsLane}
     *
     * @since 1.0.0
     */
    public CorsLane setHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 设置请求头
     *
     * @param headers 请求头
     *
     * @return {@link CorsLane}
     *
     * @since 1.0.0
     */
    public CorsLane setHeaders(String... headers) {
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
     * @param methods 方法
     *
     * @return {@link CorsLane}
     *
     * @since 1.0.0
     */
    public CorsLane setMethods(List<String> methods) {
        this.methods = methods;
        return this;
    }

    /**
     * 设置请求方法
     *
     * @param methods 请求方法
     *
     * @return {@link CorsLane}
     *
     * @since 1.0.0
     */
    public CorsLane setMethods(String... methods) {
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
     * @return {@link CorsLane}
     *
     * @since 1.0.0
     */
    public CorsLane setCredential(Boolean credential) {
        this.credential = credential;
        return this;
    }

    @Override
    public String toString() {
        return "CorsLane{" + "path='" + path + '\'' + ", origins=" + origins + ", headers=" + headers + ", methods=" + methods + ", credential=" + credential + '}';
    }
}
