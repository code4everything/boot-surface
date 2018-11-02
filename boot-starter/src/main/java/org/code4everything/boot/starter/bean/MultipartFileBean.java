package org.code4everything.boot.starter.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 文件信息
 *
 * @author pantao
 * @since 2018/11/2
 **/
public class MultipartFileBean implements Serializable {

    private String filename;

    private String originalFilename;

    private String md5;

    private Long size;

    private Map<String, Serializable> otherParams;

    public Map<String, Serializable> getOtherParams() {
        return otherParams;
    }

    public MultipartFileBean setOtherParams(Map<String, Serializable> otherParams) {
        this.otherParams = otherParams;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public MultipartFileBean setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public MultipartFileBean setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public MultipartFileBean setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public MultipartFileBean setSize(Long size) {
        this.size = size;
        return this;
    }
}
