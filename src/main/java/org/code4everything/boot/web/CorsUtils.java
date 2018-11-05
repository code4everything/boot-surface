package org.code4everything.boot.web;

import org.code4everything.boot.bean.CorsBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域过滤器工具类
 *
 * @author pantao
 * @since 2018/10/30
 **/
public class CorsUtils {

    private CorsUtils() {}

    /**
     * 新建过滤器，默认允许所有请求和路径
     *
     * @return {@link CorsFilter}
     *
     * @since 1.0.0
     */
    public static CorsFilter newCorsFilter() {
        return newCorsFilter("/**");
    }

    /**
     * 新建过滤器，默认允许所有请求和路径
     *
     * @param path 路径模式
     *
     * @return {@link CorsFilter}
     *
     * @since 1.0.0
     */
    public static CorsFilter newCorsFilter(String path) {
        return newCorsFilter(new CorsBean().setPath(path).setCredential(true).setHeaders("*").setMethods("*").setOrigins("*"));
    }

    /**
     * 新建过滤器，默认允许所有请求和路径
     *
     * @param corsBean 过滤器配置
     *
     * @return {@link CorsFilter}
     *
     * @since 1.0.0
     */
    public static CorsFilter newCorsFilter(CorsBean corsBean) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(corsBean.getOrigins());
        corsConfiguration.setAllowedHeaders(corsBean.getHeaders());
        corsConfiguration.setAllowedMethods(corsBean.getMethods());
        corsConfiguration.setAllowCredentials(corsBean.getCredential());
        source.registerCorsConfiguration(corsBean.getPath(), corsConfiguration);
        return new CorsFilter(source);
    }
}
