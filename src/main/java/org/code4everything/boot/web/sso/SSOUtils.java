package org.code4everything.boot.web.sso;

/**
 * @author pantao
 * @since 2019/6/26
 **/
public class SSOUtils {

    private static final SSOAware aware = new SimpleSSO();

    private SSOUtils() {}

    public static String validateSession(SSOAware ssoAware, String sid) {
    }
}
