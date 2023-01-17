package com.jjhroy.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * qq配置属性
 *
 * @author yezhqiu
 * @date 2021/06/14
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "wx")
public class WxConfigProperties {

    /**
     * wx appId
     */
    private String appId;

    /**
     * 校验token地址
     */
    private String checkTokenUrl;

    /**
     * QQ用户信息地址
     */
    private String userInfoUrl;

}
