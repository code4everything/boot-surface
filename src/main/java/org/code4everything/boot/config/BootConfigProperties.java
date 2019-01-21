package org.code4everything.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author pantao
 * @since 2019-01-21
 */
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "aa")
public class BootConfigProperties {}
