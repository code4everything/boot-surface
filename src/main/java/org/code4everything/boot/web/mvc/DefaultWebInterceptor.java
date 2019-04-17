package org.code4everything.boot.web.mvc;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.base.DateUtils;
import org.code4everything.boot.base.ObjectUtils;
import org.code4everything.boot.bean.ConfigBean;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.exception.ExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 默认拦截器<br>拦截顺序依次为：黑名单 - 白名单 - 拦截名单
 *
 * @author pantao
 * @since 2018/11/4
 */
public final class DefaultWebInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebInterceptor.class);

    /**
     * 默认请求检测的频率，单位：毫秒
     *
     * @since 1.1.0
     */
    private static int frequency = 1000;

    private static boolean firstLoad = true;

    private volatile static boolean visitLog = false;

    private static Map<String, Long> userVisitMap;

    private static int userVisitMapCapacity = 1024;

    /**
     * 配置信息
     *
     * @since 1.0.0
     */
    private static ConfigBean configBean;

    private static Map<String, Long> urlVisitMap;

    private static int urlVisitMapCapacity = 128;

    private static long totalVisit;

    private static long firstVisit = 0;

    private static Cache<String, Byte> cache = null;

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
     * 设置请求检测的频率，单位：毫秒
     *
     * @param frequency 频率
     *
     * @since 1.1.0
     */
    public static void setFrequency(Integer frequency) {
        if (ObjectUtil.isNotNull(frequency)) {
            DefaultWebInterceptor.frequency = frequency;
        }
    }

    /**
     * 获取用户访问统计
     *
     * @return 用户访问统计
     *
     * @since 1.1.0
     */
    public static Map<String, Long> getUserVisitMap() {
        return userVisitMap;
    }

    /**
     * 获取URL访问统计
     *
     * @return URL访问统计
     *
     * @since 1.1.0
     */
    public static Map<String, Long> getUrlVisitMap() {
        return urlVisitMap;
    }

    /**
     * 获取总访问次数
     *
     * @return 总访问次数
     *
     * @since 1.1.0
     */
    public static long getTotalVisit() {
        return totalVisit;
    }

    /**
     * 获取总访问次数，并且清空
     *
     * @return 总访问次数
     *
     * @since 1.1.0
     */
    public static long getTotalVisitAndClear() {
        long tmp = totalVisit;
        totalVisit = 0;
        return tmp;
    }

    /**
     * 设置是否统计访问数据
     *
     * @param visitLog 是否统计访问数据
     *
     * @since 1.1.0
     */
    public static void setVisitLog(Boolean visitLog) {
        if (ObjectUtil.isNotNull(visitLog)) {
            // 初始化
            if (visitLog) {
                resetVisitObjects();
            } else {
                userVisitMap = null;
                urlVisitMap = null;
                totalVisit = 0;
            }
            DefaultWebInterceptor.visitLog = visitLog;
        }
    }

    /**
     * 初始化请求统计的对象
     */
    private static void resetVisitObjects() {
        userVisitMap = new HashMap<>(userVisitMapCapacity);
        urlVisitMap = new HashMap<>(urlVisitMapCapacity);
        totalVisit = 0;
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
        checkFrequency(request);
        // 统计请求数据
        if (visitLog && ObjectUtils.isNotNull(userVisitMap, urlVisitMap)) {
            if (firstVisit != 0 && firstVisit < DateUtils.getStartOfToday().getTime()) {
                // 重置容器大小
                userVisitMapCapacity = userVisitMap.size();
                urlVisitMapCapacity = urlVisitMap.size();
                // 回调处理每日的访问统计
                interceptHandler.handleVisitLog(new Date(firstVisit), userVisitMap, urlVisitMap, totalVisit);
                // 重置统计数据
                resetVisitObjects();
                firstVisit = System.currentTimeMillis();
            }
            // 统计用户访问次数
            String userKey = interceptHandler.buildUserKey(request);
            if (StrUtil.isNotEmpty(userKey)) {
                userVisitMap.put(userKey, userVisitMap.getOrDefault(userKey, 0L) + 1);
            }
            // 统计URL访问次数
            String urlKey = request.getRequestURI();
            urlVisitMap.put(urlKey, urlVisitMap.getOrDefault(urlKey, 0L) + 1);
            // 统计总访问次数
            totalVisit++;
        }

        if (BootConfig.isDebug()) {
            // 打印请求的详细信息
            String logStr = interceptHandler.buildVisitLog(request);
            LOGGER.info(logStr);
        }
        if (Objects.isNull(configBean)) {
            if (firstLoad) {
                LOGGER.warn("has no config for this interceptor, it will does not work in anyway.");
                firstLoad = false;
            }
            return true;
        }

        String url = request.getRequestURI();

        // 黑名单
        if (StrUtil.startWithAny(url, DefaultWebInterceptor.configBean.getBlackPrefixes())) {
            interceptHandler.handleBlackList(request, response, handler);
            return false;
        }
        // 白名单
        if (StrUtil.startWithAny(url, DefaultWebInterceptor.configBean.getWhitePrefixes())) {
            interceptHandler.handleWhiteList(request, response, handler);
            return true;
        }
        // 拦截名单
        if (StrUtil.startWithAny(url, DefaultWebInterceptor.configBean.getInterceptPrefixes())) {
            return interceptHandler.handleInterceptList(request, response, handler);
        }
        return true;
    }

    /**
     * 检测请求频率
     *
     * @param request {@link HttpServletRequest}
     *
     * @since 1.1.0
     */
    private void checkFrequency(HttpServletRequest request) {
        String key = interceptHandler.buildCacheKey(request);
        if (StrUtil.isNotEmpty(key) && Objects.isNull(cache)) {
            // 创建频率检测缓存
            synchronized (DefaultExceptionHandler.class) {
                if (Objects.isNull(cache)) {
                    cache = CacheBuilder.newBuilder().expireAfterWrite(frequency, TimeUnit.MILLISECONDS).build();
                }
            }
        }
        if (StrUtil.isNotEmpty(key) && ObjectUtil.isNotNull(cache)) {
            // 频率检测
            if (cache.asMap().containsKey(key)) {
                throw ExceptionFactory.requestFrequently();
            } else {
                cache.put(key, Byte.MAX_VALUE);
            }
        }
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
