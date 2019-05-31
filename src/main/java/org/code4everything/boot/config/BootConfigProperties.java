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

    /**
     * Redis 配置
     *
     * @since 1.1.3
     */
    private RedisConfigProperties redis;

    private MailConfigProperties mail;

    public MailConfigProperties getMail() {
        return mail;
    }

    public void setMail(MailConfigProperties mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "BootConfigProperties{" + "maxUploadFileSize=" + maxUploadFileSize + ", debug=" + debug + ", sealed=" + sealed + ", okCode=" + okCode + ", frequency=" + frequency + ", visitLog=" + visitLog + ", restServer='" + restServer + '\'' + ", redis=" + redis + ", mail=" + mail + '}';
    }

    public RedisConfigProperties getRedis() {
        return redis;
    }

    public void setRedis(RedisConfigProperties redis) {
        this.redis = redis;
    }

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

}

class MailConfigProperties {

    /**
     * 主机
     *
     * @since 1.1.3
     */
    private String host;

    /**
     * 端口
     *
     * @since 1.1.3
     */
    private Integer port;

    /**
     * 协议
     *
     * @since 1.1.3
     */
    private String protocol;

    /**
     * 用户名（发件箱）
     *
     * @since 1.1.3
     */
    private String username;

    /**
     * 密码或授权密钥
     *
     * @since 1.1.3
     */
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "MailConfigProperties{" + "host='" + host + '\'' + ", port=" + port + ", protocol='" + protocol + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }
}

class RedisConfigProperties {

    /**
     * Redis 主机
     *
     * @since 1.1.3
     */
    private String host;

    /**
     * Redis 端口
     *
     * @since 1.1.3
     */
    private Integer port;

    /**
     * Redis 数据库
     *
     * @since 1.1.3
     */
    private Integer db;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getDb() {
        return db;
    }

    public void setDb(Integer db) {
        this.db = db;
    }

    @Override
    public String toString() {
        return "RedisConfigProperties{" + "host='" + host + '\'' + ", port=" + port + ", db=" + db + '}';
    }
}
