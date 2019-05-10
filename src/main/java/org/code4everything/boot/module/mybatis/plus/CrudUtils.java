package org.code4everything.boot.module.mybatis.plus;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.code4everything.boot.base.bean.BaseDomain;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 增删改查工具类
 *
 * @author pantao
 * @since 2019/5/8
 **/
public final class CrudUtils {

    private CrudUtils() {}

    /**
     * 根据 ID 更新
     *
     * @param mapper {@link BaseMapper}
     * @param t 实体
     * @param <T> 实体类型
     *
     * @return 新旧值对
     *
     * @since 1.1.2
     */
    public static <T> UpdatedPair<T> updateById(BaseMapper<T> mapper, T t) {
        Serializable id = null;
        if (t instanceof BaseDomain) {
            id = ((BaseDomain) t).primaryKey();
        }
        return updateById(mapper, t, id);
    }

    /**
     * 根据 ID 更新
     *
     * @param mapper {@link BaseMapper}
     * @param t 实体
     * @param id ID
     * @param <T> 实体类型
     *
     * @return 新旧值对
     *
     * @since 1.1.2
     */
    public static <T> UpdatedPair<T> updateById(BaseMapper<T> mapper, T t, Serializable id) {
        T old = null;
        if (Objects.isNull(id)) {
            mapper.updateById(t);
        } else {
            old = mapper.selectById(id);
            if (ObjectUtil.isNotNull(old)) {
                mapper.updateById(t);
            }
        }
        return new UpdatedPair<>(old, t);
    }

    /**
     * 插入一条记录
     *
     * @param mapper {@link BaseMapper}
     * @param t 实体
     * @param <T> 实体类型
     *
     * @return 实体
     *
     * @since 1.1.2
     */
    public static <T> T insert(BaseMapper<T> mapper, T t) {
        mapper.insert(t);
        return t;
    }

    /**
     * 批量删除
     *
     * @param mapper {@link BaseMapper}
     * @param collection ID 集合
     * @param <T> 实体类型
     *
     * @return 删除的实体
     *
     * @since 1.1.2
     */
    public static <T> List<T> deleteBatchIds(BaseMapper<T> mapper, Collection<? extends Serializable> collection) {
        List<T> list = mapper.selectBatchIds(collection);
        if (CollUtil.isNotEmpty(list)) {
            mapper.deleteBatchIds(collection);
        }
        return list;
    }

    /**
     * 条件删除
     *
     * @param mapper {@link BaseMapper}
     * @param map 条件集合
     * @param <T> 实体类型
     *
     * @return 删除的实体
     *
     * @since 1.1.2
     */
    public static <T> List<T> deleteByMap(BaseMapper<T> mapper, Map<String, Object> map) {
        List<T> list = mapper.selectByMap(map);
        if (CollUtil.isNotEmpty(list)) {
            mapper.deleteByMap(map);
        }
        return list;
    }

    /**
     * 条件删除
     *
     * @param mapper {@link BaseMapper}
     * @param wrapper {@link Wrapper}
     * @param <T> 实体类型
     *
     * @return 删除的实体
     *
     * @since 1.1.2
     */
    public static <T> List<T> delete(BaseMapper<T> mapper, Wrapper<T> wrapper) {
        List<T> list = mapper.selectList(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            mapper.delete(wrapper);
        }
        return list;
    }

    /**
     * 根据 ID 删除
     *
     * @param mapper {@link BaseMapper}
     * @param id ID
     * @param <T> 实体类型
     *
     * @return 删除的实体
     *
     * @since 1.1.2
     */
    public static <T> T deleteById(BaseMapper<T> mapper, Serializable id) {
        T t = mapper.selectById(id);
        if (ObjectUtil.isNotNull(t)) {
            mapper.deleteById(id);
        }
        return t;
    }
}
