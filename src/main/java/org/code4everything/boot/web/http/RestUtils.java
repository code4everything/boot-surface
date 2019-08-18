package org.code4everything.boot.web.http;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.code4everything.boot.base.constant.StringConsts;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.web.mvc.exception.ExceptionFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * RestTemplate 工具类
 *
 * @author pantao
 * @since 2019/5/16
 */
public final class RestUtils {

    private static final String HTTP_PROTOCOL_HEAD = "http";

    private static String restServer;

    private static RestTemplate rest = null;

    private RestUtils() {}

    /**
     * 获取默认的RestTemplate
     *
     * @return {@link RestTemplate}
     *
     * @since 1.1.6
     */
    public static RestTemplate getRest() {
        if (Objects.isNull(rest)) {
            synchronized (RestUtils.class) {
                if (Objects.isNull(rest)) {
                    rest = new RestTemplate();
                    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
                    List<MediaType> supportedMediaTypes = new ArrayList<>();
                    supportedMediaTypes.add(MediaType.APPLICATION_JSON);
                    supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
                    supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
                    supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
                    supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
                    supportedMediaTypes.add(MediaType.APPLICATION_PDF);
                    supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
                    supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
                    supportedMediaTypes.add(MediaType.APPLICATION_XML);
                    supportedMediaTypes.add(MediaType.IMAGE_GIF);
                    supportedMediaTypes.add(MediaType.IMAGE_JPEG);
                    supportedMediaTypes.add(MediaType.IMAGE_PNG);
                    supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
                    supportedMediaTypes.add(MediaType.TEXT_HTML);
                    supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
                    supportedMediaTypes.add(MediaType.TEXT_PLAIN);
                    supportedMediaTypes.add(MediaType.TEXT_XML);
                    converter.setSupportedMediaTypes(supportedMediaTypes);
                    rest.getMessageConverters().add(converter);
                }
            }
        }
        return rest;
    }

    /**
     * 设置默认服务器
     *
     * @param restServer 服务器地址
     *
     * @since 1.1.2
     */
    public static void setRestServer(String restServer) {
        if (StrUtil.isNotEmpty(restServer)) {
            RestUtils.restServer = restServer;
        }
    }

    /**
     * 是否响应成功
     *
     * @param response {@link ResponseEntity}
     *
     * @return 是否响应成功
     *
     * @since 1.1.2
     */
    public static boolean isOk(ResponseEntity response) {
        return ObjectUtil.isNotNull(response) && response.getStatusCode().is2xxSuccessful();
    }

    /**
     * 要求请求成功
     *
     * @param response {@link ResponseEntity}
     *
     * @since 1.1.2
     */
    public static void requireOk(ResponseEntity response) {
        if (Objects.isNull(response)) {
            throw ExceptionFactory.exception(HttpStatus.REQUEST_TIMEOUT);
        }
        HttpStatus status = response.getStatusCode();
        if (status.is2xxSuccessful()) {
            return;
        }
        throw ExceptionFactory.exception(status.value(), status.getReasonPhrase());
    }

    /**
     * post {@link ResponseEntity}
     *
     * @param url 请求路径
     * @param params 参数
     * @param body 请求正文
     *
     * @return {@link ResponseEntity}
     *
     * @since 1.1.2
     */
    public static ResponseEntity<Object> postForEntity(String url, Object body, Object... params) {
        return postForEntity(url, body, Object.class, params);
    }

    /**
     * post {@link ResponseEntity}
     *
     * @param url 请求路径
     * @param responseType 响应类型
     * @param params 参数
     * @param body 请求正文
     * @param <T> 响应类型
     *
     * @return {@link ResponseEntity}
     *
     * @since 1.1.2
     */
    public static <T> ResponseEntity<T> postForEntity(String url, Object body, Class<T> responseType,
                                                      Object... params) {
        return getRest().postForEntity(formatUrl(url, params), body, responseType);
    }

    /**
     * post {@link Object}
     *
     * @param url 请求路径
     * @param params 参数
     * @param body 请求正文
     *
     * @return {@link Object}
     *
     * @since 1.1.2
     */
    public static Object postForObject(String url, Object body, Object... params) {
        return postForObject(url, body, Object.class, params);
    }

    /**
     * post {@link T}
     *
     * @param url 请求路径
     * @param responseType 响应类型
     * @param params 参数
     * @param body 请求正文
     * @param <T> 响应类型
     *
     * @return {@link T}
     *
     * @since 1.1.2
     */
    public static <T> T postForObject(String url, Object body, Class<T> responseType, Object... params) {
        return getRest().postForObject(formatUrl(url, params), body, responseType);
    }

    /**
     * post {@link JSONObject}
     *
     * @param url 请求路径
     * @param params 参数
     * @param body 请求正文
     *
     * @return {@link JSONObject}
     *
     * @since 1.1.2
     */
    public static JSONObject postForJsonObject(String url, Object body, Object... params) {
        return JSONObject.parseObject(postForObject(url, body, params).toString());
    }

    /**
     * post {@link JSONArray}
     *
     * @param url 请求路径
     * @param params 参数
     * @param body 请求正文
     *
     * @return {@link JSONArray}
     *
     * @since 1.1.2
     */
    public static JSONArray postForJsonArray(String url, Object body, Object... params) {
        return JSONArray.parseArray(postForObject(url, body, params).toString());
    }

