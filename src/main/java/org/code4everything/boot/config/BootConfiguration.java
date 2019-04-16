package org.code4everything.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author pantao
 * @since 2019/4/16
 **/
@Configuration
@EnableConfigurationProperties(BootConfigProperties.class)
public class BootConfiguration {

    @Autowired
    public BootConfiguration(BootConfigProperties properties) {
        BootConfig.setFrequency(properties.getFrequency());
        BootConfig.setOkCode(properties.getOkCode());
        BootConfig.setSealed(properties.getSealed());
        BootConfig.setDebug(properties.getDebug());
        BootConfig.setMaxUploadFileSize(properties.getMaxUploadFileSize());
    }
}
