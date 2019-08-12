package org.code4everything.boot.web.mvc;

import cn.hutool.core.collection.CollUtil;
import org.code4everything.boot.base.constant.MessageConsts;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.web.http.HttpUtils;
import org.code4everything.boot.web.mvc.exception.ExceptionBiscuit;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Objects;

/**
 * 控制器基类
 *
 * @author pantao
 * @since 2018/11/2
 */
public class BaseController {

    private static final int DEFAULT_ERROR_CODE = BootConfig.DEFAULT_ERROR_CODE;

    private static final String DEFAULT_OK_MSG = MessageConsts.REQUEST_OK_ZH;

    @Resource
    public HttpServletRequest request;

    /**
     * 只允许继承
     *
     * @since 1.0.9
     */
    protected BaseController() {}

    /**
     * 创建对象
     *
     * @param request {@link HttpServletRequest}
     *
     * @since 1.1.3
     */
    public BaseController(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 返回 {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @param <T> 数据类型
     *
     * @return {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @since 1.1.2
     */
    public <T> ResponseEntity<Response<T>> http() {
        return http(null, null, HttpStatus.OK);
    }

    /**
     * 返回 {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @param status 响应状态
     * @param <T> 数据类型
     *
     * @return {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @since 1.1.2
     */
    public <T> ResponseEntity<Response<T>> http(HttpStatus status) {
        return http(null, null, status);
    }

    /**
     * 返回 {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @param headers 响应头
     * @param status 响应状态
     * @param <T> 数据类型
     *
     * @return {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @since 1.1.2
     */
    public <T> ResponseEntity<Response<T>> http(HttpHeaders headers, HttpStatus status) {
        return http(null, headers, status);
    }

    /**
     * 返回 {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @param response 响应BODY
     * @param <T> 数据类型
     *
     * @return {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @since 1.1.2
     */
    public <T> ResponseEntity<Response<T>> http(Response<T> response) {
        return http(response, null, HttpStatus.OK);
    }

    /**
     * 返回 {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @param response 响应BODY
     * @param headers 响应头
     * @param <T> 数据类型
     *
     * @return {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @since 1.1.2
     */
    public <T> ResponseEntity<Response<T>> http(Response<T> response, HttpHeaders headers) {
        return http(response, headers, HttpStatus.OK);
    }

    /**
     * 返回 {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @param response 响应BODY
     * @param status 响应状态
     * @param <T> 数据类型
     *
     * @return {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @since 1.1.2
     */
    public <T> ResponseEntity<Response<T>> http(Response<T> response, HttpStatus status) {
        return http(response, null, status);
    }

    /**
     * 返回 {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @param response 响应BODY
     * @param headers 响应头
     * @param status 响应状态
     * @param <T> 数据类型
     *
     * @return {@link ResponseEntity} extends {@link HttpEntity}
     *
     * @since 1.1.2
     */
    public <T> ResponseEntity<Response<T>> http(Response<T> response, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(response, headers, status);
    }

    /**
     * 获取Token
     *
     * @return Token
     *
     * @since 1.0.0
     */
    public String getToken() {
        return HttpUtils.getToken(request);
    }

    /**
     * 获取Token
     *
     * @param tokenKey 自定义Token Key
     *
     * @return Token
     *
     * @since 1.0.9
     */
    public String getToken(String tokenKey) {
        return HttpUtils.getToken(tokenKey, request);
    }

    /**
     * 获取Token
     *
     * @return Token
     *
     * @since 1.0.4
     */
    public String requireToken() {
        return HttpUtils.requireToken(request);
    }

    /**
     * 请求成功
     *
     * @param <T> 数据类
     *
     * @return {@link Response}
     *
     * @since 1.0.4
     */
    public <T> Response<T> successResult() {
        return successResult(DEFAULT_OK_MSG);
    }

    /**
     * 请求成功
     *
     * @param <T> 数据类
     * @param data 数据
     *
     * @return {@link Response}
     *
     * @since 1.0.8
     */
    public <T> Response<T> successResult(T data) {
        return successResult(data, BootConfig.isSealed());
    }

    /**
     * 请求成功
     *
     * @param <T> 数据类
     * @param data 数据
     * @param sealed 是否加密
     *
     * @return {@link Response}
     *
     * @since 1.1.2
     */
    public <T> Response<T> successResult(T data, boolean sealed) {
        return successResult(DEFAULT_OK_MSG, data, sealed);
    }

    /**
     * 请求成功
     *
     * @param okMsg 成功消息
     * @param <T> 数据类
     *
     * @return {@link Response}
     *
     * @since 1.0.0
     */
    public <T> Response<T> successResult(String okMsg) {
        return successResult(okMsg, null, false);
    }

    /**
     * 请求成功
     *
     * @param okMsg 成功消息
     * @param data 数据
     * @param <T> 数据类
     *
     * @return {@link Response}
     *
     * @since 1.0.4
     */
    public <T> Response<T> successResult(String okMsg, T data) {
        return successResult(okMsg, data, BootConfig.isSealed());
    }

    /**
     * 请求成功
     *
     * @param okMsg 成功消息
     * @param data 数据
     * @param <T> 数据类
     * @param sealed 是否加密
     *
     * @return {@link Response}
     *
     * @since 1.0.4
     */
    public <T> Response<T> successResult(String okMsg, T data, boolean sealed) {
        return printAndReturn(new Response<>(okMsg, data), sealed);
    }

    /**
     * 请求失败
     *
     * @param errMsg 错误消息
     * @param <T> 数据类
     *
     * @return {@link Response}
     *
     * @since 1.0.0
     */
    public <T> Response<T> errorResult(String errMsg) {
        return errorResult(DEFAULT_ERROR_CODE, errMsg);
    }

    /**
     * 请求失败
     *
     * @param errCode 错误码
     * @param errMsg 错误消息
     * @param <T> 数据类
     *
     * @return {@link Response}
     *
     * @since 1.0.0
     */
    public <T> Response<T> errorResult(int errCode, String errMsg) {
        return printAndReturn(new Response<T>().error(errCode, errMsg));
    }

    /**
     * 请求失败
     *
     * @param biscuit {@link ExceptionBiscuit}
     * @param <T> 数据类
     *
     * @return {@link Response}
     *
     * @since 1.1.2
     */
    public <T> Response<T> errorResult(ExceptionBiscuit biscuit) {
        return errorResult(biscuit.getCode(), biscuit.getMsg());
    }

    /**
     * 解析结果
     *
     * @param isOk 是否请求成功
     *
     * @return 结果
     *
     * @since 1.1.0
     */
    public Response<Boolean> parseBoolean(boolean isOk) {
        return parseBoolean(DEFAULT_OK_MSG, DEFAULT_OK_MSG, isOk);
    }

    /**
     * 解析结果
     *
     * @param errMsg 请求失败消息
     * @param isOk 是否请求成功
     *
     * @return 结果
     *
     * @since 1.0.0
     */
    public Response<Boolean> parseBoolean(String errMsg, boolean isOk) {
        return parseBoolean(DEFAULT_OK_MSG, errMsg, isOk);
    }

    /**
     * 解析结果
     *
     * @param okMsg 请求成功消息
     * @param errMsg 请求失败消息
     * @param isOk 是否请求成功
     *
     * @return 结果
     *
     * @since 1.0.0
     */
    public Response<Boolean> parseBoolean(String okMsg, String errMsg, boolean isOk) {
        return successResult(isOk ? okMsg : errMsg, isOk, false);
    }

    /**
     * 解析结果（对数据进行NULL判断）
     *
     * @param errMsg 请求失败的消息
     * @param data 数据
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.0
     */
    public <T> Response<T> parseResult(String errMsg, T data) {
        return parseResult(errMsg, data, BootConfig.isSealed());
    }

    /**
     * 解析结果（对数据进行NULL判断）
     *
     * @param errMsg 请求失败的消息
     * @param data 数据
     * @param sealed 是否对字段进行加密
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.0
     */
    public <T> Response<T> parseResult(String errMsg, T data, boolean sealed) {
        return parseResult(DEFAULT_OK_MSG, errMsg, DEFAULT_ERROR_CODE, data, sealed);
    }

    /**
     * 解析结果（对数据进行NULL判断）
     *
     * @param errMsg 请求失败的消息
     * @param errCode 错误码
     * @param data 数据
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.0
     */
    public <T> Response<T> parseResult(String errMsg, int errCode, T data) {
        return parseResult(errMsg, errCode, data, BootConfig.isSealed());
    }

    /**
     * 解析结果（对数据进行NULL判断）
     *
     * @param errMsg 请求失败的消息
     * @param errCode 错误码
     * @param data 数据
     * @param sealed 是否对字段进行加密
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.0
     */
    public <T> Response<T> parseResult(String errMsg, int errCode, T data, boolean sealed) {
        return parseResult(DEFAULT_OK_MSG, errMsg, errCode, data, sealed);
    }

    /**
     * 解析结果（对数据进行NULL判断）
     *
     * @param okMsg 请求成功的消息
     * @param errMsg 请求失败的消息
     * @param data 数据
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.0
     */
    public <T> Response<T> parseResult(String okMsg, String errMsg, T data) {
        return parseResult(okMsg, errMsg, data, BootConfig.isSealed());
    }

    /**
     * 解析结果（对数据进行NULL判断）
     *
     * @param okMsg 请求成功的消息
     * @param errMsg 请求失败的消息
     * @param data 数据
     * @param sealed 是否对字段进行加密
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.0
     */
    public <T> Response<T> parseResult(String okMsg, String errMsg, T data, boolean sealed) {
        return parseResult(okMsg, errMsg, DEFAULT_ERROR_CODE, data, sealed);
    }

    /**
     * 解析结果（对数据进行NULL判断）
     *
     * @param okMsg 请求成功的消息
     * @param errMsg 请求失败的消息
     * @param errCode 错误码
     * @param data 数据
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.0
     */
    public <T> Response<T> parseResult(String okMsg, String errMsg, int errCode, T data) {
        return parseResult(okMsg, errMsg, errCode, data, BootConfig.isSealed());
    }

    /**
     * 解析结果（对数据进行NULL判断）
     *
     * @param okMsg 请求成功的消息
     * @param errMsg 请求失败的消息
     * @param errCode 错误码
     * @param data 数据
     * @param sealed 是否对字段进行加密
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.0
     */
    public <T> Response<T> parseResult(String okMsg, String errMsg, int errCode, T data, boolean sealed) {
        return Objects.isNull(data) ? errorResult(errCode, errMsg) : successResult(okMsg, data, sealed);
    }

    /**
     * 解析结果
     *
     * @param errMsg 请求失败的消息
     * @param data 数据
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.5
     */
    public <T extends Collection> Response<T> parseCollection(String errMsg, T data) {
        return parseCollection(errMsg, data, BootConfig.isSealed());
    }

    /**
     * 解析结果
     *
     * @param errMsg 请求失败的消息
     * @param data 数据
     * @param sealed 是否对字段进行加密
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.5
     */
    public <T extends Collection> Response<T> parseCollection(String errMsg, T data, boolean sealed) {
        return parseCollection(DEFAULT_OK_MSG, errMsg, DEFAULT_ERROR_CODE, data, sealed);
    }

    /**
     * 解析结果
     *
     * @param okMsg 请求成功的消息
     * @param errMsg 请求失败的消息
     * @param data 数据
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.5
     */
    public <T extends Collection> Response<T> parseCollection(String okMsg, String errMsg, T data) {
        return parseCollection(okMsg, errMsg, data, BootConfig.isSealed());
    }

    /**
     * 解析结果
     *
     * @param okMsg 请求成功的消息
     * @param errMsg 请求失败的消息
     * @param data 数据
     * @param sealed 是否对字段进行加密
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.5
     */
    public <T extends Collection> Response<T> parseCollection(String okMsg, String errMsg, T data, boolean sealed) {
        return parseCollection(okMsg, errMsg, DEFAULT_ERROR_CODE, data, sealed);
    }

    /**
     * 解析结果
     *
     * @param errMsg 请求失败的消息
     * @param errCode 错误码
     * @param data 数据
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.5
     */
    public <T extends Collection> Response<T> parseCollection(String errMsg, int errCode, T data) {
        return parseCollection(errMsg, errCode, data, BootConfig.isSealed());
    }

    /**
     * 解析结果
     *
     * @param errMsg 请求失败的消息
     * @param errCode 错误码
     * @param data 数据
     * @param sealed 是否对字段进行加密
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.5
     */
    public <T extends Collection> Response<T> parseCollection(String errMsg, int errCode, T data, boolean sealed) {
        return parseCollection(DEFAULT_OK_MSG, errMsg, errCode, data, sealed);
    }

    /**
     * 解析结果
     *
     * @param okMsg 请求成功的消息
     * @param errMsg 请求失败的消息
     * @param errCode 错误码
     * @param data 数据
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.5
     */
    public <T extends Collection> Response<T> parseCollection(String okMsg, String errMsg, int errCode, T data) {
        return parseCollection(okMsg, errMsg, errCode, data, BootConfig.isSealed());
    }

    /**
     * 解析结果
     *
     * @param okMsg 请求成功的消息
     * @param errMsg 请求失败的消息
     * @param errCode 错误码
     * @param data 数据
     * @param sealed 是否对字段进行加密
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.5
     */
    public <T extends Collection> Response<T> parseCollection(String okMsg, String errMsg, int errCode, T data,
                                                              boolean sealed) {
        return CollUtil.isEmpty(data) ? errorResult(errCode, errMsg) : successResult(okMsg, data, sealed);
    }

    /**
     * 输出日志信息
     *
     * @param response {@link Response}
     * @param <T> 数据类型
     *
     * @return {@link Response}
     *
     * @since 1.1.0
     */
    public <T> Response<T> printAndReturn(Response<T> response) {
        return response.debug(request);
    }

    /**
     * 输出日志信息
     *
     * @param response {@link Response}
     * @param <T> 数据类型
     *
     * @return {@link Response}
     *
     * @since 1.1.6
     */
    public <T> Response<T> printAndReturn(Response<T> response, boolean sealed) {
        return sealed ? response.debug(request).encode() : response.debug(request);
    }
}
