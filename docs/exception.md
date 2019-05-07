## 默认异常处理

> 任何项目都存在潜在的异常风险，而我们的职责就是对这些可能发生的异常做出正确的响应，为了方便一些小项目的开发，故封装了一个默认的WEB异常处理类

#### 首先我们需要一个实现了 `WebMvcConfigurer` 接口的配置类

``` java
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {}
```

#### 然后重写 `configureHandlerExceptionResolvers` 方法

一行代码搞定

``` java
@Override
public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
    resolvers.add(new DefaultExceptionHandler());
}
```

或者自定义异常信息，当发生了对应的异常时，会返回你定义的消息内容

``` java
@Override
public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
    DefaultExceptionHandler handler = new DefaultExceptionHandler();
    // 添加自定义异常信息
    handler.addException(400, "参数验证失败", HttpStatus.BAD_REQUEST, MethodArgumentNotValidException.class);
    // 在这里你还可以添加更多的异常
    resolvers.add(handler);
}
```

> 你还可以直接在代码中抛出 `HttpException` 异常或任何继承自 `HttpException` 的异常，亦或者是任何实现了 `ExceptionBiscuit` 的异常

#### 完整的示例代码参考

``` java
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        DefaultExceptionHandler handler = new DefaultExceptionHandler();
        handler.addException(400, "参数验证失败", HttpStatus.BAD_REQUEST, MethodArgumentNotValidException.class);
        resolvers.add(handler);
    }
}
```
