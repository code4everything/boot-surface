package org.code4everything.boot.web.mvc;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import org.code4everything.boot.bean.ResponseResult;
import org.code4everything.boot.constant.MessageConsts;
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
        String token = request.getHeader(MessageConsts.TOKEN_EN);
        if (StrUtil.isEmpty(token)) {
            token = request.getParameter(MessageConsts.TOKEN_EN);
        }
        return token;
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
        return new ResponseResult<Boolean>().setMsg(isOk ? okMsg : errMsg);
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
        return parseResult(DEFAULT_OK_MSG, errMsg, DEFAULT_ERROR_CODE, data);
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
        return parseResult(DEFAULT_OK_MSG, errMsg, errCode, data);
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
        return parseResult(okMsg, errMsg, DEFAULT_ERROR_CODE, data);
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
        return Validator.isNull(data) ? new ResponseResult<>(errCode, errMsg) : new ResponseResult<>(okMsg, data);
    }
}
