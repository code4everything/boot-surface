## Spring注解缓存

本模块简单的实现了 `Google Guava Cache` 和 `Redis` 的 `Spring` 缓存版，还没有使用过 `Spring Cache` 的小伙伴，可以自行谷歌了解下


#### 创建缓存管理器

``` java
@Bean
public GuavaCacheManager guavaCacheManager() {
    // @formatter:off
    CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .softValues()
            .initialCapacity(128)
            .maximumSize(1024);
    // @formatter:on
    return CacheUtils.newGuavaCacheManager(cacheBuilder, "cache1", "cache2");
}
```

``` java
@Bean
public RedisCacheManager redisCacheManager() {
    Map<String, RedisCacheCreator> creatorMap = new HashMap<>(2, 1);
    // 不同的缓存使用不同的缓存策略
    creatorMap.put("cache1", new RedisCacheCreator(60 * 60));
    creatorMap.put("cache2", new RedisCacheCreator(60 * 60 * 24));
    return CacheUtils.newRedisCacheManager(creatorMap);
}
```

提供多种创建方式，你可以选择任意你喜欢的方式创建

#### 开启缓存

``` java
@EnableCaching
```

#### 使用注解缓存

``` java
@Cacheable(value = "cache1", key = "#root.method", cacheManager = "guavaCacheManager")
```

或者

``` java
@Cacheable(value = "cache2", key = "#root.args", cacheManager = "redisCacheManager")
```

当然，你也可以不使用注解，直接在代码中手动缓存，使用你用到的 `CacheManager` 即可