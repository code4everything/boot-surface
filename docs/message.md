## 消息类

本来并没有计划封装这部分代码的，因为复用的代码也比较少，封装的部分也不多，而且封装后，仍然需要配置，但是最后我还是心烦了，特别是验证码部分，
所以下决心还是封装了一下，虽然意义不太大，而且目前封装也比较简单，希望以后能不断完善吧

#### 邮箱消息

- 在`pom.xml`中添加邮箱依赖

    ``` xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
    ```

- 在`application.properties`或`application.yml`中配置邮箱

    ``` properties
    spring.mail.host=smtp.exmail.qq.com
    spring.mail.protocol=smtp
    spring.mail.username=your.email@email-domain.com
    spring.mail.password=your.password.or.key
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    spring.mail.properties.mail.smtp.starttls.required=true
    ```
    
- 将邮箱配置传递到`BootSurface`库

    ``` java
    @Configuration
    public class EmailConfiguration {
    
        @Autowired
        public EmailConfiguration(JavaMailSender mailSender, @Value("${spring.mail.username}") String outbox) {
            BootConfig.setMailSender(outbox, mailSender);
        }
    }
    ```
    
- 到这里，配置就结束了，下面测试一下如何发送一封简单的邮件

    ``` java
    EmailUtils.sendEmail("sendto@example.com", "your subject", "your message");
    ```
    
#### 验证码

如果是通过邮件发送验证码，那么你需要先配置邮箱，请参考上面的说明

> 目前仅支持邮箱发送验证码，短信验证码后期会完善的

- 发送验证码

    ``` java
    VerifyCodeUtils.sendVerifyCodeByEmailAsync("sendto@example.com", "验证码", "你的验证码：{}，请不要告诉其他人哦");
    ```
    
- 校验验证码

    ``` java
    // 验证码正确将返回true，否则返回false
    boolean result = VerifyCodeUtils.validateVerifyCode("sendto@example.com", "123456");
    ```
    
- 从缓存中移除验证码

    ``` java
    VerifyCodeUtils.removeVerifyCode("sendto@example.com");
    ```
    
- 检测频率（一分钟）

    ``` java
    // 如果在一分钟内返回true，否则返回false
    boolean result = VerifyCodeUtils.isFrequently("sendto@example.com");
    ```
