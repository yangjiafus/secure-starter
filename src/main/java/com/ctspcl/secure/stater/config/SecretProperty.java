package com.ctspcl.secure.stater.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/3
 **/
@ConfigurationProperties(prefix = "secure.secret")
@RefreshScope
@Data
public class SecretProperty {

    private String priKeySecret;

    private String ivKeySecret;

    private Boolean onOff;

    private Boolean onOffAuthorize;

    private List<String> whitelist;

    private List<String> blacklist;

}
