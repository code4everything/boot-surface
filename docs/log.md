## 简化日志记录

> 任何项目的开发都少不了日志的记录，如果你也像我一样是用的Spring AOP进行的日志记录，并且需要将日志保存至数据库，那么这个类可以帮到你



#### 新建一个日志实体类

``` java
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class Log implements Serializable {

    @Id
    private String id;

    private String description;

    private String ip;

    private String className;

    private String methodName;

    private Long createTime;

    private String args;

    private String exceptionClass;

    private String exceptionDetail;
    
    private Long executedTime;
    
    ... setter and getter
}
```

#### 新建一个数据访问对象

``` java
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDAO extends MongoRepository<Log, String> {}
```

#### 实现日志服务接口

``` java
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import org.code4everything.boot.bean.LogBean;
import org.code4everything.boot.service.LogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class LogServiceImpl implements LogService<Log> {

    private final LogDAO logDAO;

    private final HttpServletRequest request;

    @Autowired
    public LogServiceImpl(LogDAO logDAO, HttpServletRequest request) {
        this.logDAO = logDAO;
        this.request = request;
    }

    @Override
    public Log save(Log log) {
        return logDAO.save(log);
    }

    @Override
    public Log saveException(Log log, Throwable throwable) {
        if (ObjectUtil.isNotNull(log) && ObjectUtil.isNotNull(throwable)) {
            log.setExceptionClass(throwable.getClass().getName());
            log.setExceptionDetail(throwable.getMessage());
            logDAO.save(log);
        }
        return log;
    }

    @Override
    public Log getLog(LogBean logBean) {
        Log log = new Log();
        BeanUtils.copyProperties(logBean, log);
        log.setId(IdUtil.simpleUUID());
        log.setCreateTime(System.currentTimeMillis());
        log.setIp(request.getRemoteAddr());
        return log;
    }
}
```

#### 创建切面类

``` java
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.code4everything.boot.constant.StringConsts;
import org.code4everything.boot.log.AopLogUtils;
import org.code4everything.boot.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {

    private final LogService<Log> logService;

    private final HttpServletRequest request;

    @Autowired
    public LogAspect(LogService<Log> logService, HttpServletRequest request) {
        this.logService = logService;
        this.request = request;
    }

    @Pointcut("@annotation(org.code4everything.boot.annotations.AopLog)")
    public void serviceAspect() {}

    @Before("serviceAspect()")
    public void doBefore(JoinPoint joinPoint) {
        doAfterThrowing(joinPoint, null);
    }

    @AfterThrowing(pointcut = "serviceAspect()", throwing = "throwable")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        String key = request.getHeader(StringConsts.TOKEN) + Thread.currentThread().getId();
        AopLogUtils.saveLog(logService, key, joinPoint, throwable);
    }
    
    /**
     * 或者使用 {@link Around} 方法
     */
    @Around("serviceAspect()")
    public Object doAround(ProceedingJoinPoint point) {
        return AopLogUtils.saveLog(logService, point).getResult();
    }
}
```

在需要日志记录的方法上面使用 `@AopLog` 注解，例如：

``` java
@Override
@AopLog("日志记录")
public void foo(String msg) {}
```

> 这里假设使用的是 `MongoDB` 数据库和 `Spring Data MongoDB` 框架，其他数据库和框架请根据实际情况进行相应的修改
