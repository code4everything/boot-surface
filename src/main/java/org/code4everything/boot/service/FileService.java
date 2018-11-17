package org.code4everything.boot.service;

import org.code4everything.boot.bean.MultipartFileBean;

import java.io.Serializable;

/**
 * 文件服务，如果没有对应数据表，可以不实现接口中的方法
 *
 * @author pantao
 * @since 2018/11/2
 **/
public interface FileService<T extends Serializable> {

    /**
     * 检测当前 {@link MultipartFileBean}是否存在，如果文件不存在则写入磁盘，否则不写入，返回NULL时会尝试调用 {@link #getBy(MultipartFileBean)}。
     * <br>如果文件存在时也需要返回文件信息，请实现 {@link #getBy(MultipartFileBean)}方法，不要实现本方法
     *
     * @param fileBean {@link MultipartFileBean}
     *
     * @return 是否存在
     *
     * @since 1.0.0
     */
    default Boolean exists(MultipartFileBean fileBean) {return null;}

    /**
     * 获取一条数据库记录，当返回值为NULL（表示文件不存在）时将文件写入磁盘，否则不写入。如果 {@link #exists(MultipartFileBean)}方法返回 NULL时才会调用本方法
     *
     * @param fileBean {@link MultipartFileBean}
     *
     * @return T 一个实体类
     *
     * @since 1.0.0
     */
    default T getBy(MultipartFileBean fileBean) {return null;}

    /**
     * 定义将数据写入数据库中的方法
     *
     * @param fileBean {@link MultipartFileBean}
     *
     * @return T 实体类
     *
     * @since 1.0.0
     */
    default T save(MultipartFileBean fileBean) {return null;}
}
