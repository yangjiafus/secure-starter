package com.ctspcl.secure.stater.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author JiaFu.yang
 * @description
 * @date 2019/7/3
 **/
@ConfigurationProperties("secure.secret")
@Data
public class SecretProperty {

    private String priKeySecret;

    private String pubKeySecret;

    private List<String> whitelist;

    private List<String> blacklist;

}
