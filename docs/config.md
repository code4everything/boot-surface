## 配置说明

> 本库所有的设置都可以通过 `BootConfit` 类来设置，类下所有方法均为静态方法

### 在 `Spring Boot` 应用中使用 `properties` 进行配置

开启属性配置

``` java
@EnableSurfaceConfiguration
```

在 `application.properties` 或 `application.yml` 中配置

``` properties
boot.surface.max-upload-file-size=1024
boot.surface.debug=false
boot.surface.sealed=false
boot.surface.ok-code=0
boot.surface.frequency=1000
```


### 设置日志缓存

``` java
setLogCache(com.google.common.cache.Cache)
// 例如：
BootConfig.setLogCache(CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build());
```

### 设置拦截器名单

``` java
setConfigBean(org.code4everything.boot.bean.ConfigBean)
// 例如：
BootConfit.setConfigBean(new ConfigBean().setInterceptPrefixes(new String[]{"/user", "/custom"}));
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
setFieldEncoder(org.code4everything.boot.encoder.DefaultFieldEncoder)
```

> [用法参考](response.md)
