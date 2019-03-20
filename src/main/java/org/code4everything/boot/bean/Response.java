package org.code4everything.boot.bean;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.annotation.JSONField;
import org.code4everything.boot.config.BootConfig;
import org.code4everything.boot.constant.IntegerConsts;
import org.code4everything.boot.constant.MessageConsts;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * HTTP响应实体
 *
 * @author pantao
 * @since 2018/10/30
 */
public class Response<T> implements Serializable {

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
    private int code = IntegerConsts.ZERO;

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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

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
            BootConfig.getFieldEncoder().encode(data);
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
    public Timestamp getTimestamp() {
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
    public Response<T> setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * 设置为当前时间戳
     *
     * @return {@link Response}
     *
     * @since 1.0.9
     */
    public Response<T> setTimestamp() {
        return setTimestamp(new Timestamp(System.currentTimeMillis()));
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
        return this.error(HttpStatus.HTTP_BAD_REQUEST, errMsg);
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
        BootConfig.getFieldEncoder().encode(this.getData());
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
     * 转字符串
     *
     * @return 字符串
     *
     * @since 1.0.5
     */
    @Override
    public String toString() {
        return "Response{code=" + code + ", msg='" + msg + '\'' + ", data=" + data + ", timestamp=" + timestamp + '}';
    }

    /**
     * 不比较时间戳
     *
     * @param obj 对象
     *
     * @return 是否相等
     *
     * @since 1.0.5
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Response) {
            Response result = (Response) obj;
            return code == result.code && msg.equals(result.msg) && Objects.equals(data, result.data);
        }
        return false;
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
        int hasCode = 7;
        hasCode = hasCode * 23 + code;
        hasCode = hasCode * 23 + msg.hashCode();
        return data == null ? hasCode : hasCode * 23 + data.hashCode();
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeInt(code);
        outputStream.writeBoolean(sealed);
        outputStream.writeObject(msg);
        outputStream.writeObject(timestamp);
        outputStream.writeObject(data);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        inputStream.readInt();
        inputStream.readBoolean();
        msg = (String) inputStream.readObject();
        timestamp = (Timestamp) inputStream.readObject();
        data = (T) inputStream.readObject();
    }
}
