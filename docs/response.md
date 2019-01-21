## HTTP响应实体的封装

为了方便前端的开发，后端同学往往都需要写接口文档，而我们可以借助丝袜哥（Swagger）可以很方便的生成接口文档，同时借助本工具可以更详细的生成响应字段信息，或字段加密功能

> [如何使用丝袜哥](https://blog.csdn.net/qq_26954773/article/details/81364352)

#### 假设有一个视图对象

``` java
import org.code4everything.boot.annotations.Sealed;

import java.io.Serializable;

public class DemoVO implements Serializable {

    /**
     * 对需要加密或抹掉的字段使用 {@link Sealed} 注解
     */
    @Sealed
    private String key;

    private Integer count;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
```

#### 控制器示例代码

``` java
import org.code4everything.boot.bean.Response;
import org.code4everything.boot.web.mvc.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController extends BaseController {

    private static final DemoVO DEMO_VO = new DemoVO();

    static {
        DEMO_VO.setCount(233);
        DEMO_VO.setKey("secret key");
    }

    /**
     * 在 {@link BaseController} 类中封装了很多 `parseResult` 方法，同时也建议使用 parseResult 方法
     */
    @GetMapping("/foo")
    public ResponseResult<DemoVO> foo() {
        // 最后一个参数表示是否对对象字段进行加密
        return parseResult("success", "error", DEMO_VO, false);
    }

    @GetMapping("/bar")
    public ResponseResult<DemoVO> bar() {
        return new ResponseResult<DemoVO>().setCode(200).setMsg("success").setData(DEMO_VO).encode();
    }
}
```

> 默认的字段加密器仅支持 `md5` `sha1` `sha256` 三个加密方法，如果有使用其他加密方法的需求请继承 [`FieldEncoder`](../src/main/java/org/code4everything/boot/encoder/FieldEncoder.java) 类并重写
`encodeField` 方法，然后在主类的 `main` 方法中调用 `BootConfig.setFieldEncoder`
