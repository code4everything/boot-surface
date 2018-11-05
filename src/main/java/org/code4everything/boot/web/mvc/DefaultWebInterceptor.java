package org.code4everything.boot.web.mvc;

import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.bean.ConfigBean;
import org.code4everything.boot.interfaces.InterceptHandler;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认拦截器<br/>拦截顺序：黑名单 -> 白名单 -> 拦截名单
 *
 * @author pantao
 * @since 2018/11/4
 */
public class DefaultWebInterceptor implements HandlerInterceptor {

    /**
     * 配置信息
     *
     * @since 1.0.0
     */
    private ConfigBean configBean;

    /**
     * 拦截处理器
     *
     * @since 1.0.0
     */
    private InterceptHandler interceptHandler;

    /**
     * 构造函数
     *
     * @param configBean 配置信息
     *
     * @since 1.0.0
     */
    public DefaultWebInterceptor(ConfigBean configBean) {
        this.configBean = configBean;
        this.interceptHandler = new InterceptHandler() {};
    }

    /**
     * 构造函数
     *
     * @param configBean 配置信息
     * @param interceptHandler 拦截处理器
     *
     * @since 1.0.0
     */
    public DefaultWebInterceptor(ConfigBean configBean, InterceptHandler interceptHandler) {
        this.configBean = configBean;
        this.interceptHandler = interceptHandler;
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
        String url = request.getServletPath();
        // 黑名单
        if (StrUtil.startWithAny(url, configBean.getBlackPrefixes())) {
            interceptHandler.handleBlackList(request, response, handler);
            return false;
        }
        // 白名单
        if (StrUtil.startWithAny(url, configBean.getWhitePrefixes())) {
            interceptHandler.handleWhiteList(request, response, handler);
            return true;
        }
        // 拦截名单
        if (StrUtil.startWithAny(url, configBean.getInterceptPrefixes())) {
            return interceptHandler.handleInterceptList(request, response, handler);
        }
        return true;
    }
}
