package org.code4everything.boot.web.mvc;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.apache.log4j.Logger;
import org.code4everything.boot.bean.ExceptionBean;
import org.code4everything.boot.config.BootConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认异常处理器
 *
 * @author pantao
 * @since 2018/11/4
 */
public class DefaultExceptionHandler implements HandlerExceptionResolver {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private final Logger logger = Logger.getLogger(DefaultExceptionHandler.class);

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
     *
     * @return {@link ModelAndView}
     *
     * @since 1.0.0
     */
    public static ModelAndView parseModelAndView(HttpServletRequest request, Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<>(4);
        attributes.put("code", "500");
        attributes.put("msg", exception.getMessage());
        attributes.put("timestamp", DateUtil.format(new Date(), DATE_FORMAT));
        String queryString = request.getQueryString();
        attributes.put("data", request.getRequestURI() + (Validator.isEmpty(queryString) ? "" : "?" + queryString));
        view.setAttributesMap(attributes);
        modelAndView.setView(view);
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }

    /**
     * 获取自定义的异常信息
     *
     * @return 自定义的异常信息
     *
     * @since 1.0.0
     */
    public Map<String, ExceptionBean> getExceptionMap() {
        return exceptionMap;
    }

    /**
     * 自定义异常信息，在项目启动时就应该配置好了，所以无需考虑并发情况
     *
     * @param exceptionMap 异常信息
     *
     * @since 1.0.0
     */
    public void setExceptionMap(Map<String, ExceptionBean> exceptionMap) {
        this.exceptionMap = exceptionMap;
    }

    /**
     * 添加异常信息
     *
     * @param e 自定义异常
     * @param code 错误码
     * @param msg 消息
     * @param status {@link HttpStatus} 状态
     */
    public void addException(Exception e, int code, String msg, HttpStatus status) {
        ExceptionBean bean = new ExceptionBean();
        exceptionMap.put(e.getClass().getName(), bean.setCode(code).setException(e).setMsg(msg).setStatus(status));
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
            e.printStackTrace();
        }
        logger.error("url -> " + req.getServletPath() + ", message -> " + e.getMessage());
        return parseModelAndView(req, e);
    }
}
