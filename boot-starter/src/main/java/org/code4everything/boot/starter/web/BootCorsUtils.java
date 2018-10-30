package org.code4everything.boot.starter.web;

import org.code4everything.boot.starter.bean.CorsBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author pantao
 * @since 2018/10/30
 **/
public class BootCorsUtils {

    private BootCorsUtils() {}

    public static CorsFilter newCorsFilter() {
        return newCorsFilter("/**");
    }

    public static CorsFilter newCorsFilter(String path) {
        return newCorsFilter(new CorsBean().setPath(path).setCredential(true).setHeaders("*").setMethods("*").setOrigins("*"));
    }

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
