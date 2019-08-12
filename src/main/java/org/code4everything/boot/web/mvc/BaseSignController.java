package org.code4everything.boot.web.mvc;

import com.google.common.base.Strings;
import org.code4everything.boot.service.BootUserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 可获取用户信息的控制器基类
 *
 * @author pantao
 * @since 2019/6/19
 */
public class BaseSignController<U> extends BaseController {

    /**
     * @since 1.1.4
     */
    @Resource
    private BootUserService<U> userService;

    /**
     * 只能继承
     *
     * @since 1.1.4
     */
    protected BaseSignController() {}

    /**
     * 创建对象
     *
     * @param request {@link HttpServletRequest}
     * @param userService {@link BootUserService}
     *
     * @since 1.1.4
     */
    public BaseSignController(HttpServletRequest request, BootUserService<U> userService) {
        super(request);
        this.userService = userService;
    }

    /**
     * 获取用户
     *
     * @param token 令牌
     *
     * @return 用户
     *
     * @since 1.1.4
     */
    public U getUser(String token) {
        Objects.requireNonNull(userService, "please implementation interface 'BootUserService<U>'");
        return userService.getUserByToken(token);
    }

    /**
     * 获取用户
     *
     * @return 用户
     *
     * @since 1.1.4
     */
    public U getUser() {
        return getUser(Strings.nullToEmpty(getToken()));
    }

    /**
     * 获取用户
     *
     * @return 用户
     *
     * @since 1.1.4
     */
    public U requireUser() {
        return AssertUtils.assertUserLoggedIn(getUser(requireToken()));
    }
}
