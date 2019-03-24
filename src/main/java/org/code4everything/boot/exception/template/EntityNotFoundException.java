package org.code4everything.boot.exception.template;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.code4everything.boot.exception.BootException;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author pantao
 * @since 2019/3/20
 **/
public class EntityNotFoundException extends BootException {

    /**
     * 已创建对象的存储器，有效期一天
     *
     * @since 1.0.9
     */
    private static Cache<String, EntityNotFoundException> cache = null;

    /**
     * 普通构造函数
     *
     * @param code 错误码
     * @param msg 消息
     * @param responseOk 是否响应200OK
     *
     * @since 1.0.9
     */
    public EntityNotFoundException(int code, String msg, boolean responseOk) {
        super.setCode(code);
        super.setMsg(msg);
        if (responseOk) {
            super.setStatus(HttpStatus.OK);
        } else {
            super.setStatus(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 仅内部使用
     *
     * @param code 错误码
     * @param entityName 实体名称
     *
     * @since 1.0.9
     */
    private EntityNotFoundException(int code, String entityName) {
        this(code, "entity '" + entityName + "' not found", true);
    }

    /**
     * 将使用entityName作为键值存储对象，减少对象的创建，增加对象的复用
     *
     * @param code 错误码
     * @param entityName 实体名称
     *
     * @return {@link EntityNotFoundException}
     *
     * @since 1.0.9
     */
    public static EntityNotFoundException getInstance(int code, String entityName) {
        if (Objects.isNull(cache)) {
            synchronized (EntityNotFoundException.class) {
                // 双重检测
                if (Objects.isNull(cache)) {
                    cache = CacheBuilder.newBuilder().expireAfterWrite(24, TimeUnit.HOURS).build();
                }
            }
        }
        Objects.requireNonNull(entityName, "entity name must not be null");
        EntityNotFoundException entityNotFoundException = cache.getIfPresent(entityName);
        if (Objects.isNull(entityNotFoundException)) {
            // 此处不考虑并发，最坏也就创建多个相同对象，多余的让垃圾回收器收集吧
            entityNotFoundException = new EntityNotFoundException(code, entityName);
            cache.put(entityName, entityNotFoundException);
        }
        return entityNotFoundException;
    }
}
