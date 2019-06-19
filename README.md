## 项目简介

Boot Surface主要是一个依赖于Spring框架的Web开发工具类，旨在简化日志记录、文件上传、异常处理等一些繁琐而又必要的代码编写。
这也是从工作项目中剥离出来的一些公共代码，方便以后一些项目的构建。

> 本工具库主要适用于 `Spring Boot Web` 项目的开发

## 主要功能

- Spring AOP日志记录

- 对HTTP某一些响应数据的字段进行抹掉或加密

- 简化文件的上传

- 默认的异常处理和拦截器

- 封装了一些常用的常量

## 安装使用

项目仅支持 JDK1.8 以上

[API参考文档](https://apidoc.gitee.com/code4everything/boot-surface/)

``` xml
<dependency>
    <groupId>org.code4everything</groupId>
    <artifactId>boot-surface</artifactId>
    <version>1.1.4</version>
</dependency>
```

> 为了方便打包，项目已发布到了`Maven`中央仓库

## 使用手册

- [配置说明](docs/config.md)

- [简化日志记录](docs/log.md)

- [HTTP响应实体的封装](docs/response.md)

- [简化文件上传](docs/upload.md)

- [默认异常处理](docs/exception.md)

- [默认拦截器](docs/interceptor.md)

- [跨域工具类](docs/cors.md)

- [缓存工具类](docs/redis.md)

- [集合的封装](docs/collection.md)

- [消息类](docs/message.md)

- [Spring注解缓存](docs/cache.md)

## 示例项目

[https://gitee.com/code4everything/wanna-spring/tree/master/spring-bee](https://gitee.com/code4everything/wanna-spring/tree/master/spring-bee)

## 写在最后

本项目严格遵守阿里巴巴Java开发手册规范，采用MIT开源协议
