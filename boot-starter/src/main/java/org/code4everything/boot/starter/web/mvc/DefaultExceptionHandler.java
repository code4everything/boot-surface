package org.code4everything.boot.starter.web.mvc;

import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认异常处理器
 *
 * @author pantao
 * @since 2018/11/4
 */
public class DefaultExceptionHandler implements HandlerExceptionResolver {

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
        attributes.put("timestamp", new Timestamp(System.currentTimeMillis()));
        String queryString = request.getQueryString();
        attributes.put("data", request.getRequestURI() + (Validator.isEmpty(queryString) ? "" : "?" + queryString));
        view.setAttributesMap(attributes);
        modelAndView.setView(view);
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }

    /**
     * 默认异常处理器
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param object {@link Object}
     * @param exception {@link Exception}
     *
     * @return {@link ModelAndView}
     *
     * @since 1.0.0
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
                                         Exception exception) {
        return parseModelAndView(request, exception);
    }
}
