package org.code4everything.boot.web.mvc;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.code4everything.boot.bean.ExceptionBean;
import org.code4everything.boot.bean.ExceptionBiscuit;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.constant.IntegerConsts;
import org.code4everything.boot.constant.StringConsts;
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
     * @param biscuit {@link ExceptionBiscuit}
     *
     * @return {@link ModelAndView}
     *
     * @since 1.0.0
     */
    private ModelAndView parseModelAndView(HttpServletRequest request, Exception exception, ExceptionBiscuit biscuit) {
        Objects.requireNonNull(biscuit);
        ModelAndView modelAndView = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<>(IntegerConsts.EIGHT);
        // 包装数据
        wrapAttributes(attributes, request, exception, biscuit);
        view.setAttributesMap(attributes);
        modelAndView.setView(view);
        modelAndView.setStatus(biscuit.getStatus());
        return modelAndView;
    }

    /**
     * 你可以重写这个方法，自定义返回内容
     *
     * @param attr {@link Map}
     * @param req {@link HttpServletRequest}
     * @param ex {@link Exception}
     * @param bis {@link ExceptionBiscuit}
     *
     * @since 1.0.4
     */
    protected void wrapAttributes(Map<String, Object> attr, HttpServletRequest req, Exception ex,
                                  ExceptionBiscuit bis) {
        attr.put("code", bis.getCode());
        attr.put("msg", StrUtil.isEmpty(bis.getMsg()) ? ex.getMessage() : bis.getMsg());
        attr.put("timestamp", System.currentTimeMillis());
        attr.put("dateTime", DateUtil.format(new Date(), StringConsts.DateFormat.DATE_TIME_MILLIS));
        String queryString = req.getQueryString();
        attr.put("path", req.getRequestURI() + (StrUtil.isEmpty(queryString) ? "" : "?" + queryString));
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
     * @param <T> 异常类型
     *
     * @since 1.1.0
     */
    public final <T extends Exception> void addException(int code, String msg, Class<T> e) {
        addException(code, msg, HttpStatus.valueOf(code), e);
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
    public final <T extends Exception> void addException(int code, String msg, HttpStatus status, Class<T> e) {
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
    public final ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object o, Exception e) {
        if (BootConfig.isDebug()) {
            LOGGER.error("url -> {}, ip -> {}, exception -> {}, message -> {}", req.getServletPath(),
                         req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        }
        // 判断异常是否可以转换成ExceptionBean
        ExceptionBiscuit biscuit;
        if (e instanceof BootException) {
            biscuit = (BootException) e;
        } else {
            biscuit = exceptionMap.get(e.getClass().getName());
        }
        if (Objects.isNull(biscuit)) {
            // 输出未处理的异常信息
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            String exception = stringWriter.toString();
            Console.error(exception);
            LOGGER.error(exception);
            return parseModelAndView(req, e, bean);
        }
        return parseModelAndView(req, e, biscuit);
    }
}
