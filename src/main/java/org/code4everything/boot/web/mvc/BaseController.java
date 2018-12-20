package org.code4everything.boot.web.mvc;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.google.common.base.Strings;
import org.code4everything.boot.base.AssertUtils;
import org.code4everything.boot.bean.ResponseResult;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.constant.MessageConsts;
import org.code4everything.boot.exception.template.TokenBlankException;
import org.code4everything.boot.exception.template.UserUnloggedException;
import org.code4everything.boot.service.UserService;
import org.code4everything.boot.web.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

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

    @Autowired
    protected HttpServletRequest request;

    /**
     * 获取Token
     *
     * @return Token
     *
     * @since 1.0.0
     */
    protected String getToken() {
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
    protected <T extends Serializable> T getUser(UserService<T> userService) {
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
     * @throws UserUnloggedException 未登录异常
     * @since 1.0.4
     */
    protected <T extends Serializable> T requireUser(UserService<T> userService) throws UserUnloggedException {
        return AssertUtils.assertUserLoggedIn(userService.getUserByToken(requireToken()));
    }

    /**
     * 获取Token
     *
     * @return Token
     *
     * @throws TokenBlankException TOKEN 为空异常
     * @since 1.0.4
     */
    protected String requireToken() throws TokenBlankException {
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
    protected <T extends Serializable> ResponseResult<T> successResult() {
        return new ResponseResult<>();
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
    protected <T extends Serializable> ResponseResult<T> successResult(String okMsg) {
        return new ResponseResult<T>().setMsg(okMsg);
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
    protected <T extends Serializable> ResponseResult<T> successResult(String okMsg, T data) {
        return new ResponseResult<>(okMsg, data);
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
    protected <T extends Serializable> ResponseResult<T> errorResult(String errMsg) {
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
    protected <T extends Serializable> ResponseResult<T> errorResult(int errCode, String errMsg) {
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
    protected ResponseResult<Boolean> parseBoolResult(String errMsg, boolean isOk) {
        return parseBoolResult(DEFAULT_OK_MSG, errMsg, isOk);
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
    protected ResponseResult<Boolean> parseBoolResult(String okMsg, String errMsg, boolean isOk) {
        return new ResponseResult<Boolean>().setMsg(isOk ? okMsg : errMsg).setData(isOk);
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
    protected <T extends Serializable> ResponseResult<T> parseResult(String errMsg, T data) {
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
    protected <T extends Serializable> ResponseResult<T> parseResult(String errMsg, T data, boolean sealed) {
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
    protected <T extends Serializable> ResponseResult<T> parseResult(String errMsg, int errCode, T data) {
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
    protected <T extends Serializable> ResponseResult<T> parseResult(String errMsg, int errCode, T data,
                                                                     boolean sealed) {
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
    protected <T extends Serializable> ResponseResult<T> parseResult(String okMsg, String errMsg, T data) {
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
    protected <T extends Serializable> ResponseResult<T> parseResult(String okMsg, String errMsg, T data,
                                                                     boolean sealed) {
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
    protected <T extends Serializable> ResponseResult<T> parseResult(String okMsg, String errMsg, int errCode, T data) {
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
    protected <T extends Serializable> ResponseResult<T> parseResult(String okMsg, String errMsg, int errCode, T data
            , boolean sealed) {
        boolean isError = ObjectUtil.isNull(data);
        if (!isError) {
            if (data instanceof Boolean && !(Boolean) data) {
                isError = true;
            } else if (sealed) {
                BootConfig.getFieldEncoder().encode(data);
            }
        }
        return isError ? new ResponseResult<>(errCode, errMsg) : new ResponseResult<>(okMsg, data);
    }
}
