package org.code4everything.boot.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 文件信息
 *
 * @author pantao
 * @since 2018/11/2
 **/
public class MultipartFileBean implements Serializable {

    /**
     * MD5文件名
     *
     * @since 1.0.0
     */
    private String filename;

    /**
     * 原始文件名
     *
     * @since 1.0.0
     */
    private String originalFilename;

    /**
     * MD5码
     *
     * @since 1.0.0
     */
    private String md5;

    /**
     * 文件大小
     *
     * @since 1.0.0
     */
    private Long size;

    /**
     * 自定义参数
     *
     * @since 1.0.0
     */
    private Map<String, Serializable> params;

    /**
     * 获取自定义参数
     *
     * @return 自定义参数
     *
     * @since 1.0.0
     */
    public Map<String, Serializable> getParams() {
        return params;
    }

    /**
     * 设置自定义参数
     *
     * @param params 自定义参数
     *
     * @return {@link MultipartFileBean}
     *
     * @since 1.0.0
     */
    public MultipartFileBean setParams(Map<String, Serializable> params) {
        this.params = params;
        return this;
    }

    /**
     * 获取文件名（MD5）
     *
     * @return MD5文件名
     *
     * @since 1.0.0
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 设置文件名（MD5）
     *
     * @param filename MD5文件名
     *
     * @return {@link MultipartFileBean}
     *
     * @since 1.0.0
     */
    public MultipartFileBean setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * 获取原始文件名
     *
     * @return 原始文件名
     *
     * @since 1.0.0
     */
    public String getOriginalFilename() {
        return originalFilename;
    }

    /**
     * 设置原始文件名
     *
     * @param originalFilename 原始文件名
     *
     * @return {@link MultipartFileBean}
     *
     * @since 1.0.0
     */
    public MultipartFileBean setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
        return this;
    }

    /**
     * 获取MD5码
     *
     * @return MD5码
     *
     * @since 1.0.0
     */
    public String getMd5() {
        return md5;
    }

    /**
     * 设置MD5码
     *
     * @param md5 MD5码
     *
     * @return {@link MultipartFileBean}
     *
     * @since 1.0.0
     */
    public MultipartFileBean setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    /**
     * 获取文件大小
     *
     * @return 文件大小
     */
    public Long getSize() {
        return size;
    }

    /**
     * 设置文件大小
     *
     * @param size 文件大小
     *
     * @return {@link MultipartFileBean}
     *
     * @since 1.0.0
     */
    public MultipartFileBean setSize(Long size) {
        this.size = size;
        return this;
    }
}
