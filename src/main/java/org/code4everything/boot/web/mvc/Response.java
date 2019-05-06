package org.code4everything.boot.web.mvc;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.code4everything.boot.base.constant.IntegerConsts;
import org.code4everything.boot.base.constant.MessageConsts;
import org.code4everything.boot.base.constant.StringConsts;
import org.code4everything.boot.config.BootConfig;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * HTTP响应实体
 *
 * @author pantao
 * @since 2018/10/30
 */
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -5763007029340547926L;

    private static int okCode = IntegerConsts.ZERO;

    /**
     * 是否对数据进行加密
     *
     * @since 1.0.0
     */
    private boolean sealed = false;

    /**
     * 错误码
     *
     * @since 1.0.0
     */
    private int code = Response.okCode;

    /**
     * 消息
     *
     * @since 1.0.0
     */
    private String msg = MessageConsts.REQUEST_OK_ZH;

    /**
     * 数据
     *
     * @since 1.0.0
     */
    private transient T data = null;

    /**
     * 时间戳
     *
     * @since 1.0.0
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 时间
     *
     * @since 1.1.0
     */
    private Date datetime;

    /**
     * 无参构造函数
     *
     * @since 1.0.0
     */
    public Response() {}

    /**
     * 设置数据
     *
     * @param data 数据
     *
     * @since 1.0.0
     */
    public Response(T data) {
        this.data = data;
    }

    /**
     * 设置消息和数据
     *
     * @param msg 消息
     * @param data 数据
     *
     * @since 1.0.0
     */
    public Response(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    /**
     * 设置错误码和消息
     *
     * @param code 错误码
     * @param msg 消息
     *
     * @since 1.0.0
     */
    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 设置错误码、消息和数据
     *
     * @param code 错误码
     * @param msg 消息
     * @param data 数据
     *
     * @since 1.0.5
     */
    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 获取正确码
     *
     * @return 正确码
     *
     * @since 1.0.5
     */
    public static int getOkCode() {
        return Response.okCode;
    }

    /**
     * 设置正确码
     *
     * @param okCode 正确码
     *
     * @since 1.1.0
     */
    public static void setOkCode(Integer okCode) {
        if (ObjectUtil.isNotNull(okCode)) {
            Response.okCode = okCode;
        }
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     *
     * @since 1.0.0
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置错误码
     *
     * @param code 错误码
     *
     * @return 当前对象
     *
     * @since 1.0.0
     */
    public Response<T> setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * 获取消息
     *
     * @return 消息
     *
     * @since 1.0.0
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置消息
     *
     * @param msg 消息
     *
     * @return 当前对象
     *
     * @since 1.0.0
     */
    public Response<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 调试模式时，打印到终端
     *
     * @return {@link Response}
     *
     * @since 1.1.0
     */
    public Response<T> debug() {
        return debug(null);
    }

    /**
     * 调试模式时，打印到终端
     *
     * @param req {@link HttpServletRequest}
     *
     * @return {@link Response}
     *
     * @since 1.1.0
     */
    public Response<T> debug(HttpServletRequest req) {
        if (BootConfig.isDebug()) {
            return println(req);
        }
        return this;
    }

    /**
     * 打印到终端
     *
     * @return {@link Response}
     *
     * @since 1.1.0
     */
    public Response<T> println() {
        log(formatDateAndClass() + " - " + formatResponse());
        return this;
    }

    /**
     * 打印到终端
     *
     * @param req {@link HttpServletRequest}
     *
     * @return {@link Response}
     *
     * @since 1.1.0
     */
    public Response<T> println(HttpServletRequest req) {
        if (Objects.isNull(req)) {
            return println();
        }
        // 构建时间、IP地址
        StringBuilder builder = new StringBuilder(formatDateAndClass()).append(" [").append(req.getRemoteAddr());
        // 构建请求方法、接口地址
        builder.append(" ").append(req.getMethod()).append(" ").append(req.getServletPath());
        String queryString = req.getQueryString();
        if (StrUtil.isNotEmpty(queryString)) {
            // 构建QueryString
            builder.append("?").append(queryString);
        }
        // 构建响应信息
        builder.append("] - ").append(formatResponse());
        log(builder.toString());
        return this;
    }

    /**
     * 是否拥有数据
     *
     * @return 是否拥有数据
     *
     * @since 1.1.1
     */
    public boolean hasData() {
        return ObjectUtil.isNotNull(data);
    }

    /**
     * 格式化响应消息
     *
     * @return 格式化的响应消息
     *
     * @since 1.1.0
     */
    private String formatResponse() {
        return code + " " + msg + (Objects.isNull(data) ? "" : " " + data);
    }

    /**
     * 打印响应到终端
     *
     * @param log 响应日志
     *
     * @since 1.1.0
     */
    private void log(String log) {
        if (isOk()) {
            Console.log(log);
        } else {
            Console.error(log);
        }
    }

    /**
     * 格式化日期时间
     *
     * @return 时间字符串
     *
     * @since 1.1.0
     */
    private String formatDateAndClass() {
        return DateUtil.format(getDateTime(), StringConsts.DateFormat.DATE_TIME_MILLIS) + " " + Response.class.getSimpleName();
    }

    /**
     * 获取数据
     *
     * @return 数据
     *
     * @since 1.0.0
     */
    public T getData() {
        return data;
    }

    /**
     * 设置数据
     *
     * @param data 数据
     *
     * @return 当前对象
     *
     * @since 1.0.0
     */
    public Response<T> setData(T data) {
        if (sealed) {
            BootConfig.getFieldEncoder().encodeField(data);
        }
        this.data = data;
        return this;
    }

    /**
     * 获取当前时间戳
     *
     * @return 时间戳
     *
     * @since 1.0.0
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 设置时间戳
     *
     * @param timestamp 时间戳
     *
     * @return {@link Response}
     *
     * @since 1.0.0
     */
    public Response<T> setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     *
     * @since 1.1.0
     */
    public Date getDateTime() {
        if (Objects.isNull(datetime)) {
            datetime = new Date(timestamp);
        } else if (timestamp != datetime.getTime()) {
            // 防篡改
            datetime.setTime(timestamp);
        }
        return datetime;
    }

    /**
     * 设置为当前时间戳
     *
     * @return {@link Response}
     *
     * @since 1.0.9
     */
    public Response<T> setTimestamp() {
        return setTimestamp(System.currentTimeMillis());
    }


    /**
     * 请求失败
     *
     * @param errMsg 错误消息
     *
     * @return {@link Response}
     *
     * @since 1.0.0
     */
    public Response<T> error(String errMsg) {
        return this.error(400, errMsg);
    }

    /**
     * 请求失败
     *
     * @param errCode 错误码
     * @param errMsg 错误消息
     *
     * @return {@link Response}
     *
     * @since 1.0.0
     */
    public Response<T> error(int errCode, String errMsg) {
        return this.setCode(errCode).setMsg(errMsg).setData(null);
    }

    /**
     * 对字段进行加密
     *
     * @return {@link Response}
     *
     * @since 1.0.0
     */
    public Response<T> encode() {
        BootConfig.getFieldEncoder().encodeField(this.getData());
        sealed = true;
        return this;
    }

    /**
     * 复制当前响应对象，不包括数据
     *
     * @param <E> 新的数据类型
     *
     * @return 新的响应对象
     *
     * @since 1.0.0
     */
    public <E> Response<E> copy() {
        return new Response<E>(code, msg).setTimestamp(timestamp);
    }

    /**
     * 复制当前响应对象
     *
     * @param newData 新的类型数据
     * @param <E> 新的数据类型
     *
     * @return 新的响应对象
     *
     * @since 1.0.0
     */
    public <E> Response<E> copy(E newData) {
        return new Response<E>(code, msg).setTimestamp(timestamp).setData(newData);
    }

    /**
     * 从参数对象中复制数据
     *
     * @param result 目标 {@link Response}
     *
     * @return {@link Response}
     *
     * @since 1.0.4
     */
    @SuppressWarnings("unchecked")
    public Response<T> copyFrom(Response<?> result) {
        this.setMsg(result.getMsg()).setCode(result.getCode()).setTimestamp(result.getTimestamp());
        if (ObjectUtil.isNotNull(result.data)) {
            this.setData((T) result.getData());
        }
        return this;
    }

    /**
     * 复制数据到参数对象中
     *
     * @param result 目标 {@link Response}
     * @param <E> 数据类型
     *
     * @return {@link Response}
     *
     * @since 1.0.4
     */
    public <E> Response<E> copyInto(Response<E> result) {
        return result.copyFrom(this);
    }

    /**
     * 响应是否成功
     *
     * @return 响应是否成功
     *
     * @since 1.1.0
     */
    public boolean isOk() {
        return code == Response.okCode;
    }

    /**
     * 响应是否失败
     *
     * @return 响应是否失败
     *
     * @since 1.1.0
     */
    public boolean isError() {
        return !isOk();
    }

    /**
     * 转字符串
     *
     * @return 字符串
     *
     * @since 1.0.5
     */
    @Override
    public String toString() {
        return "Response{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + ", timestamp=" + timestamp + ", datetime=" + getDateTime() + '}';
    }

    /**
     * 不比较时间戳
     *
     * @param o 对象
     *
     * @return 是否相等
     *
     * @since 1.0.5
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response<?> response = (Response<?>) o;
        return code == response.code && Objects.equals(msg, response.msg) && Objects.equals(data, response.data);
    }

    /**
     * 重写哈希
     *
     * @return 哈希
     *
     * @since 1.0.5
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, msg, data);
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeInt(code);
        outputStream.writeLong(timestamp);
        outputStream.writeBoolean(sealed);
        outputStream.writeObject(msg);
        outputStream.writeObject(data);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        inputStream.readInt();
        timestamp = inputStream.readLong();
        inputStream.readBoolean();
        msg = (String) inputStream.readObject();
        data = (T) inputStream.readObject();
    }
}
