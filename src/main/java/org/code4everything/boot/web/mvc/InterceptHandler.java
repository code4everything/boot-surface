package org.code4everything.boot.web.mvc;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpStatus;
import org.code4everything.boot.bean.Response;
import org.code4everything.boot.constant.MessageConsts;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截处理器接口
 *
 * @author pantao
 * @since 2018/11/4
 */
public interface InterceptHandler {

    /**
     * 处理黑名单
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler {@link Object}
     *
     * @throws Exception 异常
     * @since 1.0.0
     */
    default void handleBlackList(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.getWriter().append(new Response(HttpStatus.HTTP_FORBIDDEN, MessageConsts.REQUEST_BANNED_ZH).toString());
        response.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.JSON.toString());
        response.setStatus(HttpStatus.HTTP_FORBIDDEN);
    }

    /**
     * 处理黑白名单
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler {@link Object}
     *
     * @throws Exception 异常
     * @since 1.0.0
     */
    default void handleWhiteList(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {}

    /**
     * 处理拦截名单
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler {@link Object}
     *
     * @return {@link Boolean}
     *
     * @throws Exception 异常
     * @since 1.0.0
     */
    default boolean handleInterceptList(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {return true;}

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
    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                            @Nullable ModelAndView modelAndView) throws Exception {}

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
    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 @Nullable Exception ex) throws Exception {}
}
