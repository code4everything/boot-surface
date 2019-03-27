package org.code4everything.boot.web.mvc;

import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.bean.ConfigBean;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.interfaces.InterceptHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 默认拦截器<br>拦截顺序依次为：黑名单 - 白名单 - 拦截名单
 *
 * @author pantao
 * @since 2018/11/4
 */
public final class DefaultWebInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebInterceptor.class);

    /**
     * 配置信息
     *
     * @since 1.0.0
     */
    private static ConfigBean configBean;

    /**
     * 拦截处理器
     *
     * @since 1.0.0
     */
    private InterceptHandler interceptHandler;

    /**
     * 构造函数
     *
     * @since 1.0.0
     */
    public DefaultWebInterceptor() {
        this.interceptHandler = new InterceptHandler() {};
    }

    /**
     * 构造函数
     *
     * @param interceptHandler 拦截处理器
     *
     * @since 1.0.0
     */
    public DefaultWebInterceptor(InterceptHandler interceptHandler) {
        this.interceptHandler = interceptHandler;
    }

    /**
     * 设置配置类
     *
     * @param configBean {@link ConfigBean}
     *
     * @since 1.0.0
     */
    public static void setConfigBean(ConfigBean configBean) {
        DefaultWebInterceptor.configBean = configBean;
    }

    /**
     * 默认拦截器
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler {@link Object}
     *
     * @return {@link Boolean}
     *
     * @since 1.0.0
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Objects.requireNonNull(DefaultWebInterceptor.configBean);
        String url = request.getServletPath();
        if (BootConfig.isDebug()) {
            LOGGER.info("{} [{}] with params >>> {}", request.getMethod(), url, request.getParameterMap());
        }
        // 黑名单
        if (StrUtil.startWithAny(url, DefaultWebInterceptor.configBean.getBlackPrefixes())) {
            if (BootConfig.isDebug()) {
                LOGGER.info("url -> {}, in black list", url);
            }
            interceptHandler.handleBlackList(request, response, handler);
            return false;
        }
        // 白名单
        if (StrUtil.startWithAny(url, DefaultWebInterceptor.configBean.getWhitePrefixes())) {
            if (BootConfig.isDebug()) {
                LOGGER.info("url -> {}, in white list", url);
            }
            interceptHandler.handleWhiteList(request, response, handler);
            return true;
        }
        // 拦截名单
        if (StrUtil.startWithAny(url, DefaultWebInterceptor.configBean.getInterceptPrefixes())) {
            if (BootConfig.isDebug()) {
                LOGGER.info("url -> {}, in intercept list", url);
            }
            return interceptHandler.handleInterceptList(request, response, handler);
        }
        return true;
    }

    /**
     * 拦截之后的处理
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler {@link Object}
     * @param modelAndView 数据对象
     *
     * @throws Exception 异常
     * @since 1.0.2
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        interceptHandler.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 渲染了响应数据之后的处理
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler {@link Object}
     * @param ex 抛出的异常
     *
     * @throws Exception 异常
     * @since 1.0.2
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        interceptHandler.afterCompletion(request, response, handler, ex);
    }
}
