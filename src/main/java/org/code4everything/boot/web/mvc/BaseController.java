package org.code4everything.boot.web.mvc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.google.common.base.Strings;
import org.code4everything.boot.base.AssertUtils;
import org.code4everything.boot.base.function.BooleanFunction;
import org.code4everything.boot.base.function.ResponseResultFunction;
import org.code4everything.boot.bean.ResponseResult;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.constant.MessageConsts;
import org.code4everything.boot.exception.ExceptionThrower;
import org.code4everything.boot.service.UserService;
import org.code4everything.boot.web.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * 控制器基类
 *
 * @author pantao
 * @since 2018/11/2
 **/
@RestController
public class BaseController {

    private static final int DEFAULT_ERROR_CODE = HttpStatus.HTTP_BAD_REQUEST;

    private static final String DEFAULT_OK_MSG = MessageConsts.REQUEST_OK_ZH;

    private static int okCode = HttpStatus.HTTP_OK;

    @Autowired
    public HttpServletRequest request;

    private ThreadLocal<ResponseResult<? extends Serializable>> resultThreadLocal = new ThreadLocal<>();

    /**
     * 获取正确码
     *
     * @return 正确码
     *
     * @since 1.0.5
     */
    public static int getOkCode() {
        return okCode;
    }

    /**
     * 设置正确码
     *
     * @param okCode 正确码
     *
     * @since 1.0.5
     */
    public static void setOkCode(int okCode) {
        BaseController.okCode = okCode;
    }

    /**
     * 抛出异常
     *
     * @param function 布尔函数
     *
     * @return {@link ExceptionThrower}
     *
     * @since 1.0.5
     */
    public static ExceptionThrower throwIf(BooleanFunction function, RuntimeException exception) {
        return AssertUtils.throwIf(function, exception);
    }

    /**
     * 是否有结果
     *
     * @return 是否有结果
     *
     * @since 1.0.5
     */
    public boolean hasResult() {
        return ObjectUtil.isNotNull(resultThreadLocal.get());
    }

    /**
     * 获取结果
     *
     * @param <T> 数据类型
     *
     * @return 结果
     *
     * @since 1.0.5
     */
    public <T extends Serializable> ResponseResult<T> getReturn() {
        ResponseResult<?> responseResult = resultThreadLocal.get();
        if (Objects.isNull(responseResult)) {
            return null;
        } else {
            ResponseResult<T> result = new ResponseResult<T>().copyFrom(responseResult);
            resultThreadLocal.remove();
            return result;
        }
    }

    /**
     * 抛出异常
     *
     * @param shouldThrow 是否抛出异常
     *
     * @return {@link ExceptionThrower}
     *
     * @since 1.0.5
     */
    public ExceptionThrower throwIf(boolean shouldThrow, RuntimeException exception) {
        return AssertUtils.throwIf(shouldThrow, exception);
    }

    /**
     * 如果条件为真时返回
     *
     * @param shouldReturn 是否返回结果
     * @param result 响应结果
     * @param <T> 数据类型
     *
     * @return {@link BaseController}
     *
     * @since 1.0.5
     */
    public <T extends Serializable> BaseController ifReturn(boolean shouldReturn, ResponseResult<T> result) {
        if (!hasResult() && shouldReturn) {
            resultThreadLocal.set(result);
        }
        return this;
    }

    /**
     * 如果条件为真时返回
     *
     * @param shouldReturn 是否返回结果
     * @param function 结果响应函数
     *
     * @return {@link BaseController}
     *
     * @since 1.0.5
     */
    public BaseController ifReturn(boolean shouldReturn, ResponseResultFunction function) {
        if (!hasResult() && shouldReturn) {
            resultThreadLocal.set(function.get());
        }
        return this;
    }

    /**
     * 如果条件为真时返回
     *
     * @param function 布尔函数
     * @param result 响应结果
     *
     * @return {@link BaseController}
     *
     * @since 1.0.5
     */
    public BaseController ifReturn(BooleanFunction function, ResponseResult result) {
        if (!hasResult() && function.get()) {
            resultThreadLocal.set(result);
        }
        return this;
    }

