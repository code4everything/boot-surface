package org.code4everything.boot.config;

import org.code4everything.boot.service.BootUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 * @author pantao
 * @since 2019/4/16
 **/
@Configuration
@EnableConfigurationProperties(BootConfigProperties.class)
public class BootConfiguration {

    @Autowired
    public BootConfiguration(BootConfigProperties properties, @Nullable BootUserService userService) {
        BootConfig.setConfig(properties);
        BootConfig.setUserService(userService);
    }
}
