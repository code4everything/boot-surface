package org.code4everything.boot.web.mvc;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.code4everything.boot.bean.ExceptionBean;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.constant.IntegerConsts;
import org.code4everything.boot.exception.BootException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 默认异常处理器
 *
 * @author pantao
 * @since 2018/11/4
 */
public class DefaultExceptionHandler implements HandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    /**
     * 默认异常信息
     *
     * @since 1.0.0
     */
    private final ExceptionBean bean = new ExceptionBean().setCode(500).setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

    /**
     * 异常信息在项目启动时就应该配置好了，所以无需考虑并发情况
     *
     * @since 1.0.0
     */
    private Map<String, ExceptionBean> exceptionMap = new HashMap<>(16);

    /**
     * 获取默认的异常信息
     *
     * @param request {@link HttpServletRequest}
     * @param exception {@link Exception}
     * @param bean {@link ExceptionBean}
     *
     * @return {@link ModelAndView}
     *
     * @since 1.0.0
     */
    private ModelAndView parseModelAndView(HttpServletRequest request, Exception exception, ExceptionBean bean) {
        Objects.requireNonNull(bean);
        ModelAndView modelAndView = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<>(IntegerConsts.EIGHT);
        // 包装数据
        wrapAttributes(attributes, request, exception, bean);
        view.setAttributesMap(attributes);
        modelAndView.setView(view);
        modelAndView.setStatus(bean.getStatus());
        return modelAndView;
    }

    /**
     * 你可以重写这个方法，自定义返回内容
     *
     * @param attr {@link Map}
     * @param req {@link HttpServletRequest}
     * @param ex {@link Exception}
     * @param bean {@link ExceptionBean}
     *
     * @since 1.0.4
     */
    protected void wrapAttributes(Map<String, Object> attr, HttpServletRequest req, Exception ex, ExceptionBean bean) {
        attr.put("code", bean.getCode());
        attr.put("msg", Validator.isEmpty(bean.getMsg()) ? ex.getMessage() : bean.getMsg());
        attr.put("timestamp", System.currentTimeMillis());
        attr.put("dateTime", DateUtil.formatDateTime(new Date(System.currentTimeMillis())));
        String queryString = req.getQueryString();
        attr.put("path", req.getRequestURI() + (Validator.isEmpty(queryString) ? "" : "?" + queryString));
        attr.put("data", ex.getMessage());
    }

    /**
     * 获取自定义的异常信息
     *
     * @return 自定义的异常信息
     *
     * @since 1.0.0
     */
    public final Map<String, ExceptionBean> getExceptionMap() {
        return exceptionMap;
    }

    /**
     * 自定义异常信息，使用异常类的类名作为键值
     *
     * @param exceptionMap 异常信息
     *
     * @since 1.0.0
     */
    public final void setExceptionMap(Map<String, ExceptionBean> exceptionMap) {
        this.exceptionMap = exceptionMap;
    }

    /**
     * 添加异常信息
     *
     * @param e 自定义异常
     * @param code 错误码
     * @param msg 消息
     * @param status {@link HttpStatus} 状态
     * @param <T> 异常类型
     *
     * @since 1.0.0
     */
    public <T extends Exception> void addException(int code, String msg, HttpStatus status, Class<T> e) {
        Objects.requireNonNull(e);
        exceptionMap.put(e.getName(), new ExceptionBean().setCode(code).setMsg(msg).setStatus(status));
    }

    /**
     * 默认异常处理器
     *
     * @param req {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     * @param o {@link Object}
     * @param e {@link Exception}
     *
     * @return {@link ModelAndView}
     *
     * @since 1.0.0
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object o, Exception e) {
        if (BootConfig.isDebug()) {
            LOGGER.error("url -> {}, ip -> {}, exception -> {}, message -> {}", req.getServletPath(),
                         req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
            // 输出异常信息
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            String exception = stringWriter.toString();
            Console.log(exception);
            LOGGER.error(exception);
        }
        // 判断异常是否可以转换成ExceptionBean
        ExceptionBean exceptionBean;
        if (e instanceof BootException) {
            exceptionBean = ((BootException) e).asExceptionBean();
        } else {
            exceptionBean = exceptionMap.get(e.getClass().getName());
        }
        // 解析并返回
        return parseModelAndView(req, e, Objects.isNull(exceptionBean) ? bean : exceptionBean);
    }
}
