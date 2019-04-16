package org.code4everything.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author pantao
 * @since 2019/4/16
 **/
@Configuration
@ConditionalOnClass(JavaMailSender.class)
public class BootMailConfiguration {

    @Autowired
    public BootMailConfiguration(JavaMailSender sender, @Value("${spring.mail.username}") String outbox) {
        BootConfig.setMailSender(outbox, sender);
    }
}
