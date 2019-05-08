package org.code4everything.boot.web.mvc;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.base.DateUtils;
import org.code4everything.boot.base.MapUtils;
import org.code4everything.boot.base.ObjectUtils;
import org.code4everything.boot.base.constant.IntegerConsts;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.web.mvc.exception.ExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

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

    private static boolean visitLog = false;

    /**
     * 配置信息
     *
     * @since 1.0.0
     */
    private static FilterPath filterPath = null;

    private static Map<String, Long> userVisitMap = null;

    private static Map<String, Long> urlVisitMap = null;

    private static long totalVisit = 0;

    /**
     * 拦截处理器
     *
     * @since 1.0.0
     */
    private final PathFilterHandler filterHandler;

    private final ThreadFactory factory = ThreadFactoryBuilder.create().setDaemon(true).build();

    private ThreadPoolExecutor executor = null;

    private Cache<String, Byte> cache = null;

    private ScheduledThreadPoolExecutor scheduledExecutor = null;

    /**
     * 构造函数
     *
     * @since 1.0.0
     */
    public DefaultWebInterceptor() {
        this(new PathFilterHandler() {});
    }

    /**
     * 构造函数
     *
     * @param filterHandler 拦截处理器
     *
     * @since 1.0.0
     */
    public DefaultWebInterceptor(PathFilterHandler filterHandler) {
        this.filterHandler = filterHandler;
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
     * @param clear 是否清除已有访问日志
     *
     * @return 用户访问统计
     *
     * @since 1.1.0
     */
    public static Map<String, Long> getUserVisitMap(boolean clear) {
        return visitMapHelper(userVisitMap, clear);
    }

    /**
     * 获取URL访问统计
     *
     * @param clear 是否清除已有访问日志
     *
     * @return URL访问统计
     *
     * @since 1.1.0
     */
    public static Map<String, Long> getUrlVisitMap(boolean clear) {
        return visitMapHelper(urlVisitMap, clear);
    }

    /**
     * 操作访问日志
     *
     * @param map 集合
     * @param clear 是否清除
     *
     * @return 访问日志
     *
     * @since 1.1.2
     */
    private static Map<String, Long> visitMapHelper(Map<String, Long> map, boolean clear) {
        if (Objects.isNull(map)) {
            return null;
        }
        Map<String, Long> tmp = MapUtils.sortByValue(map, Comparator.reverseOrder());
        if (clear) {
            map.clear();
        }
        return tmp;
    }

    /**
     * 获取总访问次数
     *
     * @param clear 是否清除已有访问日志
     *
     * @return 总访问次数
     *
     * @since 1.1.0
     */
    public static long getTotalVisit(boolean clear) {
        long tmp = totalVisit;
        if (clear) {
            totalVisit = 0;
        }
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
        if (Objects.isNull(visitLog)) {
            return;
        }
        if (visitLog) {
            // 第一次初始化
            userVisitMap = new HashMap<>(1024);
            urlVisitMap = new HashMap<>(128);
            totalVisit = 0;
        } else {
            userVisitMap = null;
            urlVisitMap = null;
            totalVisit = 0;
        }
        DefaultWebInterceptor.visitLog = visitLog;
    }

    /**
     * 设置配置类
     *
     * @param filterPath {@link FilterPath}
     *
     * @since 1.0.0
     */
    public static void setFilterPath(FilterPath filterPath) {
        DefaultWebInterceptor.filterPath = filterPath;
    }

    private static void incrementVisit() {
        DefaultWebInterceptor.totalVisit++;
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
            countVisit(filterHandler.buildUserKey(request), request.getRequestURI());
        }

        if (BootConfig.isDebug()) {
            // 打印请求的详细信息
            String logStr = filterHandler.buildVisitLog(request);
            LOGGER.info(logStr);
        }

        String url = request.getRequestURI();

        // 黑名单
        if (StrUtil.startWithAny(url, getBlackList())) {
            filterHandler.handleBlackList(request, response, handler);
            return false;
        }
        // 白名单
        if (StrUtil.startWithAny(url, getWhiteList())) {
            filterHandler.handleWhiteList(request, response, handler);
            return true;
        }
        // 拦截名单
        if (StrUtil.startWithAny(url, getInterceptList())) {
            return filterHandler.handleInterceptList(request, response, handler);
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
        String key = filterHandler.buildCacheKey(request);
        if (StrUtil.isNotEmpty(key) && Objects.isNull(cache)) {
            // 创建频率检测缓存
            synchronized (DefaultExceptionHandler.class) {
                if (Objects.isNull(cache)) {
                    cache = CacheBuilder.newBuilder().expireAfterWrite(frequency, TimeUnit.MILLISECONDS).build();
                }
            }
        }
        if (StrUtil.isNotEmpty(key) && ObjectUtil.isNotNull(cache) && frequency > 0) {
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
        filterHandler.postHandle(request, response, handler, modelAndView);
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
        filterHandler.afterCompletion(request, response, handler, ex);
    }

    private void countVisit(final String userKey, final String urlKey) {
        if (Objects.isNull(executor) || Objects.isNull(scheduledExecutor)) {
            // 初始化线程池
            synchronized (DefaultWebInterceptor.class) {
                if (Objects.isNull(executor)) {
                    // 初始化统计线程
                    BlockingQueue<Runnable> queue = filterHandler.createWorkQueue();
                    executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, queue, factory);
                }
                if (Objects.isNull(scheduledExecutor)) {
                    // 初始化回调线程
                    scheduledExecutor = new ScheduledThreadPoolExecutor(1, factory);
                    long initialDelay = DateUtils.getEndOfToday().getTime() - System.currentTimeMillis() - 999;
                    scheduledExecutor.scheduleAtFixedRate(() -> {
                        // 回调处理每日的访问统计
                        Date date = new Date(System.currentTimeMillis());
                        if (BootConfig.isDebug()) {
                            LOGGER.info("call method 'handleVisitLog' to save and reset today's http request data");
                        }
                        Map<String, Long> userMap = getUserVisitMap(false);
                        Map<String, Long> urlMap = getUrlVisitMap(false);
                        boolean saved = filterHandler.handleVisitLog(date, userMap, urlMap, totalVisit);
                        if (saved) {
                            userVisitMap.clear();
                            urlVisitMap.clear();
                        }
                    }, initialDelay, IntegerConsts.ONE_DAY_MILLIS, TimeUnit.MILLISECONDS);
                }
            }
        }
        // 使用单独一个线程来进行统计
        executor.execute(() -> {
            if (StrUtil.startWithAny(urlKey, getVisitIgnorePrefixes())) {
                return;
            }
            // 统计用户访问次数
            if (StrUtil.isNotEmpty(userKey)) {
                userVisitMap.put(userKey, userVisitMap.getOrDefault(userKey, 0L) + 1);
            }
            // 统计URL访问次数
            urlVisitMap.put(urlKey, urlVisitMap.getOrDefault(urlKey, 0L) + 1);
            // 统计总访问次数
            incrementVisit();
        });
    }

    private String[] getVisitIgnorePrefixes() {
        return isNull() ? null : DefaultWebInterceptor.filterPath.getVisitIgnorePrefixes();
    }

    private String[] getBlackList() {
        return isNull() ? null : DefaultWebInterceptor.filterPath.getBlackPrefixes();
    }

    private String[] getWhiteList() {
        return isNull() ? null : DefaultWebInterceptor.filterPath.getWhitePrefixes();
    }

    private String[] getInterceptList() {
        return isNull() ? null : DefaultWebInterceptor.filterPath.getInterceptPrefixes();
    }

    private boolean isNull() {
        return Objects.isNull(DefaultWebInterceptor.filterPath);
    }
}
