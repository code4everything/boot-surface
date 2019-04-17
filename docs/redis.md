## 缓存工具类

每次使用 `RedisTemplate` 都需要设置连接工厂，以及键值的序列方式，显得很是繁琐，所以封装了新建 `RedisTemplate` 的工具方法

#### 设置连接信息

> 如果是默认的主机和端口无需设置

``` java
@Autowired
public YourRedisConfigurationCustructor(RedisConnectionFactory redisConnectionFactory) {
    BootConfig.setRedisConnectionFactory(redisConnectionFactory);
}
```

或直接使用注解

``` java
@EnableSurfaceRedisTemplate
```

#### 新建 `RedisTemplate`

``` java
@Bean
public RedisTemplate<String, Long> longRedisTemplate() {
    return RedisTemplateUtils.newTemplate(Long.class);
}
```

> 键如果是 `String` 类型的就使用 `StringRedisSerializer` 进行序列化，否则与值的序列化方式一样，值使用 `FastJsonRedisSerializer` 进行序列化
