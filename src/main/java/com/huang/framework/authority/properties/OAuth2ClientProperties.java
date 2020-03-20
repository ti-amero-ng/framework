package com.huang.framework.authority.properties;

import lombok.Data;

/**
 * 客户端参数
 * @author -Huang
 * @create 2020-03-19 7:48
 */
@Data
public class OAuth2ClientProperties {
    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 客户端 secret
     */
    private String clientSecret;
    /**
     * token有效期
     */
    private int accessTokenValiditySeconds;
    /**
     * 授权模式
     */
    private String[] authorizedGrantTypes = {};
    /**
     * 信任的回调域
     */
    private String[] redirectUris = {};
    /**
     * 客户端权限
     */
    private String[] scopes = {};
}
