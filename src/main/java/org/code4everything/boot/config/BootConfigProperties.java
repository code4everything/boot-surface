package org.code4everything.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author pantao
 * @since 2019/4/16
 **/
@ConfigurationProperties("boot.surface")
public class BootConfigProperties {


    /**
     * 最大文件上传大小
     *
     * @since 1.1.0
     */
    private Long maxUploadFileSize;

    /**
     * 是否开启调试模式
     *
     * @since 1.1.0
     */
    private Boolean debug;

    /**
     * 是否给响应字段加密
     *
     * @since 1.1.0
     */
    private Boolean sealed;

    /**
     * 正确响应码
     *
     * @since 1.1.0
     */
    private Integer okCode;

    /**
     * 请求频率
     *
     * @since 1.1.0
     */
    private Integer frequency;

    /**
     * 是否统计访问数据
     *
     * @since 1.1.0
     */
    private Boolean visitLog;

    /**
     * Rest Template 默认地址
     *
     * @since 1.1.2
     */
    private String restServer;

    public String getRestServer() {
        return restServer;
    }

    public void setRestServer(String restServer) {
        this.restServer = restServer;
    }

    public Boolean getVisitLog() {
        return visitLog;
    }

    public void setVisitLog(Boolean visitLog) {
        this.visitLog = visitLog;
    }

    public Long getMaxUploadFileSize() {
        return maxUploadFileSize;
    }

    public void setMaxUploadFileSize(Long maxUploadFileSize) {
        this.maxUploadFileSize = maxUploadFileSize;
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    public Boolean getSealed() {
        return sealed;
    }

    public void setSealed(Boolean sealed) {
        this.sealed = sealed;
    }

    public Integer getOkCode() {
        return okCode;
    }

    public void setOkCode(Integer okCode) {
        this.okCode = okCode;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "BootConfigProperties{" + "maxUploadFileSize=" + maxUploadFileSize + ", debug=" + debug + ", sealed=" + sealed + ", okCode=" + okCode + ", frequency=" + frequency + ", visitLog=" + visitLog + ", restServer='" + restServer + '\'' + '}';
    }
}
