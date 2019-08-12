package org.code4everything.boot.service.impl;

import org.code4everything.boot.service.BootBaseService;
import org.code4everything.boot.web.http.HttpUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 服务基类
 *
 * @author pantao
 * @since 2019/6/11
 */
public class BaseServiceImpl implements BootBaseService {

    @Resource
    public HttpServletRequest request;

    /**
     * 只能继承
     *
     * @since 1.1.3
     */
    protected BaseServiceImpl() {}

    /**
     * 创建对象
     *
     * @param request {@link HttpServletRequest}
     *
     * @since 1.1.3
     */
    public BaseServiceImpl(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getToken() {
        return HttpUtils.requireToken(request);
    }

    @Override
    public String getToken(boolean require) {
        return require ? HttpUtils.requireToken(request) : HttpUtils.getToken(request);
    }

    @Override
    public Object getUser() {
        return getUser(true);
    }

    @Override
    public Object getUser(boolean require) {
        throw new RuntimeException("if you want to get user directly, please use class 'BaseSignServiceImpl'");
    }
}
