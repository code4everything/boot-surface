## 配置说明

> 本库所有的设置都可以通过 `BootConfit` 类来设置，类下所有方法均为静态方法

### 在 `Spring Boot` 应用中使用 `properties` 进行配置

开启属性配置

``` java
@EnableSurfaceConfiguration
```

在 `application.properties` 或 `application.yml` 中配置

``` properties
# 只需要关心自己所需要的配置属性即可
boot.surface.max-upload-file-size=1024
boot.surface.debug=false
boot.surface.sealed=false
boot.surface.ok-code=0
boot.surface.frequency=1000
boot.surface.rest-server=http:127.0.0.1

# 配置邮箱
boot.surface.mail.host=smtp.qq.com
boot.surface.mail.port=25
boot.surface.mail.protocol=smtp
boot.surface.mail.username=example@qq.com
boot.surface.mail.password=123456

# 配置缓存
boot.surface.redis.host=127.0.0.1
boot.surface.redis.port=6379
boot.surface.redis.db=1
```

> 当使用BootSurface属性配置 `Mail` 和 `Redis` 时，无需使用注解 `@EnableSurfaceRedis` `@EnableSurfaceMail` 手动开启相关功能，
否则可能导致配置无效

### 设置日志缓存

``` java
setLogCache(com.google.common.cache.Cache)
// 例如：
BootConfig.setLogCache(CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build());
```

### 设置拦截器名单

``` java
setFilterPath(org.code4everything.boot.web.mvc.FilterPath)
// 例如：
BootConfit.setFilterPath(new FilterPath().setInterceptPrefixes(new String[]{"/user", "/custom"}));
```

### 设置缓存连接

``` java
// set host 
initJedisConnectionFactory(String)
// set host, port
initJedisConnectionFactory(String, Integer)
// set host, port, database
initJedisConnectionFactory(String, Integer, Integer)

// 例如
BootConfig.initJedisConnectionFactory("127.0.0.1", 6379)
```

> 如果是默认的主机和端口无需任何设置

### 设置最大文件上传大小

``` java
setMaxUploadFileSize(long)
```

### 是否开启调试

``` java
setDebug(boolean)
```

> 开启后会打印详细的日志信息

### 设置HTTP响应字段加密

``` java
// 是否对响应字段加密
setSealed(boolean)
// 加密方法
setFieldEncoder(org.code4everything.boot.base.encoder.DefaultFieldEncoder)
```

> [用法参考](response.md)

> [PROPERTY配置样本](../src/main/resources/application-sample.properties)

> [JSON配置样本](../src/main/resources/boot-config-sample.json)

> [LOGBACK日志配置样本](../src/main/resources/logback-sample.xml)
