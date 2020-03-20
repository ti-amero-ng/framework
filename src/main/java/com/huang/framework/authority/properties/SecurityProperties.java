package com.huang.framework.authority.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author -Huang
 * @create 2020-03-19 8:19
 */
@Component
@ConfigurationProperties(prefix = "framework.security")
public class SecurityProperties {
    private OAuth2Properties oauth2 = new OAuth2Properties();

    public OAuth2Properties getOauth2() {
        return oauth2;
    }

    public void setOauth2(OAuth2Properties oauth2) {
        this.oauth2 = oauth2;
    }
}
