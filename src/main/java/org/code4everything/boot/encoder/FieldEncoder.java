package org.code4everything.boot.encoder;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.apache.log4j.Logger;
import org.code4everything.boot.annotations.Sealed;
import org.springframework.util.ClassUtils;

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
public class FieldEncoder {

    protected static final Logger LOGGER = Logger.getLogger(FieldEncoder.class);

    /**
     * 加密字段
     *
     * @param field 字段
     * @param object 对象
     * @param sealed 加密方法
     *
     * @since 1.0.0
     */
    protected void encodeField(Field field, Object object, Sealed sealed) {
        if (Objects.isNull(field) || Objects.isNull(object) || Objects.isNull(sealed)) {
            return;
        }
        try {
            String value = String.valueOf(field.get(object));
            switch (sealed.value()) {
                case "md5":
                    field.set(object, DigestUtil.md5Hex(value));
                    break;
                case "sha1":
                    field.set(object, DigestUtil.sha1Hex(value));
                    break;
                case "sha256":
                    field.set(object, DigestUtil.sha256Hex(value));
                    break;
                default:
                    field.set(object, sealed.value());
                    break;
            }
        } catch (IllegalAccessException e) {
            LOGGER.error(StrUtil.format("encrypt field {} failed, message -> ", field.getName(), e.getMessage()));
        }
    }

    /**
     * 对对象字段进行加密
     *
     * @param data 加密
     *
     * @return 是否进行了加密
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public boolean encode(Object data) {
        if (Objects.isNull(data) || data instanceof CharSequence || ObjectUtil.isBasicType(data)) {
            return false;
        }
        if (data instanceof Collection) {
            // 遍历集合
            for (Object datum : ((Collection) data)) {
                if (!encode(datum)) {
                    // 集合内对象无加密的字段
                    return false;
                }
            }
            return true;
        } else if (data instanceof Map) {
            // 遍历字典
            for (Object value : ((Map) data).values()) {
                if (!encode(value)) {
                    // 字典内对象无加密的字段
                    return false;
                }
            }
            return true;
        } else {
            boolean res = false;
            // 遍历属性字段
            Field[] fields = data.getClass().getDeclaredFields();
            for (Field field : fields) {
                Sealed sealed = field.getAnnotation(Sealed.class);
                if (ObjectUtil.isNotNull(sealed)) {
                    if (!res) {
                        res = true;
                    }
                    // 对字段属性加密
                    encode(field, data, sealed);
                }
            }
            return res;
        }
    }

    private void encode(Field field, Object object, Sealed sealed) {
        Class<?> type = field.getType();
        if (type == String.class || ClassUtils.isPrimitiveWrapper(type)) {
            // 对基本类型直接加密
            field.setAccessible(true);
            encodeField(field, object, sealed);
        }
        try {
            // 不是基本类型的继续遍历
            encode(field.get(object));
        } catch (IllegalAccessException e) {
            LOGGER.error(StrUtil.format("encrypt field {} failed, message -> ", field.getName(), e.getMessage()));
        }
    }
}
