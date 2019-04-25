package org.code4everything.boot.web.mvc;

import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.bean.Response;
import org.code4everything.boot.constant.IntegerConsts;
import org.code4everything.boot.constant.MessageConsts;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 拦截处理器接口
 *
 * @author pantao
 * @since 2018/11/4
 */
public interface InterceptHandler {

    /**
     * 创建访问统计的生产队列，如果当前的队列大小超过了设置的队列边界时，将发生阻塞，所以请选择合适的队列并设置一个合适的大小
     *
     * @return {@link BlockingQueue}
     *
     * @since 1.1.0
     */
    default BlockingQueue<Runnable> createWorkQueue() {
        return new ArrayBlockingQueue<>(IntegerConsts.ONE_THOUSAND_AND_TWENTY_FOUR);
    }

    /**
     * 处理每日的访问统计
     *
     * @param date 日期
     * @param userVisit 用户访问统计
     * @param urlVisit URL访问统计
     * @param totalVisit 总访问次数
     *
     * @since 1.1.0
     */
    default void handleVisitLog(Date date, Map<String, Long> userVisit, Map<String, Long> urlVisit, long totalVisit) {}

    /**
     * 构建统计用户访问次数的键，返回NULL时不统计
     *
     * @param request {@link HttpServletRequest}
     *
     * @return 用户键
     *
     * @since 1.1.0
     */
    default String buildUserKey(HttpServletRequest request) {
        return buildCacheKey(request);
    }

    /**
     * 构建请求日志
     *
     * @param request {@link HttpServletRequest}
     *
     * @return 请求日志
     *
     * @since 1.1.0
     */
    default String buildVisitLog(HttpServletRequest request) {
        // 构建IP地址
        StringBuilder builder = new StringBuilder().append(request.getRemoteAddr()).append(" ");
        // 构建请求方法和URI
        builder.append(request.getMethod()).append(" ").append(request.getRequestURI());
        String queryString = request.getQueryString();
        if (StrUtil.isNotBlank(queryString)) {
            // 构建QueryString
            builder.append("?").append(queryString);
        }
        return builder.toString();
    }

    /**
     * 构建频率检测缓存的键，返回NULL时不检测请求频率
     *
     * @param request {@link HttpServletRequest}
     *
     * @return 缓存键
     *
     * @since 1.1.0
     */
    default String buildCacheKey(HttpServletRequest request) {
        return null;
    }

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
        response.getWriter().append(new Response(HttpStatus.FORBIDDEN.value(), MessageConsts.REQUEST_BANNED_ZH).toString());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }

    /**
     * 处理白名单
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
