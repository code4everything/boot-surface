package org.code4everything.boot.web.http;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * RestTemplate 工具类
 *
 * @author pantao
 * @since 2019/5/16
 **/
public final class RestUtils {

    private static final String HTTP_PROTOCOL_HEAD = "http";

    private static String serverAddress;

    private static RestTemplate rest = null;

    private RestUtils() {}

    /**
     * 设置默认服务器
     *
     * @param serverAddress 服务器地址
     *
     * @since 1.1.2
     */
    public static void setServerAddress(String serverAddress) {
        if (StrUtil.isNotEmpty(serverAddress)) {
            RestUtils.serverAddress = serverAddress;
        }
    }

    private static void checkRestTemplate() {
        if (Objects.isNull(rest)) {
            synchronized (RestUtils.class) {
                if (Objects.isNull(rest)) {
                    rest = new RestTemplate();
                    rest.getMessageConverters().add(new FastJsonHttpMessageConverter());
                    rest.getMessageConverters().add(new BufferedImageHttpMessageConverter());
                    rest.getMessageConverters().add(new ByteArrayHttpMessageConverter());
                }
            }
        }
    }

    /**
     * 请求 {@link ResponseEntity}
     *
     * @param url 请求路径
     * @param params 参数
     *
     * @return {@link ResponseEntity}
     *
     * @since 1.1.2
     */
    public static ResponseEntity<Object> getForEntity(String url, Object... params) {
        return getForEntity(url, Object.class, params);
    }

    /**
     * 请求 {@link ResponseEntity}
     *
     * @param url 请求路径
     * @param responseType 响应类型
     * @param params 参数
     *
     * @return {@link ResponseEntity}
     *
     * @since 1.1.2
     */
    public static <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... params) {
        checkRestTemplate();
        return rest.getForEntity(formatUrl(url, params), responseType);
    }

    /**
     * 请求 {@link Object}
     *
     * @param url 请求路径
     * @param params 参数
     *
     * @return {@link Object}
     *
     * @since 1.1.2
     */
    public static Object getForObject(String url, Object... params) {
        return getForObject(url, Object.class, params);
    }

    /**
     * 请求 {@link T}
     *
     * @param url 请求路径
     * @param responseType 响应类型
     * @param params 参数
     *
     * @return {@link T}
     *
     * @since 1.1.2
     */
    public static <T> T getForObject(String url, Class<T> responseType, Object... params) {
        checkRestTemplate();
        return rest.getForObject(formatUrl(url, params), responseType);
    }

    /**
     * 请求 {@link JSONObject}
     *
     * @param url 请求路径
     * @param params 参数
     *
     * @return {@link JSONObject}
     *
     * @since 1.1.2
     */
    public static JSONObject getForJsonObject(String url, Object... params) {
        return JSONObject.parseObject(getForObject(url, params).toString());
    }

    /**
     * 请求 {@link JSONArray}
     *
     * @param url 请求路径
     * @param params 参数
     *
     * @return {@link JSONArray}
     *
     * @since 1.1.2
     */
    public static JSONArray getForJsonArray(String url, Object... params) {
        return JSONArray.parseArray(getForObject(url, params).toString());
    }

    private static String formatUrl(String url, Object... params) {
        if (!url.startsWith(HTTP_PROTOCOL_HEAD) && StrUtil.isNotEmpty(serverAddress)) {
            url = serverAddress + (serverAddress.endsWith("/") || url.startsWith("/") ? "" : "/") + url;
        }
        return StrUtil.format(String.format(url, params), params);
    }

    /**
     * 添加自定义消息转换器
     *
     * @param converters 转换器数组
     *
     * @since 1.1.2
     */
    public static void addMessageConverters(HttpMessageConverter<?>... converters) {
        checkRestTemplate();
        if (ArrayUtil.isNotEmpty(converters)) {
            for (HttpMessageConverter<?> converter : converters) {
                rest.getMessageConverters().add(converter);
            }
        }
    }

    /**
     * 获取 {@link RestTemplate}
     *
     * @return {@link RestTemplate}
     *
     * @since 1.1.2
     */
    public static RestTemplate getTemplate() {
        checkRestTemplate();
        return rest;
    }

    /**
     * 消息转换器
     *
     * @param converters 转换器数组
     *
     * @return {@link RestTemplate}
     *
     * @since 1.1.2
     */
    public static RestTemplate newTemplate(HttpMessageConverter<?>... converters) {
        RestTemplate template = new RestTemplate();
        if (ArrayUtil.isNotEmpty(converters)) {
            for (HttpMessageConverter<?> converter : converters) {
                template.getMessageConverters().add(converter);
            }
        }
        return template;
    }
}
