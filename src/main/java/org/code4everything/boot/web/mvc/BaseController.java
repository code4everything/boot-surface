package org.code4everything.boot.web.mvc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.base.Strings;
import org.code4everything.boot.base.AssertUtils;
import org.code4everything.boot.base.function.BooleanFunction;
import org.code4everything.boot.base.function.ResponseFunction;
import org.code4everything.boot.bean.Response;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.constant.IntegerConsts;
import org.code4everything.boot.constant.MessageConsts;
import org.code4everything.boot.exception.ExceptionThrower;
import org.code4everything.boot.service.UserService;
import org.code4everything.boot.web.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    private static final int DEFAULT_ERROR_CODE = -1;

    private static final String DEFAULT_OK_MSG = MessageConsts.REQUEST_OK_ZH;

    private static int okCode = IntegerConsts.ZERO;

    @Autowired
    public HttpServletRequest request;

    private ThreadLocal<Response<?>> resultThreadLocal = new ThreadLocal<>();

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
    public <T> Response<T> getReturn() {
        Response<?> responseResult = resultThreadLocal.get();
        if (Objects.isNull(responseResult)) {
            return null;
        } else {
            Response<T> result = new Response<T>().copyFrom(responseResult);
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
    public <T> BaseController ifReturn(boolean shouldReturn, Response<T> result) {
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
    public BaseController ifReturn(boolean shouldReturn, ResponseFunction function) {
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
    public BaseController ifReturn(BooleanFunction function, Response result) {
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
    public BaseController ifReturn(BooleanFunction booleanFunction, ResponseFunction responseResultFunction) {
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
    public <T> BaseController elseReturn(Response<T> result) {
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
    public BaseController elseReturn(ResponseFunction function) {
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
    public <T> Response<T> getReturn(Response<T> result) {
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
    public <T> Response<T> getReturn(ResponseFunction function) {
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
    public <T> T getUser(UserService<T> userService) {
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
    public <T> T requireUser(UserService<T> userService) {
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
     * @return {@link Response}
     *
     * @since 1.0.4
     */
    public <T> Response<T> successResult() {
        return new Response<T>().setCode(okCode);
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
        return new Response<T>().setMsg(okMsg).setCode(okCode);
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
        return new Response<>(okCode, okMsg, data);
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
     * @since 1.0.5
     */
    public <T> Response<T> successResult(int okCode, String okMsg, T data) {
        return new Response<>(okCode, okMsg, data);
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
        return new Response<T>().error(errMsg);
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
        return new Response<T>().error(errCode, errMsg);
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
        return new Response<Boolean>().setMsg(isOk ? okMsg : errMsg).setData(isOk).setCode(okCode);
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
        if (CollectionUtil.isEmpty(data)) {
            return errorResult(errCode, errMsg);
        } else {
            if (sealed) {
                BootConfig.getFieldEncoder().encode(data);
            }
            return new Response<>(okCode, okMsg, data);
        }
    }
}
