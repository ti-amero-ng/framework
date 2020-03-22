package com.huang.framework.authority.properties;

import java.util.Map;

/**
 * @author -Huang
 * @create 2020-03-19 8:18
 */
public class OAuth2Properties {
    private String jwtSigningKey = "security";

    private Map<String, Object> tokenInfo;

    public Map<String, Object> getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(Map<String, Object> tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    private OAuth2ClientProperties[] clients = {};

    public OAuth2ClientProperties[] getClients() {
        return clients;
    }

    public void setClients(OAuth2ClientProperties[] clients) {
        this.clients = clients;
    }

    public String getJwtSigningKey() {
        return jwtSigningKey;
    }

    public void setJwtSigningKey(String jwtSigningKey) {
        this.jwtSigningKey = jwtSigningKey;
    }
}
