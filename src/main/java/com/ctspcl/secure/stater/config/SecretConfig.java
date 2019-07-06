package com.ctspcl.secure.stater.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/3
 **/
@Configuration
@EnableConfigurationProperties(SecretProperty.class)
@ConditionalOnClass({SecretCoreValidator.class,SecretProperty.class})
@ComponentScan("com.ctspcl.secure.stater")
public class SecretConfig {

    @Bean
    @ConditionalOnMissingBean(SecretCoreValidator.class)
    public SecretCoreValidator secretCoreController(SecretProperty secretProperty){
        return new SecretCoreValidator(secretProperty);
    }

}
