## 跨域工具类

有时候我们的项目需要允许跨域，Spring Boot的跨域设置其实已经很简单了，但是还不够简单，现在我们在Spring Boot项目中只需要下面几行代码即可实现跨域

``` java
@Bean
public CorsFilter corsFilter() {
    return CorsUtils.newCorsFilter();
}
```

或者这样，只允许指定路径可以跨域

``` java
@Bean
public CorsFilter corsFilter() {
    return CorsUtils.newCorsFilter("/api/**");
}
```

还或者这样，自定义跨域

``` java
@Bean
public CorsFilter corsFilter() {
    return CorsUtils.newCorsFilter(new CorsLane().setPath("/**").setCredential(true).setHeaders("*").setMethods("GET", "POST", "DELETE", "PUT", "OPTIONS").setOrigins("yourdomain.com"));
}
```