    /**
     * get {@link ResponseEntity}
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
     * get {@link ResponseEntity}
     *
     * @param url 请求路径
     * @param responseType 响应类型
     * @param params 参数
     * @param <T> 响应类型
     *
     * @return {@link ResponseEntity}
     *
     * @since 1.1.2
     */
    public static <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... params) {
        return getRest().getForEntity(formatUrl(url, params), responseType);
    }

    /**
     * get {@link Object}
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
     * get {@link T}
     *
     * @param url 请求路径
     * @param responseType 响应类型
     * @param params 参数
     * @param <T> 响应类型
     *
     * @return {@link T}
     *
     * @since 1.1.2
     */
    public static <T> T getForObject(String url, Class<T> responseType, Object... params) {
        return getRest().getForObject(formatUrl(url, params), responseType);
    }

    /**
     * get {@link JSONObject}
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
     * patch {@link Object}
     *
     * @param url 请求路径
     * @param params 参数
     * @param body 请求正文
     *
     * @return {@link Object}
     *
     * @since 1.1.2
     */
    public static Object patchForObject(String url, Object body, Object... params) {
        return patchForObject(url, body, Object.class, params);
    }

    /**
     * patch {@link T}
     *
     * @param url 请求路径
     * @param responseType 响应类型
     * @param params 参数
     * @param body 请求正文
     * @param <T> 响应类型
     *
     * @return {@link T}
     *
     * @since 1.1.2
     */
    public static <T> T patchForObject(String url, Object body, Class<T> responseType, Object... params) {
        return getRest().patchForObject(formatUrl(url, params), body, responseType);
    }

    /**
     * get {@link JSONObject}
     *
     * @param url 请求路径
     * @param params 参数
     * @param body 请求正文
     *
     * @return {@link JSONObject}
     *
     * @since 1.1.2
     */
    public static JSONObject patchForJsonObject(String url, Object body, Object... params) {
        return JSONObject.parseObject(patchForObject(url, body, params).toString());
    }

    /**
     * get {@link JSONArray}
     *
     * @param url 请求路径
     * @param params 参数
     * @param body 请求正文
     *
     * @return {@link JSONArray}
     *
     * @since 1.1.2
     */
    public static JSONArray patchForJsonArray(String url, Object body, Object... params) {
        return JSONArray.parseArray(patchForObject(url, body, params).toString());
    }

    /**
     * delete
     *
     * @param url 请求路径
     * @param params 参数
     *
     * @since 1.1.2
     */
    public static void delete(String url, Object... params) {
        rest.delete(formatUrl(url, params));
    }

    /**
     * put
     *
     * @param url 请求路径
     * @param body 请求正文
     * @param params 参数
     *
     * @since 1.1.2
     */
    public static void put(String url, Object body, Object... params) {
        rest.put(formatUrl(url, params), body);
    }

    /**
     * options
     *
     * @param url 请求路径
     * @param params 参数
     *
     * @return 支持的请求方法
     *
     * @since 1.1.2
     */
    public static Set<HttpMethod> options(String url, Object... params) {
        return rest.optionsForAllow(formatUrl(url, params));
    }

    /**
     * head
     *
     * @param url 请求路径
     * @param params 参数
     *
     * @return 请求头
     *
     * @since 1.1.2
     */
    public static HttpHeaders head(String url, Object... params) {
        return rest.headForHeaders(formatUrl(url, params));
    }

    /**
     * 转发 {@link HttpServletRequest} 参数
     *
     * @param url 请求路径
     * @param request {@link HttpServletRequest}
     * @param ignore 忽略参数
     *
     * @return 整合后的请求路径
     *
     * @since 1.1.2
     */
    public static String forwardRequest(String url, HttpServletRequest request, String... ignore) {
        Set<String> ignoreSet = new HashSet<>();
        if (ArrayUtil.isNotEmpty(ignore)) {
            ignoreSet.addAll(Arrays.asList(ignore));
        }
        return forwardRequest(url, request, ignoreSet);
    }

    /**
     * 转发 {@link HttpServletRequest} 参数
     *
     * @param url 请求路径
     * @param request {@link HttpServletRequest}
     * @param ignore 忽略参数
     *
     * @return 整合后的请求路径
     *
     * @since 1.1.2
     */
    public static String forwardRequest(String url, HttpServletRequest request, Set<String> ignore) {
        StringBuilder builder = new StringBuilder().append(url);
        String seq = url.contains("?") ? "&" : "?";
        Set<String> paramKey = request.getParameterMap().keySet();
        for (String key : paramKey) {
            if (!ignore.contains(key)) {
                builder.append(seq).append(key).append("=").append(request.getParameter(key));
                seq = "&";
            }
        }
        return builder.toString();
    }

    private static String formatUrl(String url, Object... params) {
        if (!url.startsWith(HTTP_PROTOCOL_HEAD) && StrUtil.isNotEmpty(restServer)) {
            url = restServer + (restServer.endsWith("/") || url.startsWith("/") ? "" : "/") + url;
        }
        if (ArrayUtil.isNotEmpty(params)) {
            url = StrUtil.format(String.format(url, params), params);
        }
        if (BootConfig.isDebug()) {
            String date = DateUtil.format(new Date(), StringConsts.DateFormat.DATE_TIME_MILLIS);
            Console.log(date + " RestUtils - request url: " + url);
        }
        return url;
    }

    /**
     * 添加自定义消息转换器
     *
     * @param converters 转换器数组
     *
     * @since 1.1.2
     */
    public static void addMessageConverters(HttpMessageConverter<?>... converters) {
        if (ArrayUtil.isNotEmpty(converters)) {
            for (HttpMessageConverter<?> converter : converters) {
                getRest().getMessageConverters().add(converter);
            }
        }
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
    public static RestTemplate newRest(HttpMessageConverter<?>... converters) {
        RestTemplate template = new RestTemplate();
        if (ArrayUtil.isNotEmpty(converters)) {
            for (HttpMessageConverter<?> converter : converters) {
                template.getMessageConverters().add(converter);
            }
        }
        return template;
    }
}
