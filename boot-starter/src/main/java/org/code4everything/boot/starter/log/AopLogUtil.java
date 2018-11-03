package org.code4everything.boot.starter.log;

import org.aspectj.lang.JoinPoint;
import org.code4everything.boot.starter.bean.LogBean;
import org.code4everything.boot.xtool.annotation.AopLog;

import java.lang.reflect.Method;

/**
 * 切面日志工具类
 *
 * @author pantao
 * @since 2018/11/3
 */
public class AopLogUtil {

    private AopLogUtil() {}

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
        LogBean logBean = new LogBean().setArgs(joinPoint.getArgs());
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 设置类名和方法名
        logBean.setClassName(targetClass.getName()).setMethodName(joinPoint.getSignature().getName());
        for (Method method : targetClass.getMethods()) {
            // 找到对应的方法名
            if (method.getName().equals(logBean.getMethodName())) {
                if (method.getParameterTypes().length == logBean.getArgs().length) {
                    return logBean.setDescription(method.getAnnotation(AopLog.class).value());
                }
            }
        }
        return logBean;
    }
}
