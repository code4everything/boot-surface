package org.code4everything.boot.starter.interfaces;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpStatus;
import org.code4everything.boot.xtool.bean.ResponseResult;
import org.springframework.http.HttpHeaders;

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
        response.getWriter().append(new ResponseResult(HttpStatus.HTTP_FORBIDDEN, "禁止访问").toString());
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
}
