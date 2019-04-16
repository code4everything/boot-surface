package org.code4everything.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author pantao
 * @since 2019/4/16
 **/
@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
public class BootRedisConfiguration {

    @Autowired
    public BootRedisConfiguration(RedisConnectionFactory factory) {
        BootConfig.setRedisConnectionFactory(factory);
    }
}
