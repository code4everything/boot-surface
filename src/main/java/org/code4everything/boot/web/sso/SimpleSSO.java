package org.code4everything.boot.web.sso;

import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.base.ReferenceUtils;
import org.code4everything.boot.base.constant.IntegerConsts;
import org.code4everything.boot.web.mvc.Response;

import javax.annotation.Nullable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pantao
 * @since 2019/6/26
 **/
class SimpleSSO implements SSOAware {

    private String loginUrl;

    private int sessionTimeout;

    /**
     * 缓存登录的用户
     *
     * @since 1.1.5
     */
    private Map<String, SoftReference<Object>> userCache = new ConcurrentHashMap<>(1024);

    /**
     * 缓存注册的子系统
     *
     * @since 1.1.5
     */
    private Map<String, SoftReference<Set<String>>> subSysCache = new ConcurrentHashMap<>(1024);

    /**
     * 缓存重定向路径
     *
     * @since 1.1.5
     */
    private Map<String, WeakReference<String>> urlCache = new ConcurrentHashMap<>(1024);

    public SimpleSSO() {
        this("/error", IntegerConsts.ONE_DAY_SECONDS);
    }

    public SimpleSSO(String loginUrl, int sessionTimeout) {
        this.loginUrl = loginUrl;
    }

    public SimpleSSO loginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
        return this;
    }

    @Override
    public Response<String> validateSession(String sid, String addr, @Nullable String url) {
        if (userCache.containsKey(sid)) {
            addSubSys(sid, addr);
            return new Response<>("用户已登录", generateToken());
        }
        if (StrUtil.isNotEmpty(url)) {
            urlCache.put(sid, new WeakReference<>(url));
        }
        return new Response<String>().error("请求用户登录").setData(loginUrl);
    }

    @Override
    public Response<LoginVO> registerSubSystem(String sid, String addr, Object user) {
        userCache.put(sid, new SoftReference<>(user));
        addSubSys(sid, addr);
        String url = ReferenceUtils.safeUnwrap(urlCache.get(sid));
        return new Response<>("登录成功", new LoginVO(generateToken(), url));
    }

    @Override
    public Response<Object> getUser(String sid) {
        return new Response<>(userCache.containsKey(sid) ? userCache.get(sid).get() : null);
    }

    private void addSubSys(String sid, String addr) {
        Set<String> subs = ReferenceUtils.safeUnwrap(subSysCache.get(sid));
        if (Objects.isNull(subs)) {
            subs = new HashSet<>(8);
            subSysCache.put(sid, new SoftReference<>(subs));
        }
        subs.add(addr);
    }
}
