## 默认拦截器

> 在WEB项目开发中少不了对访问路径进行拦截过滤或一些权限验证的需求，虽然有像Shiro、Spring Security这样成熟的框架，但是对于个人的一些小项目来说还是显得有一些重，而且配置繁琐，所以我最常用的还是拦截器，并进行了第二次封装

#### 首先我们需要在主类的 `main` 方法中设置名单

``` java
ConfigBean bean = new ConfigBean();
bean.setInterceptPrefixes(new String[]{"/user", "/custom"});
bean.setInterceptPrefixes(new String[]{"/common"});
DefaultWebInterceptor.setConfigBean(bean);
```

#### 然后准备一个实现了 `WebMvcConfigurer` 接口的配置类
           
``` java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {}
```

#### 重写 `addInterceptors` 方法

``` java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new DefaultWebInterceptor(new InterceptHandler() {
        @Override
        public void handleBlackList(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // 处理黑名单，可不重写此方法
        }

        @Override
        public void handleWhiteList(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // 处理白名单，可不重写此方法
        }

        @Override
        public boolean handleInterceptList(HttpServletRequest request, HttpServletResponse response,
                                           Object handler) throws Exception {
            // 你可以在这里进行一些复杂的权限验证
            return Validator.isNotEmpty(request.getHeader("token"));
        }
    }));
}
```

> 拦截器主要是根据配置的黑名单、白名单、需要权限验证的名单前缀进行相应的处理

#### 完整的示例代码参考

``` java
import cn.hutool.core.lang.Validator;
import org.code4everything.boot.interfaces.InterceptHandler;
import org.code4everything.boot.web.mvc.DefaultWebInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DefaultWebInterceptor(new InterceptHandler() {
            @Override
            public void handleBlackList(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                // 处理黑名单，可不重写此方法
            }

            @Override
            public void handleWhiteList(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                // 处理白名单，可不重写此方法
            }

            @Override
            public boolean handleInterceptList(HttpServletRequest request, HttpServletResponse response,
                                               Object handler) throws Exception {
                // 你可以在这里进行一些复杂的权限验证
                return Validator.isNotEmpty(request.getHeader("token"));
            }
        }));
    }
}
```
