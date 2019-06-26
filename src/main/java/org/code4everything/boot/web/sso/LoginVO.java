package org.code4everything.boot.web.sso;

import org.code4everything.boot.base.bean.BaseBean;

import java.io.Serializable;

/**
 * @author pantao
 * @since 2019/6/26
 **/
public class LoginVO implements BaseBean, Serializable {

    private static final long serialVersionUID = -3238074886033708538L;

    private String token;

    private String redirectUrl;

    public LoginVO() {}

    public LoginVO(String token, String redirectUrl) {
        this.token = token;
        this.redirectUrl = redirectUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