    /**
     * 如果条件为真时返回
     *
     * @param booleanFunction 布尔函数
     * @param responseResultFunction 结果响应函数
     *
     * @return {@link BaseController}
     *
     * @since 1.0.5
     */
    public BaseController ifReturn(BooleanFunction booleanFunction, ResponseResultFunction responseResultFunction) {
        if (!hasResult()) {
            boolean res = booleanFunction.get();
            if (res) {
                resultThreadLocal.set(responseResultFunction.get());
            }
        }
        return this;
    }

    /**
     * 没有结果时返回
     *
     * @param result 响应结果
     * @param <T> 数据类型
     *
     * @return {@link BaseController}
     *
     * @since 1.0.5
     */
    public <T extends Serializable> BaseController elseReturn(ResponseResult<T> result) {
        return ifReturn(true, result);
    }

    /**
     * 没有结果时返回
     *
     * @param function 结果响应函数
     *
     * @return {@link BaseController}
     *
     * @since 1.0.5
     */
    public BaseController elseReturn(ResponseResultFunction function) {
        if (!hasResult()) {
            resultThreadLocal.set(function.get());
        }
        return this;
    }

    /**
     * 返回结果
     *
     * @param result 响应结果
     * @param <T> 数据类型
     *
     * @return {@link BaseController}
     *
     * @since 1.0.5
     */
    public <T extends Serializable> ResponseResult<T> getReturn(ResponseResult<T> result) {
        return hasResult() ? getReturn() : result;
    }

