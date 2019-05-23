package org.code4everything.boot.web.http;

import org.code4everything.boot.base.bean.BaseBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 文件信息
 *
 * @author pantao
 * @since 2018/11/2
 **/
public class DustFile implements BaseBean {

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
    private long size;

    /**
     * 自定义参数
     *
     * @since 1.0.0
     */
    private Map<String, Object> params;

    /**
     * 文件保存路径
     *
     * @since 1.0.6
     */
    private String storagePath;

    DustFile() {}

    /**
     * 添加参数
     *
     * @param key 键
     * @param value 值
     *
     * @since 1.1.2
     */
    public void addParam(String key, Object value) {
        if (Objects.isNull(params)) {
            params = new HashMap<>(8);
        }
        params.put(key, value);
    }

    /**
     * 获取参数
     *
     * @param key 键
     *
     * @return 参数值
     *
     * @since 1.1.2
     */
    public Object getParam(String key) {
        return Objects.isNull(params) ? null : params.get(key);
    }

    /**
     * 获取参数
     *
     * @param key 键
     *
     * @return 参数值
     *
     * @since 1.1.2
     */
    public String getParamString(String key) {
        Object value = getParam(key);
        return Objects.isNull(value) ? null : String.valueOf(value);
    }

    /**
     * 获取文件保存路径
     *
     * @return 文件保存路径
     *
     * @since 1.0.6
     */
    public String getStoragePath() {
        return storagePath;
    }

    /**
     * 设置文件保存路径
     *
     * @param storagePath 文件保存路径
     *
     * @since 1.0.6
     */
    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    /**
     * 获取自定义参数
     *
     * @return 自定义参数
     *
     * @since 1.0.0
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * 设置自定义参数
     *
     * @param params 自定义参数
     *
     * @return {@link DustFile}
     *
     * @since 1.0.0
     */
    public DustFile setParams(Map<String, Object> params) {
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
     * @return {@link DustFile}
     *
     * @since 1.0.0
     */
    public DustFile setFilename(String filename) {
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
     * @return {@link DustFile}
     *
     * @since 1.0.0
     */
    public DustFile setOriginalFilename(String originalFilename) {
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
     * @return {@link DustFile}
     *
     * @since 1.0.0
     */
    public DustFile setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    /**
     * 获取文件大小
     *
     * @return 文件大小
     */
    public long getSize() {
        return size;
    }

    /**
     * 设置文件大小
     *
     * @param size 文件大小
     *
     * @return {@link DustFile}
     *
     * @since 1.0.0
     */
    public DustFile setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public String toString() {
        return "DustFile{" + "filename='" + filename + '\'' + ", originalFilename='" + originalFilename + '\'' + ", " + "md5='" + md5 + '\'' + ", size=" + size + ", params=" + params + ", storagePath='" + storagePath + '\'' + '}';
    }
}
