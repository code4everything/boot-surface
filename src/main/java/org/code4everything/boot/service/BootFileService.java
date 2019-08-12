package org.code4everything.boot.service;

import org.code4everything.boot.web.http.DustFile;

import javax.annotation.Nullable;

/**
 * 文件服务，如果没有对应数据表，可以不实现接口中的方法。如果你不需要MD5校验，那么建议你在{@link #exists(DustFile)}方法或{@link #getBy(DustFile)}方法中调用{@link
 * DustFile#setFilename(String)}格式化文件名
 *
 * @author pantao
 * @since 2018/11/2
 */
public interface BootFileService<T> {

    /**
     * 检测当前 {@link DustFile}是否存在，如果文件不存在则写入磁盘，否则不写入，返回NULL时会尝试调用 {@link #getBy(DustFile)}。
     * <br>如果文件存在时也需要返回文件信息，请实现 {@link #getBy(DustFile)}方法，不要实现本方法
     *
     * @param dustFile {@link DustFile}
     *
     * @return 是否存在
     *
     * @since 1.0.0
     */
    default Boolean exists(DustFile dustFile) {return null;}

    /**
     * 获取一条数据库记录，当返回值为NULL（表示文件不存在）时将文件写入磁盘，否则不写入。如果 {@link #exists(DustFile)}方法返回 NULL时才会调用本方法
     *
     * @param dustFile {@link DustFile}
     *
     * @return T 一个实体类
     *
     * @since 1.0.0
     */
    default T getBy(DustFile dustFile) {return null;}

    /**
     * 定义将数据写入数据库中的方法
     *
     * @param dustFile {@link DustFile}
     *
     * @return T 实体类
     *
     * @since 1.0.0
     */
    default T save(DustFile dustFile) {return null;}

    /**
     * 定义将数据写入数据库中的方法，针对开启了文件强制写入，如果没有开启强制写入那么无需实现此方法
     *
     * @param dustFile {@link DustFile}
     * @param file 如果文件存在，并且开启了强制写入，将传入一个最新的文件实体，注意进行空指针判断（为NULL表示文件不存在，没有强制写入，
     *         否则表示文件存在并强制写入了，为了防止数据冲突，不应将dustFile保存到数据库）；如果没有开启强制写入，那么无需考虑这个参数
     *
     * @return T 实体类
     *
     * @since 1.0.5
     */
    default T save(DustFile dustFile, @Nullable T file) {return save(dustFile);}

    /**
     * 通过访问链接获取本地路径
     *
     * @param accessUrl 访问路径
     *
     * @return 本地路径
     *
     * @since 1.0.2
     */
    default String getLocalPathByAccessUrl(String accessUrl) {return null;}
}
