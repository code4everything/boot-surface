package org.code4everything.boot.web.mvc.exception;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.http.HttpStatus;

/**
 * @author pantao
 * @since 2019/4/30
 */
public interface ExceptionBiscuit {

    /**
     * 获取错误码
     *
     * @return 错误码
     *
     * @since 1.1.1
     */
    int getCode();

    /**
     * 自定义格式化
     *
     * @param params 参数
     *
     * @return 消息
     *
     * @since 1.1.3
     */
    default String getMsgs(Object... params) {
        return ArrayUtil.isEmpty(params) ? getMsg() : String.format(getMsg(), params);
    }

    /**
     * 获取消息
     *
     * @return 消息
     *
     * @since 1.1.1
     */
    String getMsg();

    /**
     * 获取响应状态
     *
     * @return 响应状态
     *
     * @since 1.1.1
     */
    HttpStatus getStatus();
}
