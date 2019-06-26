package org.code4everything.boot.web.sso;

import cn.hutool.core.util.IdUtil;
import org.code4everything.boot.web.mvc.Response;

import javax.annotation.Nullable;

/**
 * @author pantao
 * @since 2019/6/26
 **/
public interface SSOAware {

    /**
     * 生成令牌
     *
     * @return
     */
    default String generateToken() {
        return IdUtil.simpleUUID();
    }

    /**
     * 检测用户是否已经登录
     *
     * @param sid 会话唯一标识符
     * @param addr
     * @param url 用户当前的页面路径
     *
     * @return 验证结果
     *
     * @since 1.1.5
     */
    Response<String> validateSession(String sid, String addr, @Nullable String url);

    /**
     * 用户登录成功后，注册子系统
     *
     * @param sid 会话唯一标识符
     * @param addr 子系统地址
     * @param user 用户
     *
     * @return 注册结果
     *
     * @since 1.1.5
     */
    Response<LoginVO> registerSubSystem(String sid, String addr, Object user);

    Response<Object> getUser(String sid);
}
