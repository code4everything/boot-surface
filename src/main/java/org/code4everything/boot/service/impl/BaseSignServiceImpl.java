package org.code4everything.boot.service.impl;

import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.service.BootUserService;
import org.code4everything.boot.web.mvc.AssertUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 可获取用户的服务基类
 *
 * @author pantao
 * @since 2019/6/19
 */
public class BaseSignServiceImpl<U> extends BaseServiceImpl {

    @Resource
    protected BootUserService<U> userService;

    /**
     * 只能继承
     *
     * @since 1.1.4
     */
    protected BaseSignServiceImpl() {}

    /**
     * 创建对象
     *
     * @param request {@link HttpServletRequest}
     * @param userService {@link BootUserService}
     *
     * @since 1.1.3
     */
    public BaseSignServiceImpl(HttpServletRequest request, BootUserService<U> userService) {
        super(request);
        this.userService = userService;
    }

    @Override
    public U getUser(boolean require) {
        Objects.requireNonNull(userService, "please set interface 'BootUserService<T>'");
        U user = userService.getUserByToken(StrUtil.nullToEmpty(getToken(require)));
        return require ? AssertUtils.assertUserLoggedIn(user) : user;
    }

    @Override
    public U getUser() {
        return getUser(true);
    }
}
