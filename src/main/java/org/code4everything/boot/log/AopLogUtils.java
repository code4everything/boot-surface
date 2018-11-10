package org.code4everything.boot.log;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.google.common.cache.Cache;
import org.aspectj.lang.JoinPoint;
import org.code4everything.boot.annotations.AopLog;
import org.code4everything.boot.bean.LogBean;
import org.code4everything.boot.service.LogService;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 切面日志工具类
 *
 * @author pantao
 * @since 2018/11/3
 */
public class AopLogUtils {

    /**
     * 调试
     *
     * @since 1.0.0
     */
    private static boolean debug = false;

    private AopLogUtils() {}

    /**
     * 是否调试
     *
     * @return 是否调试
     *
     * @since 1.0.0
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * 设置是否调试
     *
     * @param debug 是否调试
     *
     * @since 1.0.0
     */
    public static void setDebug(boolean debug) {
        AopLogUtils.debug = debug;
    }

    /**
     * 保存日志信息
     *
     * @param service 日志服务 {@link LogService}
     * @param cache 日志缓存 {@link Cache}
     * @param key 缓存键，确保每个请求键是唯一的
     * @param point 切点  {@link JoinPoint}
     * @param throwable 异常抛出 {@link Throwable}
     * @param <T> 日志表
     *
     * @return 日志信息
     *
     * @since 1.0.0
     */
    public static <T> T saveLog(LogService<T> service, Cache<String, T> cache, String key, JoinPoint point,
                                Throwable throwable) {
        T log;
        if (Objects.isNull(throwable)) {
            log = service.save(service.getLog(parse(point)));
            cache.put(key, log);
            if (AopLogUtils.isDebug()) {
                Console.log(log);
            }
        } else {
            log = cache.asMap().get(key);
            if (ObjectUtil.isNotNull(log)) {
                log = service.getLog(parse(point));
                cache.asMap().remove(key);
            }
            log = service.saveException(log, throwable);
            if (AopLogUtils.isDebug()) {
                Console.error(throwable, log.toString());
            }
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
                return logBean.setDescription(method.getAnnotation(AopLog.class).value());
            }
        }
        return logBean;
    }
}
