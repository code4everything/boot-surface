package org.code4everything.boot.service.impl;

import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.service.BootBaseService;
import org.code4everything.boot.service.BootUserService;
import org.code4everything.boot.web.http.HttpUtils;
import org.code4everything.boot.web.mvc.AssertUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author pantao
 * @since 2019/6/11
 **/
public class BaseServiceImpl<U> implements BootBaseService {

    @Resource
    public HttpServletRequest request;

    @Resource
    protected BootUserService<U> userService;

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
     * @param userService {@link BootUserService}
     *
     * @since 1.1.3
     */
    public BaseServiceImpl(HttpServletRequest request, BootUserService<U> userService) {
        this.request = request;
        this.userService = userService;
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
    public U getUser() {
        return getUser(true);
    }

    @Override
    public U getUser(boolean require) {
        Objects.requireNonNull(userService, "please set interface 'BootUserService<T>'");
        U user = userService.getUserByToken(StrUtil.nullToEmpty(getToken(require)));
        return require ? AssertUtils.assertUserLoggedIn(user) : user;
    }
}
