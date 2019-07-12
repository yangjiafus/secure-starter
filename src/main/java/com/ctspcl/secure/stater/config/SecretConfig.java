package com.ctspcl.secure.stater.config;

import com.ctspcl.secure.stater.SecretCoreValidator;
import com.ctspcl.secure.stater.filter.DecryptRequestFilter;
import com.ctspcl.secure.stater.filter.EncryptResponseFilter;
import com.ctspcl.secure.stater.strategy.Aes128CbcDecryptBehavior;
import com.ctspcl.secure.stater.strategy.Aes128CbcEncryptBehavior;
import com.ctspcl.secure.stater.strategy.DecryptRequestShouldFilterStrategy;
import com.ctspcl.secure.stater.strategy.EncryptResponseShouldFilterStrategy;
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
    public SecretCoreValidator secretCoreValidator(SecretProperty secretProperty){
        return new SecretCoreValidator(secretProperty);
    }

    @Bean
    @ConditionalOnMissingBean(DecryptRequestFilter.class)
    public DecryptRequestFilter encryptRequestFilter(SecretProperty secretProperty){
        return new DecryptRequestFilter(decryptRequestShouldFilterStrategy(secretProperty),aes128CBCDecryptBehavior(secretProperty));
    }

    @Bean
    @ConditionalOnMissingBean(EncryptResponseFilter.class)
    public EncryptResponseFilter encryptResponseFilter(SecretProperty secretProperty){
        return new EncryptResponseFilter(encryptResponseShouldFilterStrategy(secretProperty),aes128CBCEncryptBehavior(secretProperty));
    }

    @Bean
    @ConditionalOnMissingBean(EncryptResponseShouldFilterStrategy.class)
    public EncryptResponseShouldFilterStrategy encryptResponseShouldFilterStrategy(SecretProperty secretProperty){
        return new EncryptResponseShouldFilterStrategy(secretProperty,aes128CBCEncryptBehavior(secretProperty));
    }

    @Bean
    @ConditionalOnMissingBean(DecryptRequestShouldFilterStrategy.class)
    public DecryptRequestShouldFilterStrategy decryptRequestShouldFilterStrategy(SecretProperty secretProperty){
        return new DecryptRequestShouldFilterStrategy(secretProperty,null);
    }

    @Bean
    @ConditionalOnMissingBean(Aes128CbcDecryptBehavior.class)
    public Aes128CbcDecryptBehavior aes128CBCDecryptBehavior(SecretProperty secretProperty){
        return new Aes128CbcDecryptBehavior(secretProperty,secretCoreValidator(secretProperty));
    }

    @Bean
    @ConditionalOnMissingBean(Aes128CbcEncryptBehavior.class)
    public Aes128CbcEncryptBehavior aes128CBCEncryptBehavior(SecretProperty secretProperty){
        return new Aes128CbcEncryptBehavior(secretProperty);
    }
}
