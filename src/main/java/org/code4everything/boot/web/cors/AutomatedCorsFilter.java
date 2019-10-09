package org.code4everything.boot.web.cors;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.lang.Nullable;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author pantao
 * @since 2019/10/9
 */
public final class AutomatedCorsFilter extends CorsFilter {

    private final CorsConfigurationSource configSource;

    private final CorsProcessor processor = new DefaultCorsProcessor();

    /**
     * 预检命令缓存时长，单位秒
     */
    private final Long maxAge;

    public AutomatedCorsFilter(@Nullable Long maxAge) {
        super(new UrlBasedCorsConfigurationSource());
        this.configSource = null;
        this.maxAge = maxAge;
    }

    public AutomatedCorsFilter(CorsConfigurationSource configSource, @Nullable Long maxAge) {
        super(configSource);
        this.configSource = configSource;
        this.maxAge = maxAge;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        boolean invalid = false;
        // Spring跨域原生处理
        if (CorsUtils.isCorsRequest(request)) {
            if (ObjectUtil.isNull(configSource)) {
                invalid = true;
            } else {
                CorsConfiguration corsConfiguration = this.configSource.getCorsConfiguration(request);
                if (corsConfiguration != null) {
                    boolean isValid = this.processor.processRequest(corsConfiguration, request, response);
                    if (!isValid || CorsUtils.isPreFlightRequest(request)) {
                        invalid = true;
                    }
                }
            }
        }
        if (invalid) {
            // 如果跨域原生处理没有通过，自动设置跨域
            addCorsHeaders(request, response);
        }
        filterChain.doFilter(request, response);
    }

    private void addCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        // 设置请求源
        String origin = request.getHeader("Origin");
        if (StrUtil.isNotEmpty(origin)) {
            response.addHeader("Access-Control-Allow-Origin", origin);
        }

        // 设置请求头
        String headers = request.getHeader("Access-Control-Request-Headers");
        if (StrUtil.isNotEmpty(headers)) {
            response.addHeader("Access-Control-Allow-Headers", headers);
        }

        // 设置请求方法，凭证，预检命令缓存时长
        response.addHeader("Access-Control-Allow-Methods", request.getMethod());
        response.addHeader("Access-Control-Allow-Credentials", "true");
        if (ObjectUtil.isNotNull(maxAge)) {
            // 预检命令缓存时长
            response.addHeader("Access-Control-Max-Age", maxAge.toString());
        }
    }
}
