package org.code4everything.boot.log;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.code4everything.boot.annotations.AopLog;
import org.code4everything.boot.bean.LogBean;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.service.LogService;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 切面日志工具类
 *
 * @author pantao
 * @since 2018/11/3
 */
public class AopLogUtils {

    /**
     * 日志缓存
     *
     * @since 1.0.1
     */
    private static Cache<String, Object> logCache =
            CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();

    private AopLogUtils() {}

    /**
     * 设置指定日志缓存
     *
     * @param logCache 日志缓存
     *
     * @since 1.0.1
     */
    @SuppressWarnings("unchecked")
    public static void setLogCache(Cache<String, ?> logCache) {
        AopLogUtils.logCache = (Cache<String, Object>) logCache;
    }

    /**
     * 保存日志（不抛出异常），适用于 {@link Around} 注解的方法
     *
     * @param service 日志服务 {@link LogService}
     * @param point 切点  {@link ProceedingJoinPoint}
     * @param <T> 日志表
     *
     * @return 日志信息
     *
     * @since 1.0.4
     */
    public static <T> T saveLogNoThrowable(LogService<T> service, ProceedingJoinPoint point) {
        return proceedAround(service, point).log;
    }

    /**
     * 保存日志，适用于 {@link Around} 注解的方法
     *
     * @param service 日志服务 {@link LogService}
     * @param point 切点  {@link ProceedingJoinPoint}
     * @param <T> 日志表
     *
     * @return 日志信息
     *
     * @throws Throwable 可能发生的异常
     * @since 1.0.4
     */
    public static <T> T saveLog(LogService<T> service, ProceedingJoinPoint point) throws Throwable {
        LogExBean<T> logExBean = proceedAround(service, point);
        if (Objects.isNull(logExBean.throwable)) {
            return logExBean.log;
        }
        throw logExBean.throwable;
    }

    /**
     * 保存日志信息，适用于非 {@link Around} 注解的方法
     *
     * @param service 日志服务 {@link LogService}
     * @param key 缓存键，确保每个请求键是唯一的
     * @param point 切点  {@link JoinPoint}
     * @param throwable 异常抛出 {@link Throwable}
     * @param <T> 日志表
     *
     * @return 日志信息
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T saveLog(LogService<T> service, String key, JoinPoint point, Throwable throwable) {
        T log;
        if (Objects.isNull(throwable)) {
            log = service.save(service.getLog(parse(point)));
            logCache.put(key, log);
            if (BootConfig.isDebug()) {
                Console.log(log);
            }
        } else {
            log = (T) logCache.asMap().get(key);
            if (ObjectUtil.isNull(log)) {
                log = service.getLog(parse(point));
            }
            log = service.saveException(log, throwable);
            if (BootConfig.isDebug()) {
                Console.error(throwable, log.toString());
            }
            logCache.asMap().remove(key);
        }
        return log;
    }

    /**
     * 解析切面信息，需要方法名注有注解 {@link AopLog}
     *
     * @param joinPoint 切面 {@link JoinPoint}
     *
     * @return {@link LogBean}
     *
     * @since 1.0.0
     */
    public static LogBean parse(JoinPoint joinPoint) {
        LogBean logBean = new LogBean().setArgs(JSONArray.toJSONString(joinPoint.getArgs()));
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 设置类名和方法名
        logBean.setClassName(targetClass.getName()).setMethodName(joinPoint.getSignature().getName());
        for (Method method : targetClass.getMethods()) {
            // 找到对应的方法名
            if (method.getName().equals(logBean.getMethodName()) && method.getParameterTypes().length == logBean.getArgs().length()) {
                AopLog aopLog = method.getAnnotation(AopLog.class);
                if (ObjectUtil.isNotNull(aopLog)) {
                    return logBean.setDescription(aopLog.value());
                }
            }
        }
        return logBean;
    }

    /**
     * 执行方法
     *
     * @param service 日志服务 {@link LogService}
     * @param point 切点  {@link ProceedingJoinPoint}
     * @param <T> 日志表
     *
     * @return {@link LogExBean}
     *
     * @since 1.0.4
     */
    private static <T> LogExBean<T> proceedAround(LogService<T> service, ProceedingJoinPoint point) {
        // 获取日志信息
        LogBean logBean = parse(point);
        Throwable t = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            point.proceed();
        } catch (Throwable e) {
            t = e;
        }
        logBean.setExecutedTime(System.currentTimeMillis() - beginTime);
        T log = service.getLog(logBean);
        return new LogExBean<>(Objects.isNull(t) ? service.save(log) : service.saveException(log, t), t);
    }
}

/**
 * 日志信息临时存储类
 *
 * @param <T> 日志表
 */
class LogExBean<T> {

    /**
     * 可能抛出的异常
     *
     * @since 1.0.4
     */
    Throwable throwable;

    /**
     * 日志
     *
     * @since 1.0.4
     */
    T log;

    /**
     * 设置内容
     *
     * @param log 日志
     * @param throwable 可能抛出的异常
     *
     * @since 1.0.4
     */
    LogExBean(T log, Throwable throwable) {
        this.log = log;
        this.throwable = throwable;
    }
}

