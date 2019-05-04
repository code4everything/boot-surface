package org.code4everything.boot.encoder;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 字段加密
 *
 * @author pantao
 * @since 2018/11/11
 **/
public class DefaultFieldEncoder implements FieldEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFieldEncoder.class);

    /**
     * 加密字段
     *
     * @param field 字段
     * @param data 对象
     * @param sealed 加密方法
     *
     * @return 该字段是否加了密
     *
     * @since 1.0.0
     */
    protected boolean encodeField(Field field, Object data, Sealed sealed) {
        if (Objects.isNull(field) || Objects.isNull(data) || Objects.isNull(sealed)) {
            return false;
        }
        Class<?> type = field.getType();
        try {
            Object object = field.get(data);
            if (type != String.class) {
                // 不是字符串类型的继续遍历
                return encodeField(object);
            }
            String value = String.valueOf(object);
            switch (sealed.value()) {
                case "md5":
                    field.set(data, DigestUtil.md5Hex(value));
                    break;
                case "sha1":
                    field.set(data, DigestUtil.sha1Hex(value));
                    break;
                case "sha256":
                    field.set(data, DigestUtil.sha256Hex(value));
                    break;
                default:
                    field.set(data, sealed.value());
                    break;
            }
            return true;
        } catch (IllegalAccessException e) {
            LOGGER.error("encrypt field {} failed, message -> {}", field.getName(), e.getMessage());
        }
        return false;
    }

    @Override
    public final boolean encodeField(Object data) {
        if (Objects.isNull(data) || data instanceof CharSequence || ObjectUtil.isBasicType(data)) {
            return false;
        }
        if (data instanceof Collection) {
            return arrayEncodeHelper(((Collection) data).toArray());
        }
        if (data instanceof Map) {
            return arrayEncodeHelper(((Map) data).values().toArray());
        }
        if (data.getClass().isArray()) {
            return arrayEncodeHelper((Object[]) data);
        }
        boolean encoded = false;
        // 遍历属性字段
        Field[] fields = data.getClass().getDeclaredFields();
        for (Field field : fields) {
            Sealed sealed = field.getAnnotation(Sealed.class);
            if (ObjectUtil.isNotNull(sealed)) {
                field.setAccessible(true);
                if (encodeField(field, data, sealed)) {
                    encoded = true;
                }
            }
        }
        return encoded;
    }

    private boolean arrayEncodeHelper(Object[] data) {
        for (Object datum : data) {
            if (!encodeField(datum)) {
                // 集合内对象无加密的字段
                return false;
            }
        }
        return true;
    }
}