    /**
     * 返回结果
     *
     * @param function 结果响应函数
     *
     * @return {@link BaseController}
     *
     * @since 1.0.5
     */
    public <T extends Serializable> ResponseResult<T> getReturn(ResponseResultFunction function) {
        return hasResult() ? getReturn() : function.get();
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
     * 获取用户
     *
     * @param userService 用户服务
     * @param <T> 用户
     *
     * @return 用户
     *
     * @since 1.0.4
     */
    public <T extends Serializable> T getUser(UserService<T> userService) {
        return userService.getUserByToken(Strings.nullToEmpty(getToken()));
    }

    /**
     * 获取用户
     *
     * @param userService 用户服务
     * @param <T> 用户
     *
     * @return 用户
     *
     * @since 1.0.4
     */
    public <T extends Serializable> T requireUser(UserService<T> userService) {
        return AssertUtils.assertUserLoggedIn(userService.getUserByToken(requireToken()));
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
     * @return {@link ResponseResult}
     *
     * @since 1.0.4
     */
    public <T extends Serializable> ResponseResult<T> successResult() {
        return new ResponseResult<T>().setCode(okCode);
    }

    /**
     * 请求成功
     *
     * @param okMsg 成功消息
     * @param <T> 数据类
     *
     * @return {@link ResponseResult}
     *
     * @since 1.0.0
     */
    public <T extends Serializable> ResponseResult<T> successResult(String okMsg) {
        return new ResponseResult<T>().setMsg(okMsg).setCode(okCode);
    }

    /**
     * 请求成功
     *
     * @param okMsg 成功消息
     * @param data 数据
     * @param <T> 数据类
     *
     * @return {@link ResponseResult}
     *
     * @since 1.0.4
     */
    public <T extends Serializable> ResponseResult<T> successResult(String okMsg, T data) {
        return new ResponseResult<>(okCode, okMsg, data);
    }

    /**
     * 请求成功
     *
     * @param okMsg 成功消息
     * @param data 数据
     * @param <T> 数据类
     *
     * @return {@link ResponseResult}
     *
     * @since 1.0.5
     */
    public <T extends Serializable> ResponseResult<T> successResult(int okCode, String okMsg, T data) {
        return new ResponseResult<>(okCode, okMsg, data);
    }

    /**
     * 请求失败
     *
     * @param errMsg 错误消息
     * @param <T> 数据类
     *
     * @return {@link ResponseResult}
     *
     * @since 1.0.0
     */
    public <T extends Serializable> ResponseResult<T> errorResult(String errMsg) {
        return new ResponseResult<T>().error(errMsg);
    }

    /**
     * 请求失败
     *
     * @param errCode 错误码
     * @param errMsg 错误消息
     * @param <T> 数据类
     *
     * @return {@link ResponseResult}
     *
     * @since 1.0.0
     */
    public <T extends Serializable> ResponseResult<T> errorResult(int errCode, String errMsg) {
        return new ResponseResult<T>().error(errCode, errMsg);
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
    public ResponseResult<Boolean> parseBoolean(String errMsg, boolean isOk) {
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
    public ResponseResult<Boolean> parseBoolean(String okMsg, String errMsg, boolean isOk) {
        return new ResponseResult<Boolean>().setMsg(isOk ? okMsg : errMsg).setData(isOk).setCode(okCode);
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
    public <T extends Serializable> ResponseResult<T> parseResult(String errMsg, T data) {
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
    public <T extends Serializable> ResponseResult<T> parseResult(String errMsg, T data, boolean sealed) {
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
    public <T extends Serializable> ResponseResult<T> parseResult(String errMsg, int errCode, T data) {
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
    public <T extends Serializable> ResponseResult<T> parseResult(String errMsg, int errCode, T data, boolean sealed) {
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
    public <T extends Serializable> ResponseResult<T> parseResult(String okMsg, String errMsg, T data) {
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
    public <T extends Serializable> ResponseResult<T> parseResult(String okMsg, String errMsg, T data, boolean sealed) {
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
    public <T extends Serializable> ResponseResult<T> parseResult(String okMsg, String errMsg, int errCode, T data) {
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
    public <T extends Serializable> ResponseResult<T> parseResult(String okMsg, String errMsg, int errCode, T data,
                                                                  boolean sealed) {
        boolean isError = ObjectUtil.isNull(data);
        if (!isError) {
            if (data instanceof Boolean && !(Boolean) data) {
                isError = true;
            } else if (sealed) {
                BootConfig.getFieldEncoder().encode(data);
            }
        }
        return isError ? errorResult(errCode, errMsg) : successResult(okMsg, data);
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
    public <T extends Serializable> ResponseResult<T> parseCollection(String errMsg, Collection<?
            extends Serializable> data) {
        return parseCollection(errMsg, data, false);
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
    public <T extends Serializable> ResponseResult<T> parseCollection(String errMsg, Collection<?
            extends Serializable> data, boolean sealed) {
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
    public <T extends Serializable> ResponseResult<T> parseCollection(String okMsg, String errMsg, Collection<?
            extends Serializable> data) {
        return parseCollection(okMsg, errMsg, data, false);
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
    public <T extends Serializable> ResponseResult<T> parseCollection(String okMsg, String errMsg, Collection<?
            extends Serializable> data, boolean sealed) {
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
    public <T extends Serializable> ResponseResult<T> parseCollection(String errMsg, int errCode, Collection<?
            extends Serializable> data) {
        return parseCollection(errMsg, errCode, data, false);
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
    public <T extends Serializable> ResponseResult<T> parseCollection(String errMsg, int errCode, Collection<?
            extends Serializable> data, boolean sealed) {
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
    public <T extends Serializable> ResponseResult<T> parseCollection(String okMsg, String errMsg, int errCode,
                                                                      Collection<? extends Serializable> data) {
        return parseCollection(okMsg, errMsg, errCode, data, false);
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
    public <T extends Serializable> ResponseResult<T> parseCollection(String okMsg, String errMsg, int errCode,
                                                                      Collection<? extends Serializable> data,
                                                                      boolean sealed) {
        if (CollectionUtil.isEmpty(data)) {
            return errorResult(errCode, errMsg);
        } else {
            if (sealed) {
                BootConfig.getFieldEncoder().encode(data);
            }
            return new ResponseResult<T>(okCode, okMsg).castData(data);
        }
    }
}
