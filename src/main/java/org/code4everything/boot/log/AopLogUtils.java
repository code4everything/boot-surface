package org.code4everything.boot.log;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.service.BootLogService;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 切面日志工具类
 *
 * @author pantao
 * @since 2018/11/3
 */
public final class AopLogUtils {

    /**
     * 日志缓存
     *
     * @since 1.0.1
     */
    private static Cache<String, Object> logCache = null;

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
     * @param service 日志服务 {@link BootLogService}
     * @param point 切点  {@link ProceedingJoinPoint}
     * @param <T> 日志表
     *
     * @return 日志信息
     *
     * @since 1.0.4
     */
    public static <T> ReturnedLog<T> saveLog(BootLogService<T> service, ProceedingJoinPoint point) {
        return saveLog(service, point, true);
    }

    /**
     * 保存日志（不抛出异常），适用于 {@link Around} 注解的方法
     *
     * @param service 日志服务 {@link BootLogService}
     * @param point 切点  {@link ProceedingJoinPoint}
     * @param shouldSave 是否保存日志
     * @param <T> 日志表
     *
     * @return 日志信息
     *
     * @since 1.0.4
     */
    public static <T> ReturnedLog<T> saveLog(BootLogService<T> service, ProceedingJoinPoint point, boolean shouldSave) {
        return doAround(service, point, shouldSave);
    }

    /**
     * 保存日志，适用于 {@link Around} 注解的方法
     *
     * @param service 日志服务 {@link BootLogService}
     * @param point 切点  {@link ProceedingJoinPoint}
     * @param <T> 日志表
     *
     * @return 日志信息
     *
     * @throws Throwable 可能发生的异常
     * @since 1.0.4
     */
    public static <T> ReturnedLog<T> saveLogWithThrowable(BootLogService<T> service, ProceedingJoinPoint point) throws Throwable {
        return saveLogWithThrowable(service, point, true);
    }

    /**
     * 保存日志，适用于 {@link Around} 注解的方法
     *
     * @param service 日志服务 {@link BootLogService}
     * @param point 切点  {@link ProceedingJoinPoint}
     * @param shouldSave 是否保存日志
     * @param <T> 日志表
     *
     * @return 日志信息
     *
     * @throws Throwable 可能发生的异常
     * @since 1.0.4
     */
    public static <T> ReturnedLog<T> saveLogWithThrowable(BootLogService<T> service, ProceedingJoinPoint point,
                                                          boolean shouldSave) throws Throwable {
        ReturnedLog<T> returnedLog = doAround(service, point, shouldSave);
        if (Objects.isNull(returnedLog.getThrowable())) {
            return returnedLog;
        }
        throw returnedLog.getThrowable();
    }

    /**
     * 保存日志信息，适用于非 {@link Around} 注解的方法
     *
     * @param service 日志服务 {@link BootLogService}
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
    public static <T> T saveLog(BootLogService<T> service, String key, JoinPoint point, Throwable throwable) {
        if (Objects.isNull(logCache)) {
            synchronized (AopLogUtils.class) {
                if (Objects.isNull(logCache)) {
                    logCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();
                }
            }
        }
        T log;
        if (Objects.isNull(throwable)) {
            // 从切点获取日志基本信息，并进行保存
            log = service.getLog(parse((point)));
            if (ObjectUtil.isNotNull(log)) {
                service.save(log);
                // 当入缓存，当发生异常时，方便更新异常信息
                logCache.put(key, log);
                if (BootConfig.isDebug()) {
                    Console.log(log);
                }
            }
        } else {
            log = (T) logCache.asMap().get(key);
            if (ObjectUtil.isNull(log)) {
                // 如果缓存中没有，则直接创建一条新的日志
                log = service.getLog(parse(point));
            }
            if (ObjectUtil.isNotNull(log)) {
                // 增补异常信息
                log = service.saveException(log, throwable);
                if (BootConfig.isDebug()) {
                    Console.error(throwable, log.toString());
                }
            }
            // 移除日志缓存
            logCache.invalidate(key);
        }
        return log;
    }

    /**
     * 解析切面信息，需要方法名注有注解 {@link LogMethod}
     *
     * @param joinPoint 切面 {@link JoinPoint}
     *
     * @return {@link MethodLog}
     *
     * @since 1.0.0
     */
    public static MethodLog parse(JoinPoint joinPoint) {
        MethodLog log = new MethodLog().setArgs(JSONArray.toJSONString(joinPoint.getArgs()));
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 设置类名和方法名
        log.setClassName(targetClass.getName()).setMethodName(joinPoint.getSignature().getName());
        for (Method method : targetClass.getMethods()) {
            // 找到对应的方法名（目前只判断了方法名和参数个数是否一致）
            if (method.getName().equals(log.getMethodName()) && method.getParameterTypes().length == joinPoint.getArgs().length) {
                LogMethod aopLog = method.getAnnotation(LogMethod.class);
                if (ObjectUtil.isNotNull(aopLog)) {
                    // 设置描述信息
                    return log.setDescription(aopLog.value());
                }
            }
        }
        return log;
    }

    /**
     * 执行方法
     *
     * @param service 日志服务 {@link BootLogService}
     * @param point 切点  {@link ProceedingJoinPoint}
     * @param saveLog 是否保存日志
     * @param <T> 日志表
     *
     * @return {@link ReturnedLog}
     *
     * @since 1.0.4
     */
    private static <T> ReturnedLog<T> doAround(BootLogService<T> service, ProceedingJoinPoint point, boolean saveLog) {
        // 获取日志信息
        MethodLog methodLog = parse(point);
        Throwable t = null;
        long beginTime = System.currentTimeMillis();
        Object result = null;
        try {
            // 执行方法
            result = point.proceed();
        } catch (Throwable e) {
            t = e;
        }
        methodLog.setExecutedTime(System.currentTimeMillis() - beginTime);
        T log = service.getLog(methodLog);
        if (saveLog && ObjectUtil.isNotNull(log)) {
            // 保存日志
            if (Objects.isNull(t)) {
                service.save(log);
            } else {
                service.saveException(log, t);
            }
        }
        return new ReturnedLog<>(log, t, result);
    }
}

