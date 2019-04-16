package org.code4everything.boot.config;

import cn.hutool.core.util.ObjectUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.code4everything.boot.bean.LogTempBean;
import org.code4everything.boot.log.AopLogUtils;
import org.code4everything.boot.service.BootLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @author pantao
 * @since 2019-04-16
 */
@Aspect
@Configuration
@ConditionalOnClass(BootLogService.class)
public class BootLogConfiguration {

    private final BootLogService<?> logService;

    @Autowired
    public BootLogConfiguration(BootLogService<?> logService) {
        this.logService = logService;
    }

    @Around("@annotation(org.code4everything.boot.annotation.AopLog)")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        LogTempBean<?> bean = AopLogUtils.saveLog(logService, point);
        if (ObjectUtil.isNotNull(bean.getThrowable())) {
            throw bean.getThrowable();
        }
        return bean.getResult();
    }
}
